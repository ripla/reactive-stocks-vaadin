package actors;

import akka.actor.Actor
import akka.actor.ActorRef
import akka.actor.TypedActor
import akka.actor.TypedProps
import akka.actor.UntypedActor
import akka.event.Logging
import akka.event.LoggingReceive
import akka.japi.Creator
import ui.Listen
import ui.StockPresenter
import ui.StockPresenterImpl

class UsersActor extends Actor {

  val log = Logging(context.system, this)

  def receive = LoggingReceive {
    case message: StockUpdate => {
      log.debug(s"Dispatching message $message to ${context.children.size} children")
      context.children.foreach(child => child ! message)
    }

    case message: Listen => sender ! TypedActor(context).typedActorOf(TypedProps(classOf[StockPresenter], new StockPresenterImpl(message.uuid)), message.uuid) 
  }
}
