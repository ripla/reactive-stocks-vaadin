package actors

import scala.concurrent.ExecutionContext.Implicits.global
import akka.actor.Actor
import scala.util.parsing.json.JSON
import scala.util.parsing.json.JSONType
import play.api.libs.ws.WS
import play.api.libs.json.JsValue
import play.api.libs.ws.Response
import play.mvc.Action
import utils.Global
import play.mvc.BodyParser.AnyContent
import scala.concurrent.Future
import akka.event.LoggingReceive

case class GetSentiment(symbol: String)
abstract class Sentiment
case object PositiveSentiment extends Sentiment
case object NeutralSentiment extends Sentiment
case object NegativeSentiment extends Sentiment

class StockSentimentActor extends Actor {

  def receive = LoggingReceive {
    case GetSentiment(symbol) => {
      val stored = sender
      for {
        tweets <- WS.url(Global.tweetUrl.format(symbol)).get // get tweets that contain the stock symbol
        futureSentiments = loadSentimentFromTweet(tweets.json) // queue web requests to get the sentiments of each tweet
        sentiments <- Future.sequence(futureSentiments) // when the sentiment responses arrive, set them
      } yield {
        val neg = getAverageSentiment(sentiments, "neg")
        val neutral = getAverageSentiment(sentiments, "neutral")
        val pos = getAverageSentiment(sentiments, "pos")

        if (neutral > 0.5)
          stored ! NeutralSentiment
        else if (neg > pos)
          stored ! NegativeSentiment
        else
          stored ! PositiveSentiment
      }
    }
  }

  def getTextSentiment(text: String): Future[Response] = WS.url(Global.sentimentUrl) post Map("text" -> Seq(text))

  def getAverageSentiment(responses: Seq[Response], label: String): Double = responses.map { response =>
    (response.json \\ label).head.as[Double]
  }.sum / responses.length.max(1) // avoid division by zero

  def loadSentimentFromTweet(json: JsValue): Seq[Future[Response]] = (json \\ "text") map (_.as[String]) map getTextSentiment

}