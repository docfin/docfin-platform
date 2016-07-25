package com.docfin.persistence.entities

import slick.driver.H2Driver.api._
import slick.lifted.Tag

/**
  * Created by amit on 7/24/16.
  */
abstract class BaseTable[T](tag: Tag, name: String) extends Table[T](tag, name) {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
}
