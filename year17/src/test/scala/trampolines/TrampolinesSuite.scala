package trampolines

import org.scalatest.FunSuite

class TrampolinesSuite extends FunSuite with Solver {

  val offsets = Vector(0, 3, 0, 1, -3)
  val s0      = State(0, offsets)

  test("jumps1") {
    val s1 = s0.jump1
    val s2 = s1.jump1
    val s3 = s2.jump1
    val s4 = s3.jump1
    val s5 = s4.jump1

    assert(s0.cur == 0)
    assert(s0.offsets == Seq(0, 3, 0, 1, -3))

    assert(s1.cur == 0)
    assert(s1.offsets == Seq(1, 3, 0, 1, -3))

    assert(s2.cur == 1)
    assert(s2.offsets == Seq(2, 3, 0, 1, -3))

    assert(s3.cur == 4)
    assert(s3.offsets == Seq(2, 4, 0, 1, -3))

    assert(s4.cur == 1)
    assert(s4.offsets == Seq(2, 4, 0, 1, -2))

    assert(s5.cur == 5)
    assert(s5.offsets == Seq(2, 5, 0, 1, -2))
  }

  test("jumps2") {
    val s10 = (1 to 10).foldLeft(s0)((s, _) => s.jump2)

    assert(s10.cur == 5)
    assert(s10.offsets == Seq(2, 3, 2, 3, -1))
  }

  test("solution1") {
    assert(solution1(offsets) == 5)
  }

  test("solution2") {
    assert(solution2(offsets) == 10)
  }

}
