package com.docfin.persistence.entities

/**
  * Created by amit on 7/24/16.
  */
import com.docfin.persistence.entities.{SimpleSupplier, Supplier}
import spray.json.DefaultJsonProtocol

object JsonProtocol extends DefaultJsonProtocol {
  implicit val supplierFormat = jsonFormat3(Supplier)
  implicit val simpleSupplierFormat = jsonFormat2(SimpleSupplier)
}
