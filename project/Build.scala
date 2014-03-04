import sbt._
import Keys._
import org.vaadin.sbt.VaadinPlugin._
//import com.earldouglas.xsbtwebplugin.WebPlugin._
object ApplicationBuild extends Build {

  val appName           = "reactive-stocks"
  val appVersion        = "1.0-SNAPSHOT"
  val appScalaVersion   = "2.10.2"
  val appVaadinVersion  = "7.1.7"

  val appDependencies = Seq(
    "com.typesafe.akka" %% "akka-actor" % "2.2.1",
    "com.typesafe.akka" %% "akka-slf4j" % "2.2.1",
    "com.vaadin" % "vaadin-server" % appVaadinVersion,
    "com.vaadin" % "vaadin-push" % appVaadinVersion,
    "com.vaadin" % "vaadin-themes" % appVaadinVersion,
    "com.vaadin" % "vaadin-client-compiler" % appVaadinVersion % "provided",
    "vaadin.scala" %% "scaladin" % "3.0-SNAPSHOT" changing(),
    "javax.servlet" % "javax.servlet-api" % "3.0.1" % "provided",
    "org.eclipse.jetty" % "jetty-webapp" % "8.1.10.v20130312" % "container",
    "org.eclipse.jetty.orbit" % "javax.servlet" % "3.0.0.v201112011016" % "container",
    "org.eclipse.jetty" % "jetty-continuation" % "8.1.10.v20130312" % "container",
    "org.eclipse.jetty" % "jetty-websocket" % "8.1.10.v20130312" % "container",
    //"org.eclipse.jetty", "jetty-annotations", % "8.1.10.v20130312" % "container"
    "com.vaadin.addon" % "vaadin-charts" % "1.1.2",
    "play" %% "play" % "2.1.5"
  )
  
  val repositories = Seq("Vaadin Snapshots" at "https://oss.sonatype.org/content/repositories/comvaadin-1030",
                         "Vaadin add-ons" at "http://maven.vaadin.com/vaadin-addons",
                         "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/")

  val appSettings = 
  vaadinWebSettings ++ 
  //webSettings ++
  Seq(libraryDependencies := appDependencies,
      					version := appVersion, 
      					scalaVersion := appScalaVersion,
      					resolvers := repositories,
                javaOptions in compileVaadinWidgetsets := Seq("-Xss8M", "-Xmx512M", "-XX:MaxPermSize=512M"),
                vaadinWidgetsets := Seq("ui.Widgetset"),
                vaadinOptions in compileVaadinWidgetsets := Seq("-strict", "-draftCompile", "-localWorkers", "2" )

                //target in compileWidgetsets := (sourceDirectory in Compile).value / "webapp" / "VAADIN" / "widgetsets"
)
  lazy val app = Project(id = "reactive-stocks",
                            base = file("."),
                            settings = Project.defaultSettings ++ appSettings)
                  }
