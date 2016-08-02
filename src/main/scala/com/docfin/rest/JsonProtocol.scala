package com.docfin.rest

/**
  * Created by amit on 7/24/16.
  */
import com.docfin.model._
import spray.json.DefaultJsonProtocol

object JsonProtocol extends DefaultJsonProtocol {


  implicit val userReceived = jsonFormat5(UserReceived)
  implicit val userSent = jsonFormat4(User)
}
