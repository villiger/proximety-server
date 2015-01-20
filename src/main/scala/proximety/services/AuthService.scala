package proximety.services

import proximety.data.Commands
import proximety.data.model.{User, Token}
import proximety.http.{MessageResponse, ParamRejection}
import proximety.util.Hash
import spray.routing.{AuthenticationFailedRejection, HttpService}
import akka.pattern.ask

case class EmailPassword(email: String, password: String)
case class NewUser(name: String, email: String, password: String)

trait AuthService extends HttpService {
  self: ApiService =>

  object AuthService {
    val routes =
      pathPrefix("auth") {
        path("signup") {
          post {
            entity(as[NewUser]) { data =>
              onSuccess((Data.user ? Commands.Get(data.email)).mapTo[Option[User]]) {
                case Some(_) =>
                  reject(ParamRejection("email", "User with this email address already exists."))
                case _ =>
                  Data.user ! Commands.Set(User(data.name, data.email, Hash.sha1(data.password), admin = false))
                  complete(MessageResponse("User has been created. Get a login token."))
              }
            }
          }
        } ~
        path("token") {
          get {
            complete((Data.token ? Commands.All).mapTo[Iterable[Token]])
          } ~
          post {
            entity(as[EmailPassword]) { data =>
              onSuccess((Data.user ? Commands.Get(data.email)).mapTo[Option[User]]) {
                case Some(user) if user.password == Hash.sha1(data.password) =>
                  complete {
                    (Data.token ? Commands.GenerateToken(data.email)).mapTo[Token].map[Map[String, String]] { token =>
                      Map("token" -> token.hash)
                    }
                  }
                case _ =>
                  reject(AuthenticationFailedRejection(AuthenticationFailedRejection.CredentialsRejected, List()))
              }
            }
          }
        }
      }
  }
}
