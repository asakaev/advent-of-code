package trampolines

import scala.annotation.tailrec

trait Solver extends TrampolinesDef {

  def solution1(xs: Vector[Int]): Int = solution(xs)(_.jump1)
  def solution2(xs: Vector[Int]): Int = solution(xs)(_.jump2)

  def done(s: State): Boolean = (s.cur < 0) || (s.cur >= s.offsets.size)

  def solution(xs: Vector[Int])(next: State => State): Int = {
    @tailrec def loop(s: State, steps: Int): Int =
      if (done(s)) steps else loop(next(s), steps + 1)
    loop(State(0, xs), 0)
  }

}
