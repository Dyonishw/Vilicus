name := """Vilicus"""
organization := "com.example"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.12.6"

libraryDependencies ++= Seq(
  openId
)

libraryDependencies ++= Seq(evolutions, jdbc)
libraryDependencies += jdbc % Test
libraryDependencies += guice
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "3.1.2" % Test

libraryDependencies += "io.monix" %% "monix" % "3.0.0-RC1"

scalacOptions += "-Ypartial-unification" // 2.11.9+

libraryDependencies ++= Seq(
  "org.tpolecat" %% "doobie-core"      % "0.5.3",
//  "org.tpolecat" %% "doobie-h2"        % "0.5.3", // H2 driver 1.4.197 + type mappings.
  "org.tpolecat" %% "doobie-hikari"    % "0.5.3", // HikariCP transactor.
  "org.tpolecat" %% "doobie-postgres"  % "0.5.3", // Postgres driver 42.2.2 + type mappings.
//  "org.tpolecat" %% "doobie-specs2"    % "0.5.3", // Specs2 support for typechecking statements.
  "org.tpolecat" %% "doobie-scalatest" % "0.5.3"  // ScalaTest support for typechecking statements.
)

val circeVersion = "0.10.0"

libraryDependencies ++= Seq(
  "io.circe" %% "circe-core",
  "io.circe" %% "circe-generic",
  "io.circe" %% "circe-parser"
).map(_ % circeVersion)

libraryDependencies ++= {
  Seq(
//    "org.apache.kafka" %% "kafka" % "2.1.0",
    "org.apache.kafka" % "kafka-clients" % "2.1.0",
    "org.apache.kafka" % "kafka-streams" % "2.1.0"
  )
}

libraryDependencies += "com.outr" %% "scribe" % "2.6.0"
