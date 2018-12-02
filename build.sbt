import Dependencies._

ThisBuild / scalaVersion := "2.12.7"

lazy val commonSettings = scalacOptions ++= Seq("-unchecked")

lazy val year17 = (project in file("year17"))
  .settings(
    commonSettings,
    name := "Advent of Code 2017",
    libraryDependencies ++= commonDeps
  )

lazy val year18 = (project in file("year18"))
  .settings(
    commonSettings,
    name := "Advent of Code 2018",
    libraryDependencies ++= commonDeps
  )
