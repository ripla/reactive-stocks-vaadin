package actors;

import akka.actor.ActorRef
import akka.actor.Props
import akka.actor.UntypedActor
import akka.japi.Creator
import akka.actor.Actor
import akka.actor.TypedActor
import akka.actor.TypedProps
import ui.StockPresenterImpl
import ui.StockPresenter
import ui.Listen
import ui.WatchStock
import ui.UnwatchStock

class UsersActor extends Actor {

  def receive = {
    case message: StockUpdate => context.children.foreach(child => child ! message)

    case message: Listen => sender ! TypedActor(context).typedActorOf(TypedProps(classOf[StockPresenter], new StockPresenterImpl(message.uuid)), message.uuid) 

    case message: WatchStock => for (child <- context.child(message.uuid)) yield child ! message

    case message: UnwatchStock => for (child <- context.child(message.uuid)) yield child ! message
  }
}
