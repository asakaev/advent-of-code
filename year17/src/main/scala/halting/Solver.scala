package halting

import scala.annotation.tailrec

trait Solver extends MachineDef {

  val initialState: State
  val transitions: TransitionMatrix

  def solution(steps: Int): Tape = {
    @tailrec
    def loop(state: State, tape: Tape, n: Int): Tape =
      if (n >= steps) tape
      else {
        val (symbol, move, nextState) = transitions(state)(tape.read)
        val tapeUpdated = move match {
          case Left  => tape.write(symbol).left
          case Right => tape.write(symbol).right
        }

        loop(nextState, tapeUpdated, n + 1)
      }

    loop(initialState, tape(), 0)
  }

  def checksum(tape: Tape): Int =
    tape.toList.count(_ == 1)

}
