package ui

import akka.actor.TypedActor
import actors.StockUpdate
import akka.actor.ActorRef
import utils.Global
import akka.util.Timeout
import akka.actor.{ Props, ActorRef, Actor }
import scala.collection.mutable
import akka.pattern.ask
import akka.util.Timeout
import scala.collection.JavaConverters._
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global
import utils.Global
import actors.SetupStock
import actors.FetchHistory
import scala.concurrent.Future
import actors.GetSentiment
import scala.util.Success
import scala.util.Failure
import akka.event.LoggingReceive
import akka.event.Logging
import scala.collection.mutable.ArrayBuffer
import actors.Sentiment

class StockPresenterImpl(uuid: String) extends StockPresenter with TypedActor.Receiver {

  val stocks: ArrayBuffer[String] = ArrayBuffer.empty

  override val view: StockView = new StockView(TypedActor.self[StockPresenter])

  val log = Logging(TypedActor.context.system, TypedActor.context.self)

  override def onReceive(message: Any, sender: ActorRef): Unit = message match {

    case StockUpdate(symbol, price) =>
      if (stocks.contains(symbol)) {
        view.updateStock(symbol, price.doubleValue)
      } else {
        log.debug(s"Received update for unknown symbol: $symbol")
      }
  }

  def watch(symbol: String): Unit = {
    log.debug(s"Received WatchStock with symbol: $symbol")
    stocks += symbol

    implicit val timeout = Timeout(15 seconds)
    (Global.stockHolderActor ? SetupStock(symbol)).mapTo[ActorRef].foreach { stockActorRef =>

      // send the whole history to the client
      (stockActorRef ? FetchHistory).mapTo[Seq[Number]].foreach { history =>
        val startTime = System.currentTimeMillis() - (history.size * Global.updateFrequency)
        val values = history.map(_.doubleValue).zipWithIndex map { case (value, index) => (index * Global.updateFrequency + startTime, value) }

        log.debug(s"Received WatchStock with symbol: $symbol")
        view.watchStock(symbol, values)
      }
    }
  }

  def unwatch(symbol: String): Unit = {
    log.debug(s"Received UnwatchStock with symbol: $symbol")
    if (stocks.contains(symbol))
      stocks -= symbol
  }

  def chartClicked(symbol: String): Unit = {
    view.showFetchingSentiment(symbol)
    getSentiment(symbol) onComplete (result => result match {
      case Success(result) => view.showStockSentiment(symbol, result)
      case Failure(exception) => exception.printStackTrace()
    })
  }

  def getSentiment(symbol: String): Future[Sentiment] = {
    log.debug(s"Received GetSentiment with symbol: $symbol")
    implicit val actorTimeout = Timeout(10 seconds)
    (Global.sentimentActor ? GetSentiment(symbol)).mapTo[Sentiment]
  }

}