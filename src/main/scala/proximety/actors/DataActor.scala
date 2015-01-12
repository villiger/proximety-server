package proximety.actors

import akka.persistence.{RecoveryFailure, SnapshotOffer, PersistentActor}
import proximety.data.{Commands, Events}
import proximety.data.model.Model
import scala.collection.mutable

abstract class DataActor[K, V <: Model[K]] extends PersistentActor {
  var state = new mutable.HashMap[K, V]

  override def receiveCommand = {
    case Commands.All => sender() ! state.values
    case Commands.Get(key: K) => sender() ! state.get(key)
    case Commands.Filter(f: ((V) => Boolean)) => sender() ! state.values.filter(f)
    case Commands.Find(f: ((V) => Boolean)) => sender() ! state.values.find(f)
    case Commands.Set(value: V) => persist(Events.Set(value)) { _ => state.put(value.id, value) }
    case Commands.Delete(key: K) => persist(Events.Delete(key)) { _ => state.remove(key) }
    case Commands.Snapshot => saveSnapshot(state)
    case Commands.Status => println(state)
  }

  override def receiveRecover = {
    case Events.Set(value: V) => state.put(value.id, value)
    case Events.Delete(key: K) => state.remove(key)
    case SnapshotOffer(_, snapshot: mutable.HashMap[K, V]) => state = snapshot
    case RecoveryFailure(cause: scala.Throwable) => println("Recovery failure: " + cause.getMessage)
  }
}
