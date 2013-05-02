import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

  val appName         = "reactive-stocks"
  val appVersion      = "1.0-SNAPSHOT"

  val appDependencies = Seq(
    "com.typesafe.akka" %% "akka-actor" % "2.1.2",
    "com.typesafe.akka" %% "akka-slf4j" % "2.1.2",
    "org.webjars" % "webjars-play" % "2.1.0-1",
    "org.webjars" % "bootstrap" % "2.1.1",
    "org.webjars" % "flot" % "0.8.0"
  )


  val main = play.Project(appName, appVersion, appDependencies).settings(
    scalaVersion := "2.10.1"
  )

}
