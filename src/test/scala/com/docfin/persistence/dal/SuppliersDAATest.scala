package com.docfin.persistence.dal

import akka.util.Timeout
import com.docfin.model.Supplier
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{BeforeAndAfterAll, FunSuite}

import scala.concurrent.Await
import scala.concurrent.duration._


@RunWith(classOf[JUnitRunner])
class SuppliersDalTest extends FunSuite with AbstractPersistenceTest with BeforeAndAfterAll{
  implicit val timeout = Timeout(5.seconds)

  val modules = new Modules {
  }

  test("SuppliersActor: Testing Suppliers Actor") {
    Await.result(modules.suppliersDal.createTable(),5.seconds)
    val numberOfEntities : Long = Await.result((modules.suppliersDal.insert(Supplier(None,"sup","desc"))),5.seconds)
    assert (numberOfEntities == 1)
    val supplier : Option[Supplier] = Await.result((modules.suppliersDal.findById(1)),5.seconds)
    assert (! supplier.isEmpty &&  supplier.get.name.compareTo("sup") == 0)
    val empty : Option[Supplier] = Await.result((modules.suppliersDal.findById(2)),5.seconds)
    assert (empty.isEmpty)
  }

  override def afterAll: Unit ={
    modules.db.close()
  }
}