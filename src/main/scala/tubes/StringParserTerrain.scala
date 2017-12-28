package tubes

trait StringParserTerrain extends GameDef {

  val level: String

  def terrainFunction(levelVector: Vector[Vector[Char]]): Pos => Cell = {
    def isInsideVector[T](idx: Int, vector: Vector[T]) =
      (idx >= 0) && (idx < vector.size)

    def isInsideMatrix(p: Pos) =
      isInsideVector(p.row, levelVector) && isInsideVector(p.col, levelVector(p.row))

    def isValidPosition(p: Pos) =
      if (!isInsideMatrix(p)) Empty
      else {
        levelVector(p.row)(p.col) match {
          case '+' => Cross
          case '|' => Vertical
          case '-' => Horizontal
          case ' ' => Empty
          case c   => Letter(c)
        }
      }

    isValidPosition
  }

  def findChar(c: Char, row: Vector[Char]): Pos =
    row.indexOf(c) match {
      case -1  => throw new NoSuchElementException
      case col => Pos(0, col)
    }

  private lazy val vector: Vector[Vector[Char]] =
    Vector(level.split("\n").map(str => Vector(str: _*)): _*)

  lazy val terrain: Terrain = terrainFunction(vector)

  lazy val startPos: Pos =
    findChar('|', vector.head).deltaRow(-1)

}
