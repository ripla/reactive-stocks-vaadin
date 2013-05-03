import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

  val appName         = "reactive-stocks"
  val appVersion      = "1.0-SNAPSHOT"
  val appScalaVersion = "2.10.1"
    
  val appDependencies = Seq(
    "com.typesafe.akka" %% "akka-actor" % "2.1.2",
    "com.typesafe.akka" %% "akka-slf4j" % "2.1.2",
    "com.vaadin" % "vaadin-server" % "7.1-SNAPSHOT",
    "com.vaadin" % "vaadin-push" % "7.1-SNAPSHOT",
    "vaadin.scala" %% "scaladin" % "3.0.0-SNAPSHOT",
    "javax.servlet" % "servlet-api" % "2.4",
    "org.eclipse.jetty" % "jetty-webapp" % "9.0.1.v20130408",
    "com.vaadin.addon" % "vaadin-charts" % "1.0.1",
    "com.typesafe" % "config" % "1.0.0"
  )
  
  val repositories = Seq("Vaadin Snapshots" at "https://oss.sonatype.org/content/repositories/vaadin-snapshots/",
  						 "Vaadin add-ons" at "http://maven.vaadin.com/vaadin-addons")

  val appSettings = Seq(libraryDependencies := appDependencies,
      					version := appVersion, 
      					scalaVersion := appScalaVersion,
      					resolvers := repositories)
  
  lazy val app = Project(id = "reactive-stocks",
                            base = file("."),
                            settings = Project.defaultSettings ++ appSettings)
}
