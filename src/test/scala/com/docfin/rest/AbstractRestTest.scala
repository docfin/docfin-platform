package com.docfin.rest

/**
  * Created by amit on 7/24/16.
  */
import akka.testkit.TestProbe
import com.docfin.persistence.dal.TableOperationsAndActions
import com.docfin.persistence.entities.{Supplier, SuppliersTable}
import com.docfin.modules.{PersistenceModule, ConfigurationModuleImpl, ActorModule}
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

    override def config = getConfig.withFallback(super.config)
  }

  def getConfig: Config = ConfigFactory.empty();


}