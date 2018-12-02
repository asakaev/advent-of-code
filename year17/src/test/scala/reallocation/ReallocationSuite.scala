package reallocation

import org.scalatest.FunSuite

class ReallocationSuite extends FunSuite with ReallocationDef with Solver {

  val s0 = Seq(0, 2, 7, 0)

  test("redistribute") {
    val s1 = Seq(2, 4, 1, 2)
    val s2 = Seq(3, 1, 2, 3)
    val s3 = Seq(0, 2, 3, 4)
    val s4 = Seq(1, 3, 4, 1)

    assert(redistribute(s0) == s1)
    assert(redistribute(s1) == s2)
    assert(redistribute(s2) == s3)
    assert(redistribute(s3) == s4)
    assert(redistribute(s4) == s1)
  }

  test("solution1") {
    assert(solution1(s0) == 5)
  }

  test("solution2") {
    assert(solution2(s0) == 4)
  }

}
