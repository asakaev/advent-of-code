package trampolines

trait TrampolinesDef {

  case class State(cur: Int, offsets: Vector[Int]) {
    def jump1: State = jump(offsets(cur) + 1)
    def jump2: State = jump(if (offsets(cur) >= 3) offsets(cur) - 1 else offsets(cur) + 1)

    def jump(next: => Int): State = State(cur + offsets(cur), offsets.updated(cur, next))
  }

}
