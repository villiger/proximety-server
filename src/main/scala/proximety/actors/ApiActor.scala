package proximety.actors

import akka.actor.Actor
import proximety.data.Adapter
import proximety.services.ApiService

class ApiActor extends Actor with ApiService with Adapter {

  def actorRefFactory = context

  def receive = runRoute(routes)
}
