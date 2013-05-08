package utils

import com.typesafe.config.Config
import com.typesafe.config.ConfigFactory
import akka.actor.ActorSystem
import akka.actor.Props
import actors.UsersActor
import actors.StockHolderActor
import scala.concurrent.duration._
import scala.concurrent.duration.Duration._
import actors.FetchLatest

object Global {
  
  val configuration = ConfigFactory.load();
  val sentimentUrl = configuration.getString("sentiment.url")
  val tweetUrl = configuration.getString("tweet.url")
  val updateFrequency = configuration.getLong("updatefrequency.ms")

  val system = ActorSystem("reactive-stocks")
  val usersActor = system.actorOf(Props[UsersActor], "users")
  val stockHolderActor = system.actorOf(Props[StockHolderActor], "stocks")

  import system.dispatcher

  // fetch a new data point once every second
  system.scheduler.schedule(Zero, updateFrequency millis, stockHolderActor, FetchLatest.instance);
}