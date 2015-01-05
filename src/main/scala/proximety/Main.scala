package proximety

import akka.actor.{ActorSystem, Props}
import akka.io.IO
import akka.util.Timeout
import akka.pattern.ask
import spray.can.Http
import scala.concurrent.duration._
import proximety.actors.ApiActor

object Main extends App {

  implicit val system = ActorSystem("proximety")

  val service = system.actorOf(Props[ApiActor], "proximety-api")

  implicit val timeout = Timeout(5.seconds)

  IO(Http) ? Http.Bind(service, interface = "localhost", port = 8080)
}
