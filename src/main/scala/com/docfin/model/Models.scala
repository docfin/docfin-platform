package com.docfin.model

/**
  * Created by amit on 7/24/16.
  */
import java.sql.{Date, Timestamp}

import slick.driver.H2Driver.api._
import slick.lifted.Tag

trait BaseEntity {
  val id : Option[Long]
}

abstract class BaseTable[T](tag: Tag, name: String) extends Table[T](tag, name) {
  def id = column[Long]("ID", O.PrimaryKey, O.AutoInc)
}

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

case class User(id: Option[Long], email: String, personInfoId:Long, medicalInsurancePlanId: Option[Long]) extends BaseEntity
case class UserReceived(email: String, firstName: String, lastName: String, medicalInsuranceProviderName: Option[String], medicalInsurancePlanName: Option[String])
class UserTable(tag: Tag) extends BaseTable[User](tag, "USERS") {
  def email = column[String]("EMAIL")
  def personInfoId = column[Long]("PERSON_INFO_ID")
  def medicalInsurancePlanId = column[Option[Long]]("MEDICAL_INSURANCE_PLAN_ID")

  def personInfo = foreignKey("U_PI_FK", personInfoId, PersonInformation)(_.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
  def medicalInsurancePlan = foreignKey("U_MIP_FK", medicalInsurancePlanId, MedicalInsurancePlans)(_.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)

  def indexUnique = index("idx_u_e", (email), unique = true)

  def * = (id.?, email, personInfoId, medicalInsurancePlanId) <> (User.tupled, User.unapply)

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

object  ConfirmationStatus extends Enumeration{
  type ConfirmationStatus = Value
  val  Pending =  Value("Pending")
  val  Confirmed =  Value("Confirmed")
  val  Cancelled =  Value("Cancelled")

  implicit val confirmationStatus = MappedColumnType.base[ConfirmationStatus, String](
    e => e.toString,
    s => ConfirmationStatus.withName(s)
  )
}

import ConfirmationStatus._

case class ConfirmationRequest(id: Option[Long], userId: Long, medicalServiceProviderId: Long, confirmationStatus: ConfirmationStatus, processLog: String, requestedAt: Timestamp, confirmedAt: Option[Timestamp]) extends BaseEntity
class ConfirmationRequestTable(tag: Tag) extends BaseTable[ConfirmationRequest](tag, "CONFIRMATION_REQUEST"){
  def userId = column[Long]("USER_ID")
  def medicalServiceProviderId = column[Long]("MEDICAL_SERVICE_PROVIDER_ID")
  def confirmationStatus =  column[ConfirmationStatus]("CONFIRMATION_STATUS")
  def processLog =  column[String]("PROCESS_LOG")
  def requestedAt =  column[Timestamp]("REQUEST_DATE")
  def confirmedAt = column[Option[Timestamp]]("CONFIRMATION_DATE")

  def user = foreignKey("CR_U_FK", userId, Users)(_.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
  def serviceProvider = foreignKey("CR_SP_FK", medicalServiceProviderId, MedicalServiceProviders)(_.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)

  def indexUnique = index("idx_cr_umr_u", (userId, medicalServiceProviderId, requestedAt), unique = true)

  def * = (id.?, userId, medicalServiceProviderId, confirmationStatus, processLog, requestedAt, confirmedAt) <> (ConfirmationRequest.tupled, ConfirmationRequest.unapply)
}
object ConfirmationRequests extends TableQuery(new ConfirmationRequestTable(_))

case class MedicalInsuranceProvider(id: Option[Long], name: String) extends BaseEntity
class MedicalInsuranceProviderTable(tag: Tag) extends BaseTable[MedicalInsuranceProvider](tag, "MEDICAL_INSURANCE_PROVIDER"){
  def name = column[String]("NAME")

  def indexUnique = index("idx_mip_n_u", (name), unique = true)

  def * = (id.?, name) <> (MedicalInsuranceProvider.tupled, MedicalInsuranceProvider.unapply)
}
object MedicalInsuranceProviders extends TableQuery(new MedicalInsuranceProviderTable(_))


case class MedicalInsurancePlan(id: Option[Long], name: String, medicalInsuranceProviderId: Long) extends BaseEntity
class MedicalInsurancePlanTable(tag: Tag) extends BaseTable[MedicalInsurancePlan](tag, "MEDICAL_INSURANCE_PLAN"){
  def name = column[String]("NAME")
  def medicalInsuranceProviderId = column[Long]("MEDICAL_INSURANCE_PROVIDER_ID")

  def medicalInsuranceProvider = foreignKey("CR_U_FK", medicalInsuranceProviderId, MedicalServiceProviders)(_.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)

  def indexUnique = index("idx_mip_n_u", (name, medicalInsuranceProviderId), unique = true)

  def * = (id.?, name, medicalInsuranceProviderId) <> (MedicalInsurancePlan.tupled, MedicalInsurancePlan.unapply)
}
object MedicalInsurancePlans extends TableQuery(new MedicalInsurancePlanTable(_))


case class MedicalPractitionerInsuranceAssociation(id: Option[Long], medicalPractitionerId: Long, medicalInsurancePlanId: Long) extends BaseEntity
class MedicalPractitionerInsuranceAssociationTable(tag: Tag) extends BaseTable[MedicalPractitionerInsuranceAssociation](tag, "MEDICAL_PRACTITIONER_INS_ASS"){
  def medicalPractitionerId = column[Long]("MEDICAL_PRACTITIONER_ID")
  def medicalInsurancePlanId = column[Long]("MEDICAL_INSURANCE_PLAN_ID")

  def medicalInsurancePlan = foreignKey("MPIA_MIP_FK", medicalInsurancePlanId, MedicalInsurancePlans)(_.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
  def medicalPractitioner = foreignKey("MPIA_MIP_FK", medicalPractitionerId, MedicalPractitioners)(_.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)

  def indexUnique = index("idx_mip_n_u", (medicalInsurancePlanId, medicalPractitionerId), unique = true)

  def * = (id.?, medicalPractitionerId, medicalInsurancePlanId) <> (MedicalPractitionerInsuranceAssociation.tupled, MedicalPractitionerInsuranceAssociation.unapply)
}
object MedicalPractitionerInsuranceAssociations extends TableQuery(new MedicalPractitionerInsuranceAssociationTable(_))
