package spiral

object Spiral extends App with SpiralGen with Solver {
  val input = 361527

  val grid   = spiral(input)
  val stress = stressSpiral(input)

  println(s"steps from $input to 1: ${steps(input, 1)}")
  println(s"first value larger $input written: ${firstValueLargerWritten(stress, input)}")
}
