package proximety.actors

import scala.util.Random
import proximety.util.Hash
import proximety.data.{Commands, Events}
import proximety.data.model.Token

class TokenDataActor extends DataActor[String, Token] {
  override def persistenceId = "token-data"

  override def receiveCommand = ({
    case Commands.GenerateToken(email) =>
      def generateToken(): Unit = {
        val hash = Hash.sha1(Random.nextString(256))
        if (state.contains(hash)) {
          generateToken()
        } else {
          persist(Events.Set(Token(email, hash))) {
            case Events.Set(token) =>
              state.put(token.id, token)
              sender() ! token
          }
        }
      }
      generateToken()
  }: Receive) orElse super.receiveCommand
}
