package proximety.http

import spray.routing.Rejection

case class ParamRejection(param: String, message: String, value: Option[String] = None) extends Rejection
