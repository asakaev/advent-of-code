package halting

object Halting extends App with Solver {

  // TODO: use fastparse to get inital state, number of steps and transition matrix from input text

  val initialState = A
  val transitions: TransitionMatrix = Map(
    A -> Map(0 -> (1, Right, B), 1 -> (1, Left, E)),
    B -> Map(0 -> (1, Right, C), 1 -> (1, Right, F)),
    C -> Map(0 -> (1, Left, D), 1  -> (0, Right, B)),
    D -> Map(0 -> (1, Right, E), 1 -> (0, Left, C)),
    E -> Map(0 -> (1, Left, A), 1  -> (0, Right, D)),
    F -> Map(0 -> (1, Right, A), 1 -> (1, Right, C))
  )

  val steps = 12523873
  val tape  = solution(steps)
  println(tape)
  println(s"checksum: ${checksum(tape)}")
}
