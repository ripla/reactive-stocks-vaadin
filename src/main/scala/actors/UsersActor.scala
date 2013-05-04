package actors;

import akka.actor.ActorRef
import akka.actor.Props
import akka.actor.UntypedActor
import akka.japi.Creator;
import akka.actor.Actor

class UsersActor extends Actor {

  def receive = {
    case message: StockUpdate => context.children.foreach(child => child ! message)

    case message: Listen => sender ! context.actorOf(Props[UserActor], message.uuid)

    case message: WatchStock => for (child <- context.child(message.uuid)) yield child ! message

    case message: UnwatchStock => for (child <- context.child(message.uuid)) yield child ! message
  }
}
