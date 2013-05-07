package ui

import vaadin.scala._
import akka.pattern.Patterns.ask
import utils.Global
import java.util.UUID
import akka.actor.ActorRef
import scala.concurrent.Future
import akka.actor.TypedActor

class StockUI extends UI(pushMode = PushMode.Automatic) {

  val uuid = UUID.randomUUID().toString()

  val mainLayout = new VerticalLayout { margin = true }
  content = mainLayout

  import Global.system.dispatcher
  import scala.collection.JavaConversions._

  val userActor: Future[StockPresenter] = ask(Global.usersActor, Listen(uuid), 10000).mapTo[StockPresenter] map { userActor: StockPresenter =>
    val presenterActor = TypedActor.get(Global.system).getActorRefFor(userActor);

    access(() => mainLayout.addComponent(userActor.view))

    // watch the default stocks
    val defaultStocks = Global.configuration.getStringList("default.stocks")
    for (symbol <- defaultStocks) yield presenterActor ! WatchStock(uuid, symbol)

    userActor
  }

  //TODO poisonpill on detach
}