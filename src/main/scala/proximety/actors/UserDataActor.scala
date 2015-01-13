package proximety.actors

import proximety.data.{Events, Commands}
import proximety.data.model.User
import com.github.nscala_time.time.Imports._

class UserDataActor extends DataActor[String, User] {
  override def persistenceId = "user-data"

  override def receiveCommand = ({
    case Commands.Set(user: User) =>
      val updated = if (state.contains(user.id)) user.copy(updated = DateTime.now) else user
      persist(Events.Set(updated)) { _ => state.put(updated.id, updated) }
  }: Receive) orElse super.receiveCommand
}
