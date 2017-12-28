package tubes

trait GameDef {

  case class Pos(row: Int, col: Int) {
    def deltaRow(d: Int): Pos = copy(row = row + d)
    def deltaCol(d: Int): Pos = copy(col = col + d)
  }

  val startPos: Pos

  type Terrain = Pos => Cell

  val terrain: Terrain

  sealed trait Step
  case object Left  extends Step
  case object Right extends Step
  case object Up    extends Step
  case object Down  extends Step

  case class Packet(pos: Pos) {
    def deltaRow(d: Int) = Packet(pos.deltaRow(d))
    def deltaCol(d: Int) = Packet(pos.deltaCol(d))

    def left  = deltaCol(-1)
    def right = deltaCol(1)
    def up    = deltaRow(-1)
    def down  = deltaRow(1)

    def neighbors: List[(Packet, Step)] =
      List((left, Left), (right, Right), (up, Up), (down, Down))

    def legalNeighbors: List[(Packet, Step)] = neighbors.filter {
      case (p, _) => p.isLegal
    }

    def isLegal: Boolean = terrain(pos) match {
      case Empty => false
      case _     => true
    }
  }

  sealed trait Cell
  case object Empty      extends Cell
  case object Vertical   extends Cell
  case object Horizontal extends Cell
  case object Cross      extends Cell

  final case class Letter(c: Char) extends Cell

}
