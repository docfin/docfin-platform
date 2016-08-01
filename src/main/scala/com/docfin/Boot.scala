package com.docfin

import akka.actor.Props
import akka.io.IO
import akka.pattern.ask
import akka.util.Timeout
import com.docfin.modules._
import rest.RoutesActor
import spray.can.Http
import com.docfin.modules.{ActorModuleImpl, PersistenceModuleImpl}
import com.docfin.service.UserService

import scala.concurrent.duration._

/**
  * Created by amit on 7/24/16.
  */
object Boot extends App{
  // configuring modules for application, cake pattern for DI
  val modules = new ConfigurationModuleImpl  with ActorModuleImpl with DbModule with PersistenceModuleImpl  with ServicesModuleImpl


  // create and start our service actor
  val service = modules.system.actorOf(Props(classOf[RoutesActor], modules), "routesActor")

  implicit val system = modules.system
  implicit val timeout = Timeout(5.seconds)

  // start a new HTTP server on port 8080 with our service actor as the handler
  IO(Http) ? Http.Bind(service, interface = "localhost", port = 8080)


}
