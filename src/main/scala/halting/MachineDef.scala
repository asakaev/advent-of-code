package halting

trait MachineDef {

  type Symbol = Int

  val initialSymbol: Symbol = 0

  case class Tape(l: List[Symbol], head: Symbol, r: List[Symbol]) {

    def read: Symbol           = head
    def write(s: Symbol): Tape = Tape(l, s, r)

    def left: Tape = this match {
      case Tape(Nil, h, right)  => Tape(Nil, initialSymbol, h :: right)
      case Tape(left, h, right) => Tape(left.tail, left.head, h :: right)
    }

    def right: Tape = this match {
      case Tape(left, h, Nil)   => Tape(h :: left, initialSymbol, Nil)
      case Tape(left, h, right) => Tape(h :: left, right.head, right.tail)
    }

    def toList: List[Symbol] =
      l.reverse ++ (head :: r)

    override def toString: String =
      s"... ${l.reverse.mkString(" ")} [$head] ${r.mkString(" ")} ..."
  }

  /**
    * smart constructor
    */
  def tape(list: List[Symbol] = List.empty, cursor: Int = 0): Tape =
    if (list.isEmpty) Tape(List.empty, initialSymbol, List.empty)
    else list.splitAt(cursor) match { case (l, r) => Tape(l.reverse, r.head, r.tail) }

  sealed trait State
  case object A extends State
  case object B extends State
  case object C extends State
  case object D extends State
  case object E extends State
  case object F extends State

  sealed trait Move
  case object Left  extends Move
  case object Right extends Move

  /**
    * symbol to write, direction, next state
    */
  type TransitionMatrix = Map[State, Map[Symbol, (Symbol, Move, State)]]

}
