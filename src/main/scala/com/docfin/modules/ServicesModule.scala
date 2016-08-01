package com.docfin.modules

import com.docfin.service.UserService
import com.typesafe.scalalogging.LazyLogging

/**
  * Created by amit on 7/31/16.
  */
trait ServicesModule {
  val userService: UserService
}

trait ServicesModuleImpl extends ServicesModule { this: PersistenceModule with DbModule =>
  val userService = new UserService(this)
}