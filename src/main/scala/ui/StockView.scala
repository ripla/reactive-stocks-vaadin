package ui

import vaadin.scala._
import com.vaadin.addon.charts._
import com.vaadin.addon.charts.model._
import com.vaadin.addon.charts.model.style.SolidColor
import vaadin.scala.mixins.ComponentMixin
import actors.Sentiment

class StockView(presenter: StockPresenter) extends VerticalLayout {

  spacing = true

  var symbolCharts: Map[String, Chart with ComponentMixin] = Map.empty

  val addField: TextField = add(new TextField)
  val addButton: Button = add(new Button() {
    caption = "Watch stock"
    clickListeners += {
      val symbol = addField.value.get.asInstanceOf[String]
      if (!symbolCharts.contains(symbol))
        presenter.watch(symbol)
    }
  })

  setAlignment(addField, Alignment.MiddleCenter)
  setAlignment(addButton, Alignment.MiddleCenter)

  def updateStock(symbol: String, price: Double): Unit = ui.access {
    val time = System.currentTimeMillis()
    val series: DataSeries = symbolCharts(symbol).getConfiguration().getSeries.get(0).asInstanceOf[DataSeries]
    series.add(new DataSeriesItem(time, price), true, true)
  }

  def watchStock(symbol: String, history: Seq[(Long, Double)]): Unit = ui.access {
    //setup component
    val wrappedChart = ScaladinWrapper.wrapComponent(createChart(symbol))
    symbolCharts += (symbol -> wrappedChart.p)

    //setup initial history series
    val series: DataSeries = wrappedChart.p.getConfiguration().getSeries.get(0).asInstanceOf[DataSeries]
    history.foreach { case (time, value) => series.add(new DataSeriesItem(time, value), true, false) }

    add(wrappedChart)
    setAlignment(wrappedChart, Alignment.MiddleCenter)
  }

  def showStockSentiment(symbol: String, result: Sentiment): Unit = ui.access {
    val sentimentLabel = Label(result.toString())
    replaceComponent(symbolCharts(symbol).wrapperComponent, sentimentLabel)
  }

  def showFetchingSentiment(symbol: String): Unit = {

  }

  def createChart(symbol: String): Chart with ComponentMixin = {
    //Vaadin charts is a Java class, so it has a Java interface unlike Scaladin
    val chart = new Chart() with ComponentMixin
    chart.setWidth("500px")

    chart.addChartClickListener(new ChartClickListener() {
      def onClick(event: ChartClickEvent) {
        presenter.chartClicked(symbol)
      }
    })

    val configuration: Configuration = new Configuration()
    configuration.getChart().setType(ChartType.LINE)
    configuration.getTitle().setText(symbol)

    val xAxis: Axis = configuration.getxAxis()
    xAxis.setType(AxisType.DATETIME)
    xAxis.setTickPixelInterval(150)

    val yAxis: YAxis = configuration.getyAxis()
    yAxis.setTitle(new Title("Value"))
    yAxis.setPlotLines(new PlotLine(0, 1, new SolidColor("#808080")))

    configuration.getTooltip().setEnabled(false)
    configuration.getLegend().setEnabled(false)

    val series: DataSeries = new DataSeries()
    series.setPlotOptions(new PlotOptionsSpline())
    series.setName("Stock prices")

    configuration.setSeries(series)

    chart.drawChart(configuration)

    chart
  }
}