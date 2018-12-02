package checksum

import scala.io.Source

object Checksum extends App with StringParserChecksum with Solver {
  val input       = Source.fromResource("checksum-input.txt")
  val spreadsheet = parse(input.getLines.toList)
  println(s"checksum: ${solution(spreadsheet)}")
  println(s"evenly divisible: ${evenlyDivisible(spreadsheet)}")
}
