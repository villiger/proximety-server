package proximety.data

import akka.actor.{Props, Actor}
import proximety.actors.{TokenDataActor, UserDataActor}

trait Adapter {
  self: Actor =>

  object Data {
    val user = context.actorOf(Props[UserDataActor], "user-data")
    val token = context.actorOf(Props[TokenDataActor], "token-data")
  }
}
