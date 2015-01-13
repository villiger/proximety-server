package proximety.data.model

import com.github.nscala_time.time.Imports._

case class User(name: String,
                email: String,
                password: String,
                admin: Boolean,
                updated: DateTime = DateTime.now,
                created: DateTime = DateTime.now) extends Model[String] {
  def id = email
}
