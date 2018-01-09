package reallocation

import scala.io.Source

object Reallocation extends App with StringParserReallocation with Solver {
  val line  = Source.fromResource("reallocation-input.txt").mkString
  val input = parse(line)
  println(s"redistribution cycles: ${solution1(input)}")
  println(s"cycles are in the infinite loop: ${solution2(input)}")
}
