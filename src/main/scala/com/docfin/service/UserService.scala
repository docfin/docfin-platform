package com.docfin.service

import com.docfin.modules.{DbModule, PersistenceModule}
import com.docfin.persistence.TableOperationsAndActions
import com.docfin.model._
import com.typesafe.scalalogging.LazyLogging

import scala.concurrent.Future
import scala.util.{Failure, Success}
import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Created by amit on 7/31/16.
  */
class UserService(modules: PersistenceModule with DbModule ) extends LazyLogging {


  def findOne(id: Long): Future[Option[User]] = {
    logger.info(s"Findinf user with id $id")
    modules.db.run(modules.userDal.findByIdAction(id))
  }


  def create(userReceived: UserReceived) : Future[Option[User]] = {
    logger.info(s"Creating User from $userReceived")
    val createUserAction = for {
      personId <- modules.personInfoDal.insertAction(PersonInfo(None, userReceived.firstName, userReceived.lastName, userReceived.email, None))
      userId <- modules.userDal.insertAction(User(None, userReceived.email, personId, None))
      user <- modules.userDal.findByIdAction(userId)
    } yield (user)
    modules.db.run(createUserAction)
  }
}
