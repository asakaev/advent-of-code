package tubes

import org.scalatest.FunSuite

import scala.io.Source

class TubesSuite extends FunSuite with StringParserTerrain with Solver {
  val level: String = Source.fromResource("tubes-example.txt").mkString

  test("path") {
    val path = solution(startPos)
    val expected = List(
      Down,
      Down,
      Down,
      Down,
      Down,
      Down,
      Right,
      Right,
      Right,
      Up,
      Up,
      Up,
      Up,
      Right,
      Right,
      Right,
      Down,
      Down,
      Down,
      Down,
      Right,
      Right,
      Right,
      Up,
      Up,
      Left,
      Left,
      Left,
      Left,
      Left,
      Left,
      Left,
      Left,
      Left,
      Left,
      Left,
      Left,
      Left
    )

    assert(path.reverse == expected)
  }

  test("cells") {
    val cells = traverse(startPos, solution(startPos).reverse)

    val expected = List(
      Vertical,
      Vertical,
      Letter('A'),
      Vertical,
      Vertical,
      Cross,
      Letter('B'),
      Horizontal,
      Cross,
      Vertical,
      Horizontal,
      Vertical,
      Cross,
      Horizontal,
      Horizontal,
      Cross,
      Letter('C'),
      Vertical,
      Vertical,
      Cross,
      Horizontal,
      Horizontal,
      Cross,
      Letter('D'),
      Cross,
      Horizontal,
      Horizontal,
      Vertical,
      Letter('E'),
      Horizontal,
      Horizontal,
      Horizontal,
      Horizontal,
      Vertical,
      Horizontal,
      Horizontal,
      Horizontal,
      Letter('F')
    )

    assert(cells.reverse == expected)
  }

  test("letters") {
    val letters  = collectLetters(traverse(startPos, solution(startPos).reverse).reverse)
    val expected = "ABCDEF"

    assert(letters == expected)
  }

}
