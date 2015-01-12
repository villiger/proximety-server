package proximety.data.model

case class User(name: String, email: String, password: String, admin: Boolean) extends Model[String] {
  def id = email
}
