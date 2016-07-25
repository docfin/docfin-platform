package com.docfin.persistence.entities

/**
  * Created by amit on 7/24/16.
  */
trait BaseEntity {
  val id : Option[Long]
  def isValid : Boolean = true
}
