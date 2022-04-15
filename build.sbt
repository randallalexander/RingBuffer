val catsVersion = "2.7.0"
val kittensVersion = "3.0.0-M3"
val mouseVersion = "1.0.10"
val munitVersion = "0.7.29"

ThisBuild / organization := "net.randallalexander"
ThisBuild / scalaVersion := "3.1.0"
ThisBuild / version := "0.1.1-snapshot"

lazy val buildSettings = Seq(
  Compile / console / scalacOptions ~= {
    _.filterNot(Set("-Ywarn-unused-import", "-Xfatal-warnings"))
  },
  Compile / console / scalacOptions ~= {
    _.filterNot(Set("-Ywarn-unused-import", "-Xfatal-warnings"))
  },
  testOptions += Tests.Argument(TestFrameworks.MUnit, "-b"),
  parallelExecution := false,
  libraryDependencies ++= Seq(
    "org.typelevel" %% "cats-core" % catsVersion,
    "org.typelevel" %% "kittens" % kittensVersion,
    "org.typelevel" %% "mouse" % mouseVersion,
    "org.scalameta" %% "munit" % munitVersion % Test
  )
)

lazy val ringbuffer = project
  .in(file("ringbuffer"))
  .settings(buildSettings)
  .settings(
    name := "collection-ringbuffer",
    semanticdbEnabled := true, // enable SemanticDB
    semanticdbVersion := scalafixSemanticdb.revision // only required for Scala 2.x
  )

lazy val benchmark = project
  .in(file("benchmark"))
  .dependsOn(ringbuffer)
  .settings(buildSettings)
  .enablePlugins(JmhPlugin)
  .settings(
    name := "benchmark",
    publish := {},
    publishLocal := {}
  )

lazy val root = project
  .in(file("."))
  .settings(
    name := "collection",
    publish := {},
    publishLocal := {}
  )
  .aggregate(ringbuffer)
