package ui

import scala.concurrent.duration._

import utils.Global
import vaadin.scala._
import vaadin.scala.server.ScaladinServlet

class StockServlet extends ScaladinServlet {
  
  override def destroy = {
    Global.system.shutdown()
    Global.system.awaitTermination(5 seconds)
 }
}