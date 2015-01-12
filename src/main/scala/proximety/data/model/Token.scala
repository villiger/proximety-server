package proximety.data.model

case class Token(email: String, hash: String) extends Model[String] {
  def id = hash
}
