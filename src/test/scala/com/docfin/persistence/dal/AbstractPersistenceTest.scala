package com.docfin.persistence.dal

import akka.actor.{ActorSystem, Props}
import com.docfin.modules._
import com.docfin.persistence.entities.{MedicalPractitionerEngagement, MedicalPractitionerEngagementTable, MedicalPractitionerEngagements, MedicalServiceProviders, _}
import slick.backend.DatabaseConfig
import slick.driver.JdbcProfile
import slick.lifted.TableQuery
import spray.testkit.ScalatestRouteTest
import org.scalatest.Suite
trait AbstractPersistenceTest extends ScalatestRouteTest{  this: Suite =>


  trait Modules extends ConfigurationModuleImpl  with PersistenceModuleTest {
  }


  trait PersistenceModuleTest extends PersistenceModule{
    this: Configuration  =>

    private val dbConfig : DatabaseConfig[JdbcProfile]  = DatabaseConfig.forConfig("h2test")

    implicit val profile: JdbcProfile = dbConfig.driver
    implicit val db: JdbcProfile#Backend#Database = dbConfig.db

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

}