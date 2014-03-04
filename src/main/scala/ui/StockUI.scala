package ui

import vaadin.scala._
import akka.pattern.Patterns.ask
import utils.Global
import java.util.UUID
import akka.actor.ActorRef
import scala.concurrent.Future
import akka.actor.TypedActor
import akka.actor.PoisonPill
import vaadin.scala.event.DetachEvent

object StockUI {
  private var counter = 0
  def uiInit() = {
    counter = counter + 1
    Console.println(s"$counter UIs inited")
  }
}
class StockUI extends UI(pushMode = PushMode.Automatic) {

  StockUI.uiInit()

  val uuid = UUID.randomUUID().toString()

  val mainLayout = new VerticalLayout { margin = true }
  mainLayout.components += Label("Loading...")
  content = mainLayout

  import Global.system.dispatcher
  import scala.collection.JavaConversions._

  val userActor: Future[StockPresenter] = ask(Global.usersActor, Listen(uuid), 10000).mapTo[StockPresenter] map { presenter: StockPresenter =>

    access {
      mainLayout.removeAllComponents()
      mainLayout.components += presenter.view
      // watch the default stocks, only after the view has been initialized
      val defaultStocks = Global.configuration.getStringList("default.stocks")
      defaultStocks foreach (symbol => presenter.watch(symbol))
    }

    presenter
  }

  detachListeners += { (e: DetachEvent) =>
    System.out.println(s"Poisoning UserActor $uuid")
    userActor onSuccess {
      case presenter: StockPresenter => TypedActor.get(Global.system).getActorRefFor(presenter).tell(PoisonPill, null)
    }
  }

  override def delayedInit(body: => Unit) {
    Console.println("Delayed init")
    super.delayedInit(body)
  }
}