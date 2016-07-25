package com.docfin.persistence.entities

/**
  * Created by amit on 7/24/16.
  */
import slick.driver.H2Driver.api._

case class Supplier(id: Option[Long],name: String,desc: String) extends BaseEntity

case class SimpleSupplier(name: String,desc: String)

class SuppliersTable(tag: Tag) extends BaseTable[Supplier](tag, "SUPPLIERS") {
  def name = column[String]("userID")
  def desc = column[String]("last_name")
  def * = (id.?, name, desc) <> (Supplier.tupled, Supplier.unapply)
}


//Reference models
case class Address(id: Option[Long], street: String, city: String, country: String, zipCode: String) extends BaseEntity
case class PersonInfo(id: Option[Long], firstName: String, lastName: String, email: String, addressId: Option[Long])

case class MedicalPracticeSpeciality(id: Option[Long], name:String, description:String)

case class User(id: Option[Long], name: String, personInfoId:Long)

case class MedicalPractitioner(id: Option[Long], personInfoId:Long, specialityTypeId:Long)
case class MedicalServiceProvider(id: Option[Long], addressId: Long, email:String)
case class MedicalPractitionerEngagement(id: Option[Long], practitionerId:Long, serviceProviderId: Long)

case class ConfirmationRequest(id: Option[Long], userId: Long, medicalServiceProviderId: Long, status: Long, processLog: String)
case class ConfirmationRequestStatus(id: Option[Long])