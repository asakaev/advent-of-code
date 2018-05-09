package registers

object Solver {
  import RegistersDef._

  def solution1(cs: Commands): Int = interpret(cs).head.values.max
  def solution2(cs: Commands): Int = interpret(cs).filter(_.nonEmpty).map(_.values.max).max
}
