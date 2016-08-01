package com.docfin.rest

/**
  * Created by amit on 7/24/16.
  */
import akka.actor.Actor
import com.docfin.model._
import com.typesafe.scalalogging.{LazyLogging, Logger}
import spray.httpx.SprayJsonSupport
import spray.routing._
import spray.http._
import MediaTypes._
import com.docfin.modules.PersistenceModule

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}
import spray.http.StatusCodes._
import akka.util.Timeout
import com.docfin.modules._
import com.docfin.rest.resources.UserResource
import com.github.swagger.spray.SwaggerHttpService

import scala.concurrent.duration._
import scala.reflect.runtime.universe._
import io.swagger.annotations._

class RoutesActor(modules: ServicesModule) extends Actor with HttpService with LazyLogging {

  def actorRefFactory = context

  implicit val timeout = Timeout(5.seconds)





  /*val schema = modules.addressDal.schema.createStatements ++ modules.personInfoDal.schema.createStatements ++ modules.userDal.schema.createStatements

  val schemaAsString = schema.mkString("\n")

  logger.info(schemaAsString)*/



  val swaggerService = new SwaggerHttpService {
    implicit def actorRefFactory = context
    override val apiTypes = Seq(typeOf[UserResource])
    override val host = "localhost:8080" //the url of your api, not swagger's json endpoint
    override val basePath = "/"    //the basePath for the API you are exposing
    override val apiDocsPath = "api-docs" //where you want the swagger-json endpoint exposed

  }.routes

  val userResource = new UserResource(modules){
    def actorRefFactory = context
  }




  def receive = runRoute (
    userResource.CREATE ~ userResource.READ ~ swaggerService ~
    get {
      pathPrefix("") { pathEndOrSingleSlash {
        getFromResource("swagger-ui/index.html")
      }
      } ~
        getFromResourceDirectory("swagger-ui")
    }
  )

}



/*
@Api(value = "/supplier", description = "Operations about suppliers")
abstract class SupplierHttpService(modules: Configuration with PersistenceModule) extends HttpService {

  import SprayJsonSupport._
  import com.docfin.rest.JsonProtocol._

  implicit val timeout = Timeout(5.seconds)

  @ApiOperation(httpMethod = "GET", response = classOf[Supplier], value = "Returns a supplier based on ID")
  @ApiImplicitParams(Array(
    new ApiImplicitParam(name = "supplierId", required = true, dataType = "integer", paramType = "path", value = "ID of supplier that needs to be fetched")
  ))
  @ApiResponses(Array(
    new ApiResponse(code = 200, message = "Ok")))
  def SupplierGetRoute = path("supplier" / IntNumber) { (supId)      =>
    get {
      respondWithMediaType(`application/json`) {
        onComplete((modules.suppliersDal.findById(supId)).mapTo[Option[Supplier]]) {
          case Success(supplierOpt) => supplierOpt match {
            case Some(sup) => complete(sup)
            case None => complete(NotFound,s"The supplier doesn't exist")
          }
          case Failure(ex) => complete(InternalServerError, s"An error occurred: ${ex.getMessage}")
        }
      }
    }
  }

  @ApiOperation(value = "Add Supplier", nickname = "addSuplier", httpMethod = "POST", consumes = "application/json", produces = "text/plain; charset=UTF-8")
  @ApiImplicitParams(Array(
    new ApiImplicitParam(name = "body", value = "Supplier Object", dataType = "persistence.entities.SimpleSupplier", required = true, paramType = "body")
  ))
  @ApiResponses(Array(
    new ApiResponse(code = 400, message = "Bad Request"),
    new ApiResponse(code = 201, message = "Entity Created")
  ))
  def SupplierPostRoute = path("supplier"){
    post {
      entity(as[SimpleSupplier]){ supplierToInsert =>  onComplete((modules.suppliersDal.insert(Supplier(None,supplierToInsert.name,supplierToInsert.desc)))) {
        // ignoring the number of insertedEntities because in this case it should always be one, you might check this in other cases
        case Success(insertedEntities) => complete(StatusCodes.Created)
        case Failure(ex) => complete(InternalServerError, s"An error occurred: ${ex.getMessage}")
      }
      }
    }
  }
}*/
