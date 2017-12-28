package tubes

import scala.io.Source

object Tubes extends App with StringParserTerrain with Solver {
  val level = Source.fromResource("tubes-example.txt").mkString

  val steps = solution(startPos)
  println(s"steps: ${steps.reverse}")
  println(s"total steps: ${steps.size}")

  val cells = traverse(startPos, steps.reverse)
  println(s"cells: ${cells.reverse}")
  println(s"cells total: ${cells.size}")

  val letters = collectLetters(cells.reverse)
  println(s"letters: $letters")
}
