package intcode

import cats.effect.IO
import fs2.Stream

object Computer {

  sealed trait Instruction
  case object Halt                             extends Instruction
  final case class Sum(a: Int, b: Int, r: Int) extends Instruction
  final case class Mul(a: Int, b: Int, r: Int) extends Instruction

  final case class State(p: Int, m: Vector[Int])

  final case class Input(noun: Int, verb: Int)

  def extract(p: Int, m: Vector[Int]): IO[Instruction] =
    for {
      op <- IO(m(p))
      i <- op match {
        case 1  => IO(Sum(m(p + 1), m(p + 2), m(p + 3)))
        case 2  => IO(Mul(m(p + 1), m(p + 2), m(p + 3)))
        case 99 => IO.pure(Halt)
        case i  => IO.raiseError(new Exception(s"UnknownInstruction($p, $i)"))
      }
    } yield i

  def interpret(s: State, i: Instruction): IO[Option[State]] =
    i match {
      case Sum(a, b, r) => IO(Some(State(s.p + 4, s.m.updated(r, s.m(a) + s.m(b)))))
      case Mul(a, b, r) => IO(Some(State(s.p + 4, s.m.updated(r, s.m(a) * s.m(b)))))
      case Halt         => IO.pure(None)
      case _            => IO.raiseError(new Exception("Memory.failure"))
    }

  def compute(m: Vector[Int]): IO[Int] =
    Stream
      .unfoldEval(State(0, m)) { s =>
        for {
          i <- extract(s.p, s.m)
          e <- interpret(s, i)
        } yield e.map(s => s.m -> s)
      }
      .compile
      .lastOrError
      .map(_.head)

  def restore(m: Vector[Int], i: Input): IO[Vector[Int]] =
    IO(m.updated(1, i.noun).updated(2, i.verb))

  def computer(m: Vector[Int], i: Input): IO[Int] =
    for {
      m <- restore(m, i)
      v <- compute(m)
    } yield v

}
