package proximety.services

import proximety.http.StatusCodes
import spray.routing.HttpService

trait TeapotService  extends HttpService {

  object TeapotService {
    val routes =
      path("teapot") {
        get {
          complete(StatusCodes.ImATeaPot)
        }
      }
  }
}
