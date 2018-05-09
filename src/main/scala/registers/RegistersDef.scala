package registers

object RegistersDef {

  type Id        = String
  type Value     = Int
  type Commands  = Seq[Command]
  type Registers = Map[Id, Value]

  sealed trait Instruction
  final case class Inc(id: Id, v: Value) extends Instruction
  final case class Dec(id: Id, v: Value) extends Instruction

  sealed trait RelationalOp
  case object Eq extends RelationalOp
  case object Ne extends RelationalOp
  case object Gt extends RelationalOp
  case object Lt extends RelationalOp
  case object Ge extends RelationalOp
  case object Le extends RelationalOp

  final case class Predicate(id: Id, relationalOp: RelationalOp, v: Value)

  final case class Command(i: Instruction, p: Predicate)

  def execInstruction(rs: Registers, i: Instruction): Registers = {
    def update(id: Id, v: Value)(f: (Value, Value) => Value) = rs.updated(id, f(rs.getOrElse(id, 0), v))
    i match {
      case Inc(id, v) => update(id, v)(_ + _)
      case Dec(id, v) => update(id, v)(_ - _)
    }
  }

  def condition(rs: Registers, p: Predicate): Boolean = {
    def eval[A: Ordering](ro: RelationalOp)(a: A, b: A): Boolean = {
      val ord = implicitly[Ordering[A]]
      ro match {
        case Eq => ord.equiv(a, b)
        case Ne => !ord.equiv(a, b)
        case Gt => ord.gt(a, b)
        case Lt => ord.lt(a, b)
        case Ge => ord.gteq(a, b)
        case Le => ord.lteq(a, b)
      }

    }

    eval(p.relationalOp)(rs.getOrElse(p.id, 0), p.v)
  }

  def interpretCommand(rs: Registers, c: Command): Registers =
    if (condition(rs, c.p)) execInstruction(rs, c.i) else rs

  /**
    * Reversed list of registers state
    */
  def interpret(cs: Commands): List[Registers] =
    cs.foldLeft(List(Map.empty[Id, Value])) { (xs, c) =>
      interpretCommand(xs.head, c) :: xs
    }

}
