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
    "org.eclipse.jetty" % "jetty-webapp" % "9.0.1.v20130408"
  )

  val appSettings = Seq(libraryDependencies := appDependencies,
      					version := appVersion, 
      					scalaVersion := appScalaVersion,
      					resolvers := Seq("Vaadin Snapshots" at "https://oss.sonatype.org/content/repositories/vaadin-snapshots/"))
  
  lazy val app = Project(id = "reactive-stocks",
                            base = file("."),
                            settings = Project.defaultSettings ++ appSettings)
}
