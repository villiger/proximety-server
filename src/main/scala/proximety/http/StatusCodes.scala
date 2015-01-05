package proximety.http

object StatusCodes {
  val ImATeaPot = spray.http.StatusCodes.registerCustom(418, "I’m a teapot")
}
