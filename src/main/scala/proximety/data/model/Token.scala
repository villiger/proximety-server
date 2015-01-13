package proximety.data.model

import com.github.nscala_time.time.Imports._

case class Token(email: String, hash: String, created: DateTime = DateTime.now) extends Model[String] {
  def id = hash
}
