package com.docfin.modules

/**
  * Created by amit on 7/24/16.
  */
import akka.actor.{ActorPath, ActorRef, ActorSelection, Props}
import com.docfin.persistence.dal._
import slick.backend.DatabaseConfig
import slick.driver.JdbcProfile
import com.docfin.persistence.entities._
import slick.lifted.TableQuery


trait Profile {
  val profile: JdbcProfile
}


trait DbModule extends Profile{
  val db: JdbcProfile#Backend#Database
}

trait PersistenceModule {
  val suppliersDal: TableOperationsAndActions[SuppliersTable, Supplier]
  val userDal: TableOperationsAndActions[UserTable, User]
  val addressDal: TableOperationsAndActions[AddressTable, Address]
  val personInfoDal: TableOperationsAndActions[PersonInfoTable, PersonInfo]
  val medicalPracticeSpecialityDal: TableOperationsAndActions[MedicalPracticeSpecialityTable, MedicalPracticeSpeciality]
  val medicalPractitionerDal: TableOperationsAndActions[MedicalPractitionerTable, MedicalPractitioner]
  val medicalServiceProviderDal: TableOperationsAndActions[MedicalServiceProviderTable, MedicalServiceProvider]
  val medicalPractitionerEngagementDal: TableOperationsAndActions[MedicalPractitionerEngagementTable, MedicalPractitionerEngagement]

}


trait PersistenceModuleImpl extends PersistenceModule with DbModule{
  this: Configuration  =>

  // use an alternative database configuration ex:
  // private val dbConfig : DatabaseConfig[JdbcProfile]  = DatabaseConfig.forConfig("pgdb")
  private val dbConfig : DatabaseConfig[JdbcProfile]  = DatabaseConfig.forConfig("h2db")

  override implicit val profile: JdbcProfile = dbConfig.driver
  override implicit val db: JdbcProfile#Backend#Database = dbConfig.db

  override val suppliersDal = new TableOperationsAndActions[SuppliersTable,Supplier](Suppliers)
  override val userDal = new TableOperationsAndActions[UserTable, User](Users)
  override val addressDal = new TableOperationsAndActions[AddressTable, Address](Addresses)
  override val personInfoDal = new TableOperationsAndActions[PersonInfoTable, PersonInfo](PersonInformation)
  override val medicalPracticeSpecialityDal = new TableOperationsAndActions[MedicalPracticeSpecialityTable, MedicalPracticeSpeciality](MedicalPracticeSpecialities)
  override val medicalPractitionerDal = new TableOperationsAndActions[MedicalPractitionerTable, MedicalPractitioner](MedicalPractitioners)
  override val medicalServiceProviderDal = new  TableOperationsAndActions[MedicalServiceProviderTable, MedicalServiceProvider](MedicalServiceProviders)
  override val medicalPractitionerEngagementDal = new TableOperationsAndActions[MedicalPractitionerEngagementTable, MedicalPractitionerEngagement](MedicalPractitionerEngagements)
  val self = this

}

