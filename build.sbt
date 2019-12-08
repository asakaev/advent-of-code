import Dependencies._

ThisBuild / scalaVersion := "2.13.1"

lazy val commonSettings = scalacOptions ++= List(
  "-unchecked",
  "-language:higherKinds"
)

lazy val year17 = (project in file("year17"))
  .settings(
    commonSettings,
    name := "Advent of Code 2017",
    libraryDependencies ++= commonDeps,
    libraryDependencies ++= testDeps
  )

lazy val year18 = (project in file("year18"))
  .settings(
    commonSettings,
    name := "Advent of Code 2018",
    libraryDependencies ++= commonDeps,
    libraryDependencies ++= testDeps
  )

lazy val year19 = (project in file("year19"))
  .settings(
    commonSettings,
    name := "Advent of Code 2019",
    libraryDependencies ++= commonDeps,
    libraryDependencies ++= testDeps
  )
