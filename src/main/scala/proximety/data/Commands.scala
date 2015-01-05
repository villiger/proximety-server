package proximety.data

object Commands {
  abstract class Abstract()
  case object All extends Abstract
  case class Get[K](key: K) extends Abstract
  case class Set[K, V](key: K, value: V) extends Abstract
  case class Delete[K](key: K) extends Abstract
  case class GenerateToken(email: String) extends Abstract
  case object Snapshot extends Abstract
  case object Status extends Abstract
}
