package checksum

import org.scalatest.FunSuite

class ChecksumSuite extends FunSuite with Solver {
  test("checksum") {
    val spreadsheet = Seq(
      Seq(5, 1, 9, 5),
      Seq(7, 5, 3),
      Seq(2, 4, 6, 8)
    )

    assert(solution(spreadsheet) == 18)
  }

  test("evenly divisible") {
    val spreadsheet = Seq(
      Seq(5, 9, 2, 8),
      Seq(9, 4, 7, 3),
      Seq(3, 8, 6, 5)
    )

    assert(evenlyDivisible(spreadsheet) == 9)
  }
}
