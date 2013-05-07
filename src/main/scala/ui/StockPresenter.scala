package ui

import scala.concurrent.Future

trait StockPresenter {

  val view: StockView

  def getSentiment(symbol: String): Future[SentimentResult]

  def watch(symbol: String)
}

case class Listen(uuid: String)

case class WatchStock(uuid: String, symbol: String)

case class UnwatchStock(uuid: String, symbol: String)

abstract class SentimentResult
case object Neutral extends SentimentResult
case object Positive extends SentimentResult
case object Negative extends SentimentResult