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

class StockPresenterImpl(uuid: String) extends StockPresenter with TypedActor.Receiver {

  var stocks: Map[String, ActorRef] = Map.empty[String, ActorRef]

  override val view: StockView = new StockView

  override def onReceive(message: Any, sender: ActorRef): Unit = message match {

    case StockUpdate(symbol, price) =>
      if (stocks.contains(symbol)) {
        view.updateStock(symbol, price.doubleValue)
      }

    case WatchStock(uuid: String, symbol: String) =>
      implicit val timeout = Timeout(15 seconds)
      (Global.stockHolderActor ? SetupStock(symbol)).mapTo[ActorRef].foreach { stockActorRef =>
        stocks = stocks + (symbol -> stockActorRef)

        // send the whole history to the client
        (stockActorRef ? FetchHistory).mapTo[Seq[Number]].foreach { history =>

          view.watchStock(symbol, history map (_.doubleValue))
        }
      }

    case UnwatchStock(uuid: String, symbol: String) =>
      if (stocks.contains(symbol))
        stocks = stocks - symbol
  }

  def watch(symbol: String) {
    Global.usersActor ! WatchStock(uuid, symbol)
  }

  def unwatch(symbol: String) {
    Global.usersActor ! UnwatchStock(uuid, symbol)
  }

  //TODO migrate StockSentiment.scala from play
  def getSentiment(symbol: String): Future[SentimentResult] = Future.successful(Positive)

}