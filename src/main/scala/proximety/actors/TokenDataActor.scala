package proximety.actors

import proximety.util.Hash
import proximety.data.{Commands, Events}
import proximety.data.model.Token

class TokenDataActor extends DataActor[String, Token] {
  override def persistenceId = "token-data"

  override def receiveCommand = ({
    case Commands.GenerateToken(email) =>
      var found = false
      while(!found) {
        val hash = Hash.sha1(scala.util.Random.nextString(256))
        if (!state.contains(hash)) {
          val token = Token(email, hash)
          persist(Events.Set(hash, token)) { _ => state.put(hash, token) }
          sender() ! token
          found = true
        }
      }
  }: Receive) orElse super.receiveCommand
}
