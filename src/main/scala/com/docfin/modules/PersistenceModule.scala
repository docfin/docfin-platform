package com.docfin.modules

/**
  * Created by amit on 7/24/16.
  */
import akka.actor.{ActorPath, ActorRef, ActorSelection, Props}
import com.docfin.persistence.dal._
import slick.backend.DatabaseConfig
import slick.driver.JdbcProfile
import com.docfin.persistence.entities.{Supplier, SuppliersTable}
import slick.lifted.TableQuery


trait Profile {
  val profile: JdbcProfile
}


trait DbModule extends Profile{
  val db: JdbcProfile#Backend#Database
}

trait PersistenceModule {
  val suppliersDal: TableOperationsAndActions[SuppliersTable,Supplier]
}


trait PersistenceModuleImpl extends PersistenceModule with DbModule{
  this: Configuration  =>

  // use an alternative database configuration ex:
  // private val dbConfig : DatabaseConfig[JdbcProfile]  = DatabaseConfig.forConfig("pgdb")
  private val dbConfig : DatabaseConfig[JdbcProfile]  = DatabaseConfig.forConfig("h2db")

  override implicit val profile: JdbcProfile = dbConfig.driver
  override implicit val db: JdbcProfile#Backend#Database = dbConfig.db

  override val suppliersDal = new TableOperationsAndActions[SuppliersTable,Supplier](TableQuery[SuppliersTable]) {}

  val self = this

}

