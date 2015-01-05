package proximety.services

import proximety.data.Commands
import proximety.data.model.Token
import spray.routing.HttpService
import akka.pattern.ask

case class EmailPassword(email: String, password: String)

trait AuthService extends HttpService {
  self: ApiService =>

  object AuthService {
    val routes =
      pathPrefix("auth") {
        pathPrefix("token") {
          get {
            complete((Data.token ? Commands.All).mapTo[Iterable[Token]])
          } ~
          post {
            entity(as[EmailPassword]) { data =>
              complete((Data.token ? Commands.GenerateToken(data.email)).mapTo[Token])
            }
          }
        }
      }
  }
}
