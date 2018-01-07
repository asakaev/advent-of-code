package spiral

import java.lang.Math.abs

trait Solver extends GridDef {

  /**
    * Steps are required to carry the data from the square
    * identified in puzzle input all the way to the access port
    */
  def steps(a: Int, b: Int): Int =
    manhattanDistance(find(a), find(b))

  def manhattanDistance(p: Pos, q: Pos): Int =
    abs(p.col - q.col) + abs(p.row - q.row)

  /**
    * First value written that is larger than your puzzle input
    */
  def firstValueLargerWritten(g: Grid, input: Int): Int =
    g.last.sorted.find(_ > input).get

}
