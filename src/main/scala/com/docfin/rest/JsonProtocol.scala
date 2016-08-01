package com.docfin.rest

/**
  * Created by amit on 7/24/16.
  */
import com.docfin.model._
import spray.json.DefaultJsonProtocol

object JsonProtocol extends DefaultJsonProtocol {
  implicit val supplierFormat = jsonFormat3(Supplier)
  implicit val simpleSupplierFormat = jsonFormat2(SimpleSupplier)

  implicit val userReceived = jsonFormat3(UserReceived)
  implicit val userSent = jsonFormat3(User)
}
