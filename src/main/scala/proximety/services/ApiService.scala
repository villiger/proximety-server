package proximety.services

import akka.util.Timeout
import org.json4s.{DefaultFormats, Formats}
import proximety.actors.ApiActor
import proximety.data.model.{Token, User}
import proximety.data.{Commands, Adapter}
import proximety.http.ParamRejection
import proximety.util.Hash
import spray.http._
import spray.http.StatusCodes.BadRequest
import spray.httpx.Json4sSupport
import spray.routing.RejectionHandler
import scala.concurrent.duration._
import akka.pattern.ask

trait ApiService extends Adapter with Json4sSupport with TeapotService with AuthService {
  self: ApiActor =>

  implicit val executionContext = scala.concurrent.ExecutionContext.Implicits.global
  implicit val timeout = Timeout(5.seconds)
  implicit val json4sFormats: Formats = DefaultFormats ++ org.json4s.ext.JodaTimeSerializers.all

  def jsonify(response: HttpResponse): HttpResponse = {
    import org.json4s.native.JsonMethods._
    import org.json4s.JsonDSL._
    response.withEntity(HttpEntity(ContentTypes.`application/json`,
      compact(render(
        "message" -> response.entity.asString
      ))
    ))
  }

  implicit val rejectionHandler = RejectionHandler {
    case ParamRejection(param, message, value) :: _ =>
      complete(BadRequest, Map(
        "param" -> param,
        "message" -> message,
        "value" -> value
      ))
    case rejections => mapHttpResponse(jsonify) {
      RejectionHandler.Default(rejections)
    }
  }

  // Preload initial data
  (Data.user ? Commands.Get("user1@proximety.ch")).mapTo[Option[User]].foreach {
    case None =>
      val user1 = User("User 1", "user1@proximety.ch", Hash.sha1("1234"), admin = true)
      val user2 = User("User 2", "user2@proximety.ch", Hash.sha1("1234"), admin = false)
      val user3 = User("User 3", "user3@proximety.ch", Hash.sha1("1234"), admin = false)

      Data.user ! Commands.Set(user1)
      Data.user ! Commands.Set(user2)
      Data.user ! Commands.Set(user3)
    case _ => // they already exist, we don't have to set them again
  }

  val routes =
    respondWithMediaType(MediaTypes.`application/json`) {
      TeapotService.routes ~
      AuthService.routes ~
      path("status" / Segment) {
        case "token" => complete((Data.token ? Commands.All).mapTo[Iterable[Token]])
        case "user" => complete((Data.user ? Commands.All).mapTo[Iterable[User]])
      }
    }
}
