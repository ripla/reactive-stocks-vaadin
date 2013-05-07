package ui

import vaadin.scala._

class StockView extends VerticalLayout {

  var symbolCharts: Map[String, Field[String]] = Map.empty

  //TODO use Charts components
  //TODO call StockPresenter.getSentiment for clicks
  
  def updateStock(symbol: String, price: Double): Unit = {
    ui.access(() => {
      symbolCharts(symbol).value = price.toString
    })

  }

  def watchStock(symbol: String, history: Seq[Double]) = {
    ui.access(() => {
      val stockComponent: Field[String] = add(new TextField { caption = symbol })
      symbolCharts += (symbol -> stockComponent)
    })
  }
}