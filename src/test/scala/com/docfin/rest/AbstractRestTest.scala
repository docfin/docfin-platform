package com.docfin.rest

/**
  * Created by amit on 7/24/16.
  */
import akka.testkit.TestProbe
import com.docfin.persistence.dal.TableOperationsAndActions
import com.docfin.persistence.entities.{MedicalPractitionerEngagement, _}
import com.docfin.modules.{ActorModule, ConfigurationModuleImpl, PersistenceModule}
import akka.actor.ActorRef
import com.typesafe.config.Config
import com.typesafe.config.ConfigFactory
import akka.testkit.TestActor
import org.specs2.mutable.Specification
import spray.testkit.Specs2RouteTest
import org.specs2.mock.Mockito

trait AbstractRestTest extends Specification with Specs2RouteTest with Mockito{

  trait Modules extends ConfigurationModuleImpl with ActorModule with PersistenceModule {
    val system = AbstractRestTest.this.system

    override val suppliersDal = mock[TableOperationsAndActions[SuppliersTable,Supplier]]
    override val userDal = mock[TableOperationsAndActions[UserTable, User]]
    override val addressDal = mock[TableOperationsAndActions[AddressTable, Address]]
    override val personInfoDal = mock[TableOperationsAndActions[PersonInfoTable, PersonInfo]]
    override val medicalPracticeSpecialityDal = mock[TableOperationsAndActions[MedicalPracticeSpecialityTable, MedicalPracticeSpeciality]]
    override val medicalPractitionerDal = mock[TableOperationsAndActions[MedicalPractitionerTable, MedicalPractitioner]]
    override val medicalServiceProviderDal = mock[TableOperationsAndActions[MedicalServiceProviderTable, MedicalServiceProvider]]
    override val medicalPractitionerEngagementDal = mock[TableOperationsAndActions[MedicalPractitionerEngagementTable, MedicalPractitionerEngagement]]

    override def config = getConfig.withFallback(super.config)
  }

  def getConfig: Config = ConfigFactory.empty()


}