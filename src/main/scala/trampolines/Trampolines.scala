package trampolines

import scala.io.Source

object Trampolines extends App with Solver with StringParserTrampolines {
  val lines = Source.fromResource("trampolines-input.txt")
  val input = parse(lines.getLines.toVector)
  println(s"steps1: ${solution1(input)}")
  println(s"steps2: ${solution2(input)}")
}
