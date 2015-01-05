package proximety.services

import proximety.data.Commands
import proximety.data.model.{User, Token}
import proximety.util.Hash
import spray.routing.{AuthenticationFailedRejection, HttpService}
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
              onSuccess((Data.user ? Commands.Get(data.email)).mapTo[Option[User]]) {
                case Some(user) if user.password == Hash.sha1(data.password) =>
                  complete((Data.token ? Commands.GenerateToken(data.email)).mapTo[Token])
                case _ =>
                  reject(AuthenticationFailedRejection(AuthenticationFailedRejection.CredentialsRejected, List()))
              }
            }
          }
        }
      }
  }
}
