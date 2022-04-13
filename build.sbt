val catsVersion = "2.7.0"
val kittensVersion = "3.0.0-M3"
val mouseVersion = "1.0.10"
val munitVersion = "0.7.29"

This / organization := "net.randallalexander"
This / scalaVersion := "3.1.0"
This / version := "0.1.0-snapshot"

Test / parallelExecution := false
Test / testOptions += Tests.Argument(TestFrameworks.MUnit, "-b")

lazy val buildSettings = Seq(
  Compile / console / scalacOptions ~= {
    _.filterNot(Set("-Ywarn-unused-import", "-Xfatal-warnings"))
  },
  Compile / console / scalacOptions ~= {
    _.filterNot(Set("-Ywarn-unused-import", "-Xfatal-warnings"))
  },
  libraryDependencies ++= Seq(
    "org.typelevel" %% "cats-core" % catsVersion,
    "org.typelevel" %% "kittens" % kittensVersion,
    "org.typelevel" %% "mouse" % mouseVersion,
    "org.scalameta" %% "munit" % munitVersion % Test
  )
)

lazy val root = project
  .in(file("."))
  .settings(name := "collection-ringbuffer")
  .settings(buildSettings)
  .settings(
    semanticdbEnabled := true, // enable SemanticDB
    semanticdbVersion := scalafixSemanticdb.revision // only required for Scala 2.x
  )
