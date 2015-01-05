package proximety.services

import akka.util.Timeout
import org.json4s.{DefaultFormats, Formats}
import proximety.actors.ApiActor
import proximety.data.Adapter
import spray.httpx.Json4sSupport
import scala.concurrent.duration._

trait ApiService extends TeapotService with AuthService with Json4sSupport with Adapter {
  self: ApiActor =>

  implicit val executionContext = scala.concurrent.ExecutionContext.Implicits.global
  implicit val timeout = Timeout(5.seconds)
  implicit val json4sFormats: Formats = DefaultFormats

  val routes =
    TeapotService.routes ~
    AuthService.routes
}
