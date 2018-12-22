import sbt._

object Dependencies {
  lazy val scalaTestVersion = "3.0.5"
  lazy val fs2Version       = "1.0.2"
  lazy val catsVersion      = "1.5.0"

  val fs2       = "co.fs2"        %% "fs2-core"     % fs2Version
  val fs2io     = "co.fs2"        %% "fs2-io"       % fs2Version
  val scalaTest = "org.scalatest" %% "scalatest"    % scalaTestVersion
  val catsLaws  = "org.typelevel" %% "cats-testkit" % catsVersion

  val commonDeps = Seq(fs2, fs2io)
  val testDeps   = Seq(scalaTest, catsLaws).map(_ % Test)
}
