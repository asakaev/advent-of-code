package halting

import org.scalatest.FunSuite

class HaltingSuite extends FunSuite with Solver {

  val initialState: State = A
  val transitions: TransitionMatrix = Map(
    A -> Map(0 -> (1, Right, B), 1 -> (0, Left, B)),
    B -> Map(0 -> (1, Left, A), 1  -> (1, Right, A))
  )

  test("solution") {
    assert(solution(6) == tape(List(1, 1, 0, 1), 2))
  }

  test("checksum") {
    assert(checksum(tape(List(1, 1, 0, 1), 2)) == 3)
  }
}
