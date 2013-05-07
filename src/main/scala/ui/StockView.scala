package ui

import vaadin.scala._

class StockView(presenter: StockPresenter) extends VerticalLayout {

  var symbolCharts: Map[String, Field[String]] = Map.empty

  //TODO use Charts components
  //TODO call StockPresenter.getSentiment for clicks

  val addField: TextField = add(new TextField)
  val addButton: Button = add(new Button() {
    caption = "Watch stock"
    clickListeners += {
      val symbol = addField.value.get.asInstanceOf[String]
      if (!symbolCharts.contains(symbol))
    	  addField.value.foreach(value => presenter.watch(symbol))
    }
  })

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