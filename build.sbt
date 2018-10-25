name := """Vilicus"""
organization := "com.example"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.12.6"

libraryDependencies += guice
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "3.1.2" % Test

libraryDependencies += "io.monix" %% "monix" % "3.0.0-RC1"
libraryDependencies += jdbc % Test


// Adds additional packages into Twirl
//TwirlKeys.templateImports += "com.example.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "com.example.binders._"

libraryDependencies += "org.tpolecat" %% "doobie-postgres" % "0.5.3"


scalacOptions += "-Ypartial-unification" // 2.11.9+

libraryDependencies ++= Seq(

  // Start with this one
  "org.tpolecat" %% "doobie-core"      % "0.5.3",

  // And add any of these as needed
//  "org.tpolecat" %% "doobie-h2"        % "0.5.3", // H2 driver 1.4.197 + type mappings.
  "org.tpolecat" %% "doobie-hikari"    % "0.5.3", // HikariCP transactor.
  "org.tpolecat" %% "doobie-postgres"  % "0.5.3", // Postgres driver 42.2.2 + type mappings.
  "org.tpolecat" %% "doobie-specs2"    % "0.5.3", // Specs2 support for typechecking statements.
  "org.tpolecat" %% "doobie-scalatest" % "0.5.3"  // ScalaTest support for typechecking statements.

)


val circeVersion = "0.9.0"

libraryDependencies ++= Seq(
  "io.circe" %% "circe-core",
  "io.circe" %% "circe-generic",
  "io.circe" %% "circe-parser"
).map(_ % circeVersion)
