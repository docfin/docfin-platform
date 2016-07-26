package com.docfin.persistence.entities

/**
  * Created by amit on 7/24/16.
  */
import slick.driver.H2Driver.api._
import slick.lifted.Tag

trait BaseEntity {
  val id : Option[Long]
}

abstract class BaseTable[T](tag: Tag, name: String) extends Table[T](tag, name) {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
}

case class Supplier(id: Option[Long],name: String,desc: String) extends BaseEntity
case class SimpleSupplier(name: String,desc: String)
class SuppliersTable(tag: Tag) extends BaseTable[Supplier](tag, "SUPPLIERS") {
  def name = column[String]("userID")
  def desc = column[String]("last_name")
  def * = (id.?, name, desc) <> (Supplier.tupled, Supplier.unapply)
}
object Suppliers extends TableQuery(new SuppliersTable(_))

case class Address(id: Option[Long], street: String, city: String, country: String, zipCode: String) extends BaseEntity
class AddressTable(tag: Tag) extends BaseTable[Address](tag, "ADDRESSE") {
  def street = column[String]("STREET")
  def city = column[String]("CITY")
  def country = column[String]("COUNTRY")
  def zipCode = column[String]("ZIP_CODE")

  def indexUnique = index("idx_a_sccz_u", (street, city, country, zipCode), unique = true)
  def * = (id.?, street, city, country, zipCode) <> (Address.tupled, Address.unapply)
}
object Addresses extends TableQuery(new AddressTable(_))

case class PersonInfo(id: Option[Long], firstName: String, lastName: String, email: String, addressId: Option[Long]) extends BaseEntity
class PersonInfoTable(tag: Tag) extends BaseTable[PersonInfo](tag, "PERSON_INFO"){
  def firstName = column[String]("FIRST_NAME")
  def lastName = column[String]("LAST_NAME")
  def email = column[String]("EMAIL")
  def addressId = column[Option[Long]]("ADDRESS_ID")

  def address = foreignKey("PI_A_FK", addressId, Addresses)(_.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)

  def IndexEmailUnique = index("idx_pi_e_u", (email), unique = true)

  def * = (id.?, firstName, lastName, email, addressId) <> (PersonInfo.tupled, PersonInfo.unapply)
}
object PersonInformation extends TableQuery(new PersonInfoTable(_))


case class MedicalPracticeSpeciality(id: Option[Long], name:String, description:String) extends BaseEntity
class MedicalPracticeSpecialityTable(tag: Tag) extends BaseTable[MedicalPracticeSpeciality](tag, "MEDICAL_PRACTICE_SPECIALITY" ){
  def name = column[String]("NAME")
  def description = column[String]("DESCRIPTION")

  def indexUnique = index("idx_mps_n_u", (name), unique = true)

  def * = (id.?, name, description) <> (MedicalPracticeSpeciality.tupled, MedicalPracticeSpeciality.unapply)

}
object MedicalPracticeSpecialities extends TableQuery(new MedicalPracticeSpecialityTable(_))

case class User(id: Option[Long], name: String, personInfoId:Long) extends BaseEntity
class UserTable(tag: Tag) extends BaseTable[User](tag, "USERS") {
  def name = column[String]("NAME")
  def personInfoId = column[Long]("PERSON_INFO_ID")

  def personInfo = foreignKey("U_PI_FK", personInfoId, PersonInformation)(_.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)

  def * = (id.?, name, personInfoId) <> (User.tupled, User.unapply)

}
object Users extends TableQuery(new UserTable(_))


case class MedicalPractitioner(id: Option[Long], personInfoId:Long, specialityTypeId:Long) extends BaseEntity
class MedicalPractitionerTable(tag: Tag) extends BaseTable[MedicalPractitioner](tag, "MEDICAL_PRACTITIONER") {
  def personInfoId = column[Long]("PERSON_INFO_ID")
  def specialityTypeId = column[Long]("SPECIALITY_TYPE_ID")

  def personInfo = foreignKey("MP_PI_FK", personInfoId, PersonInformation)(_.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
  def specialityType = foreignKey("MP_MPS_FK", specialityTypeId, MedicalPracticeSpecialities)(_.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)

  def * = (id.?, personInfoId, specialityTypeId) <> (MedicalPractitioner.tupled, MedicalPractitioner.unapply)

}
object MedicalPractitioners extends TableQuery(new MedicalPractitionerTable(_))


case class MedicalServiceProvider(id: Option[Long], addressId: Long, email:String) extends BaseEntity
class MedicalServiceProviderTable(tag: Tag) extends BaseTable[MedicalServiceProvider](tag, "MEDICAL_SERVICE_PROVIDER"){
  def emailId = column[String]("EMAIL_ID")
  def addressId = column[Long]("ADDRESS_ID")

  def address = foreignKey("MSP_A_FK", addressId, Addresses)(_.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)

  def * = (id.?, addressId, emailId) <> (MedicalServiceProvider.tupled, MedicalServiceProvider.unapply)

}
object MedicalServiceProviders extends TableQuery(new MedicalServiceProviderTable(_))

case class MedicalPractitionerEngagement(id: Option[Long], practitionerId:Long, serviceProviderId: Long) extends BaseEntity
class MedicalPractitionerEngagementTable(tag: Tag) extends BaseTable[MedicalPractitionerEngagement](tag, "MEDICAL_PRACTITIONER_ENGAGEMENT"){
  def practitionerId = column[Long]("MEDICAL_PRACTITIONER_ID")
  def serviceProviderId = column[Long]("MEDICAL_SERVICE_PROVIDER_ID")

  def practitioner = foreignKey("MPE_MP_FK", practitionerId, MedicalPractitioners)(_.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
  def serviceProvider = foreignKey("MPE_MSP_FK", serviceProviderId, MedicalServiceProviders)(_.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)

  def * = (id.?, practitionerId, serviceProviderId) <> (MedicalPractitionerEngagement.tupled, MedicalPractitionerEngagement.unapply)
}
object MedicalPractitionerEngagements extends TableQuery(new MedicalPractitionerEngagementTable(_))

case class ConfirmationRequest(id: Option[Long], userId: Long, medicalServiceProviderId: Long, status: Long, processLog: String) extends BaseEntity
case class ConfirmationRequestStatus(id: Option[Long]) extends BaseEntity