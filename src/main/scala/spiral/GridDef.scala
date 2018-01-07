package spiral

trait GridDef {

  type Grid = Seq[Seq[Int]]

  val grid: Grid

  sealed trait Direction
  case object N  extends Direction
  case object NE extends Direction
  case object E  extends Direction
  case object SE extends Direction
  case object S  extends Direction
  case object SW extends Direction
  case object W  extends Direction
  case object NW extends Direction

  case class Pos(row: Int, col: Int) {
    def delta(r: Int, c: Int) = Pos(row + r, col + c)

    /**
      * Cardinal direction
      */
    def move(d: Direction): Pos = d match {
      case N  => delta(-1, 0)
      case NE => delta(-1, 1)
      case E  => delta(0, 1)
      case SE => delta(1, 1)
      case S  => delta(1, 0)
      case SW => delta(1, -1)
      case W  => delta(0, -1)
      case NW => delta(-1, -1)
    }

    val directions: Seq[Direction] = Seq(N, NE, E, SE, S, SW, W, NW)

    def neighbors: Seq[Pos] = directions.map(move)
  }

  def gridValue(p: Pos, g: Grid): Option[Int] = {
    def insideSeq[T](idx: Int, seq: Seq[T]) = (idx >= 0) && (idx < seq.size)
    def insideGrid(p: Pos)                  = insideSeq(p.row, g) && insideSeq(p.col, g(p.row))

    if (insideGrid(p)) Some(g(p.row)(p.col)) else None
  }

  def find(square: Int): Pos =
    grid.indices
      .flatMap(r => grid(r).indices.map(c => Pos(r, c)))
      .find(p => grid(p.row)(p.col) == square)
      .get

}
