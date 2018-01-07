package spiral

import scala.annotation.tailrec

trait SpiralGen extends GridDef {

  /**
    * Generate spiral grid
    *
    * 17  16  15  14  13
    * 18   5   4   3  12
    * 19   6   1   2  11
    * 20   7   8   9  10
    * 21  22  23---> ...
    *
    * Stops when bottom row contains value eq 'n'
    */
  def spiral(n: Int): Grid =
    spiralGeneric(n)(simpleBottomRow, _.contains(n))

  /**
    * Generate stress spiral
    *
    * 147  142  133  122   59
    * 304    5    4    2   57
    * 330   10    1    1   54
    * 351   11   23   25   26
    * 362  747  806--->   ...
    *
    * Stops when bottom row contains value greater then 'n'
    */
  def stressSpiral(n: Int): Grid =
    spiralGeneric(n)(stressBottomRow, _.exists(_ > n))

  def spiralGeneric(n: Int)(bottomRowGen: Grid => Seq[Int], done: Seq[Int] => Boolean): Grid = {
    @tailrec
    def loop(g: Grid): Grid = {
      val row  = bottomRowGen(g)
      val grid = g :+ row

      if (done(row)) grid
      else loop(rotateCw(grid))
    }

    loop(Seq(Seq(1)))
  }

  def rotateCw(g: Grid): Grid =
    g.reverse.transpose

  def simpleBottomRow(g: Grid): Seq[Int] = {
    val next = g.last.max + 1
    g.last.indices.map(_ + next)
  }

  def neighborsSum(p: Pos, g: Grid): Int =
    p.neighbors.flatMap(gridValue(_, g)).sum

  def stressBottomRow(g: Grid): Seq[Int] = {
    val emptyRow = Seq.fill(g.head.size)(0)
    g.head.indices.foldLeft(emptyRow) { (row, cI) =>
      row.updated(cI, neighborsSum(Pos(g.size, cI), g :+ row))
    }
  }

}
