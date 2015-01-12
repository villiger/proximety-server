package proximety.data

object Events {
  abstract class Abstract()
  case class Set[V](value: V) extends Abstract
  case class Delete[K](key: K) extends Abstract
}
