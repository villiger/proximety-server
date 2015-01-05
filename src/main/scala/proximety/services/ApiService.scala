package proximety.services

import akka.util.Timeout
import org.json4s.{DefaultFormats, Formats}
import proximety.actors.ApiActor
import proximety.data.model.User
import proximety.data.{Commands, Adapter}
import proximety.util.Hash
import spray.httpx.Json4sSupport
import scala.concurrent.duration._
import akka.pattern.ask

trait ApiService extends TeapotService with AuthService with Json4sSupport with Adapter {
  self: ApiActor =>

  implicit val executionContext = scala.concurrent.ExecutionContext.Implicits.global
  implicit val timeout = Timeout(5.seconds)
  implicit val json4sFormats: Formats = DefaultFormats

  // PRELOAD INITIAL DATA
  (Data.user ? Commands.Get("user1@proximety.ch")).mapTo[Option[User]].foreach {
    case None =>
      val user1 = User("user1@proximety.ch", Hash.sha1("1234"), admin = true)
      val user2 = User("user2@proximety.ch", Hash.sha1("1234"), admin = false)
      val user3 = User("user3@proximety.ch", Hash.sha1("1234"), admin = false)

      Data.user ! Commands.Set(user1.email, user1)
      Data.user ! Commands.Set(user2.email, user2)
      Data.user ! Commands.Set(user3.email, user3)
    case _ => // they already exist, we don't have to set them again
  }

  val routes =
    TeapotService.routes ~
    AuthService.routes
}
