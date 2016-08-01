package com.docfin.modules

/**
  * Created by amit on 7/24/16.
  */
import akka.actor.ActorSystem


trait ActorModule {
  val system: ActorSystem
}


trait ActorModuleImpl extends ActorModule {
  this: Configuration =>
  val system = ActorSystem("docfinActorSystem", config)
}
