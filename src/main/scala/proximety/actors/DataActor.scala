package proximety.actors

import akka.persistence.{SnapshotOffer, PersistentActor}
import proximety.data.{Commands, Events}
import scala.collection.mutable

abstract class DataActor[K, V] extends PersistentActor {
  var state = new mutable.HashMap[K, V]

  override def receiveCommand = {
    case Commands.All => sender() ! state.values
    case Commands.Get(key: K) => sender() ! state.get(key)
    case Commands.Set(key: K, value: V) => persist(Events.Set(key, value)) { _ => state.put(key, value) }
    case Commands.Delete(key: K) => persist(Events.Delete(key)) { _ => state.remove(key) }
    case Commands.Snapshot => saveSnapshot(state)
    case Commands.Status => println(state)
  }

  override def receiveRecover = {
    case Events.Set(key: K, value: V) =>
      println(s"Loading $key: $value")
      state.put(key, value)
    case Events.Delete(key: K) =>
      println(s"Deleting $key")
      state.remove(key)
    case SnapshotOffer(_, snapshot: mutable.HashMap[K, V]) =>
      println(s"Snapshot $snapshot")
      state = snapshot
  }
}
