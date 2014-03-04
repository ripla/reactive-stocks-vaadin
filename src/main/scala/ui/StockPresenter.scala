package ui

import scala.concurrent.Future
import actors.Sentiment

trait StockPresenter {

  val view: StockView

  def getSentiment(symbol: String): Future[Sentiment]

  def watch(symbol: String)
  
  def chartClicked(symbol: String)
}

case class Listen(uuid: String)
