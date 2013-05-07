import sbt._
import Keys._
import com.github.siasia.WebPlugin._

object ApplicationBuild extends Build {

  val appName           = "reactive-stocks"
  val appVersion        = "1.0-SNAPSHOT"
  val appScalaVersion   = "2.10.1"
  val appVaadinVersion  = "7.1.0.beta1"

  val appDependencies = Seq(
    "com.typesafe.akka" %% "akka-actor" % "2.1.2",
    "com.typesafe.akka" %% "akka-slf4j" % "2.1.2",
    "com.vaadin" % "vaadin-server" % appVaadinVersion,
    "com.vaadin" % "vaadin-push" % appVaadinVersion,
    "com.vaadin" % "vaadin-client-compiled" % appVaadinVersion,
    "com.vaadin" % "vaadin-themes" % appVaadinVersion,
    "vaadin.scala" %% "scaladin" % "3.0.0-SNAPSHOT",
    "javax.servlet" % "javax.servlet-api" % "3.0.1",
    "org.eclipse.jetty" % "jetty-webapp" % "8.1.10.v20130312" % "container",
    "org.eclipse.jetty.orbit" % "javax.servlet" % "3.0.0.v201112011016" % "container",
    "org.eclipse.jetty" % "jetty-continuation" % "8.1.10.v20130312",
    "org.eclipse.jetty" % "jetty-websocket" % "8.1.10.v20130312",
    //"org.eclipse.jetty", "jetty-annotations", % "8.1.10.v20130312" % "container"
    "com.vaadin.addon" % "vaadin-charts" % "1.0.1",
    "com.typesafe" % "config" % "1.0.0"
  )
  
  val repositories = Seq("Vaadin Snapshots" at "https://oss.sonatype.org/content/repositories/vaadin-snapshots/",
  						 "Vaadin add-ons" at "http://maven.vaadin.com/vaadin-addons")

  val appSettings = Seq(libraryDependencies := appDependencies,
      					version := appVersion, 
      					scalaVersion := appScalaVersion,
      					resolvers := repositories) ++ webSettings
  
  lazy val app = Project(id = "reactive-stocks",
                            base = file("."),
                            settings = Project.defaultSettings ++ appSettings)
}
