package com.docfin.persistence

import com.docfin.model.{BaseEntity, BaseTable}
import com.docfin.modules.{DbModule, Profile}
import slick.driver.H2Driver.api._
import slick.driver.JdbcProfile
import slick.lifted.CanBeQueryCondition

import scala.concurrent.Future

/**
  * Created by amit on 7/24/16.
  */


class TableOperationsAndActions[T <: BaseTable[A], A <: BaseEntity](tableQ: TableQuery[T])(implicit val db: JdbcProfile#Backend#Database, implicit val profile: JdbcProfile) extends Profile with DbModule {

  import profile.api._

  def insert(row: A): Future[Long] = {
    db.run(insertAction(row))
  }

  def insert(rows: Seq[A]): Future[Seq[Long]] = {
    db.run(insertAction(rows))
  }

  def insertAction(row: A) = {
    tableQ returning tableQ.map(_.id) += row
  }


  def insertAction(rows: Seq[A]) = {
    tableQ returning tableQ.map(_.id) ++= rows
  }

  def update(row: A): Future[Int] = {
    db.run(updateAction(row))
  }

  def update(rows: Seq[A]): Future[Unit] = {
    db.run(updateAction(rows))
  }

  def updateAction(row: A) = {
    tableQ.filter(_.id === row.id).update(row)
  }
  def updateAction(rows: Seq[A]) = {
    DBIO.seq((rows.map(r => tableQ.filter(_.id === r.id).update(r))): _*)
  }

  def findById(id: Long): Future[Option[A]] = {
    db.run(findByIdAction(id))
  }

  def findByIdAction(id: Long) = {
    tableQ.filter(_.id === id).result.headOption
  }

  def findByFilter[C: CanBeQueryCondition](f: (T) => C): Future[Seq[A]] = {
    db.run(findByFilterAction(f))
  }

  def findByFilterAction[C: CanBeQueryCondition](f: (T) => C)= {
    tableQ.withFilter(f).result
  }

  def deleteById(id: Long): Future[Int] = {
    deleteById(Seq(id))
  }

  def deleteById(ids: Seq[Long]): Future[Int] = {
    db.run(deleteByIdAction(ids))
  }

  def deleteByIdAction(id: Long)  = {
    tableQ.filter(_.id === id).delete
  }

  def deleteByIdAction(ids: Seq[Long])  = {
    tableQ.filter(_.id.inSet(ids)).delete
  }

  def deleteByFilter[C : CanBeQueryCondition](f:  (T) => C): Future[Int] = {
    db.run(deleteByFilterAction(f))
  }

  def deleteByFilterAction[C : CanBeQueryCondition](f:  (T) => C) = {
    tableQ.withFilter(f).delete
  }

  def createTable() : Future[Unit] = {
    db.run(createTableAction())
  }

  def createTableAction() = {
    DBIO.seq(schema.create)
  }

  def tableQuery : TableQuery[T] = {
    tableQ
  }

  def schema = {
    tableQuery.schema
  }


}
