package proximety.data

object Events {
  abstract class Abstract()
  case class Set[K, V](key: K, value: V) extends Abstract
  case class Delete[K](key: K) extends Abstract
}
