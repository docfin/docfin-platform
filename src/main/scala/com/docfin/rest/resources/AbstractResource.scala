package com.docfin.rest.resources

import akka.actor.{Actor, ActorRefFactory}
import akka.util.Timeout
import com.docfin.modules.{PersistenceModule, _}
import spray.http.{MediaType, MediaTypes}
import spray.routing._

import scala.concurrent.duration._
/**
  * Created by amit on 7/26/16.
  */

abstract class AbstractResource(modules: ServicesModule) extends HttpService {

  implicit val timeout = Timeout(5.seconds)

  def defaultResponseType(route: Route) = {
    respondWithMediaType(MediaTypes.`application/json`) {
      route
    }

  }


}
