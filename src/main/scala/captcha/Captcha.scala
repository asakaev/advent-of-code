package captcha

import scala.io.Source

object Captcha extends App with StringParserCaptcha with Solver {
  val input  = Source.fromResource("captcha-input.txt")
  val digits = parse(input.mkString)
  println(s"next digit: ${nextDigit(digits: _*)}")
  println(s"halfway around: ${halfwayAround(digits: _*)}")
}
