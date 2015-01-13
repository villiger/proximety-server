name := "proximety-reactive"

version := "0.0.1-SNAPSHOT"

scalaVersion := "2.11.4"

scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "utf8")

libraryDependencies ++= {
  val akkaVersion = "2.3.8"
  val sprayVersion = "1.3.2"
  Seq(
    "io.spray"                %%  "spray-can"     % sprayVersion,
    "io.spray"                %%  "spray-routing" % sprayVersion,
    "io.spray"                %%  "spray-testkit" % sprayVersion  % "test",
    "org.json4s"              %%  "json4s-native" % "3.2.10",
    "com.typesafe.akka"       %%  "akka-actor"    % akkaVersion,
    "com.typesafe.akka"       %%  "akka-testkit"  % akkaVersion   % "test",
    "com.typesafe.akka"       %%  "akka-persistence-experimental" % akkaVersion,
    "org.specs2"              %%  "specs2-core"   % "2.3.11" % "test",
    "com.github.nscala-time"  %%  "nscala-time"   % "1.6.0"
  )
}

Revolver.settings
