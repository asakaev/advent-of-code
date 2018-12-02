package spiral

import org.scalatest.FunSuite

class SpiralSuite extends FunSuite with SpiralGen with Solver {

  val grid = Seq(
    Seq(17, 16, 15, 14, 13),
    Seq(18, 5, 4, 3, 12),
    Seq(19, 6, 1, 2, 11),
    Seq(20, 7, 8, 9, 10),
    Seq(21, 22, 23, 24, 25)
  )

  val stressGrid = Seq(
    Seq(147, 142, 133, 122, 59),
    Seq(304, 5, 4, 2, 57),
    Seq(330, 10, 1, 1, 54),
    Seq(351, 11, 23, 25, 26),
    Seq(362, 747, 806, 880, 931)
  )

  test("manhattan distance") {
    assert(manhattanDistance(Pos(1, 1), Pos(1, 1)) == 0)
    assert(manhattanDistance(Pos(1, 1), Pos(2, 2)) == 2)
  }

  test("find square inside grid") {
    assert(find(1) == Pos(2, 2))
    assert(find(17) == Pos(0, 0))
    assert(find(16) == Pos(0, 1))
    assert(find(13) == Pos(0, 4))
    assert(find(21) == Pos(4, 0))
    assert(find(25) == Pos(4, 4))
  }

  test("steps to access port") {
    assert(steps(1, 1) == 0)
    assert(steps(12, 1) == 3)
    assert(steps(23, 1) == 2)
  }

  test("spiral gen") {
    assert(spiral(25) == grid)
  }

  test("stress spiral gen") {
    assert(stressSpiral(880) == stressGrid)
  }

  test("first value larger written") {
    assert(firstValueLargerWritten(stressGrid, 600) == 747)
  }
}
