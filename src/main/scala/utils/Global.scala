package utils

import com.typesafe.config.Config
import com.typesafe.config.ConfigFactory
import akka.actor.ActorSystem
import akka.actor.Props
import scala.concurrent.duration._

import actors._

object Global {
  
  val configuration = ConfigFactory.load();
  val sentimentUrl = configuration.getString("sentiment.url")
  val tweetUrl = configuration.getString("tweet.url")
  val updateFrequency = configuration.getLong("updatefrequency.ms")

  val system = ActorSystem("reactive-stocks")
  val usersActor = system.actorOf(Props[UsersActor], "users")
  val stockHolderActor = system.actorOf(Props[StockHolderActor], "stocks")
  val sentimentActor = system.actorOf(Props[StockSentimentActor], "sentiments")

  import system.dispatcher

  // fetch a new data point at a given interval
  system.scheduler.schedule(Duration.Zero, updateFrequency millis, stockHolderActor, FetchLatest.instance);
}