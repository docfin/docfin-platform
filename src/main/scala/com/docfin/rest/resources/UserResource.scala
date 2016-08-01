package com.docfin.rest.resources

import akka.actor.{Actor, ActorRefFactory}
import com.docfin.modules.{Configuration, DbModule, PersistenceModule, ServicesModule}
import com.docfin.model._
import io.swagger.annotations._
import spray.http.StatusCodes

import scala.concurrent.Future
import scala.util.{Failure, Success}
import scala.concurrent.ExecutionContext.Implicits.global
/**
  * Created by amit on 7/26/16.
  */
@Api(value = "/users")
abstract class UserResource(modules: ServicesModule) extends  AbstractResource(modules) {

  import spray.httpx.SprayJsonSupport._
  import com.docfin.rest.JsonProtocol._

  @ApiOperation(value = "Creates User",  httpMethod = "POST", consumes = "application/json", produces = "application/json")
  @ApiImplicitParams(Array(
    new ApiImplicitParam(name = "body", value = "User representation", dataType = "com.docfin.model.UserReceived", required = true, paramType = "body")
  ))
  @ApiResponses(Array(
    new ApiResponse(code = 400, message = "Bad Request"),
    new ApiResponse(code = 201, message = "Entity Created")
  ))
  def CREATE = defaultResponseType {
    path("users"){
      post {
        entity(as[UserReceived]){ userReceived =>
          val userFuture: Future[Option[User]]  = modules.userService.create(userReceived)
          onComplete(userFuture){
            case Success(userCreatedMayBe) =>  complete(StatusCodes.Created)
            case Failure(ex) =>   complete(StatusCodes.InternalServerError, s"An error occurred: ${ex.getMessage}")
          }
        }
      }
    }
  }


  def READ = defaultResponseType {
    path("users" / LongNumber ){ (userId) =>
      get {
        val userFuture: Future[Option[User]]  = modules.userService.findOne(userId)
        onComplete(userFuture){
          case Success(userOpt) => userOpt match {
            case Some(user) => complete(user)
            case None => complete(StatusCodes.NotFound,s"The user with id $userId could not be found")
          }
          case Failure(ex) => complete(StatusCodes.InternalServerError, s"An error occurred: ${ex.getMessage}")
        }
      }
    }
   }

}
