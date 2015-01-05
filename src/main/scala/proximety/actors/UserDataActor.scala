package proximety.actors

import proximety.data.model.User

class UserDataActor extends DataActor[String, User] {
  override def persistenceId = "user-data"
}
