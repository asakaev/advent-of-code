import sbt._

object Dependencies {
  lazy val fs2Version                  = "2.1.0"
  lazy val catsVersion                 = "2.0.0"
  lazy val fastParseVersion            = "2.1.3"
  lazy val scalaTestVersion            = "3.1.0"
  lazy val catsTestKitScalaTestVersion = "1.0.0-RC1"

  val fs2                  = "co.fs2"        %% "fs2-core"               % fs2Version
  val fs2io                = "co.fs2"        %% "fs2-io"                 % fs2Version
  val fastParse            = "com.lihaoyi"   %% "fastparse"              % fastParseVersion
  val scalaTest            = "org.scalatest" %% "scalatest"              % scalaTestVersion
  val catsTestKitScalaTest = "org.typelevel" %% "cats-testkit-scalatest" % catsTestKitScalaTestVersion

  val commonDeps = Seq(fs2, fs2io, fastParse)
  val testDeps   = Seq(scalaTest, catsTestKitScalaTest).map(_ % Test)
}
