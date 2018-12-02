package passphrases

import scala.io.Source

object Passphrases extends App with Solver {
  val input = Source.fromResource("passphrases-input.txt").getLines.toSeq

  println(s"without duplicates: ${solution1(input)}")
  println(s"without anagrams: ${solution2(input)}")
}
