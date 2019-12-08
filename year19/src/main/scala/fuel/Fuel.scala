package fuel

import java.nio.file.Paths

import cats.effect._
import cats.implicits._
import fs2._

object Fuel extends IOApp {

  val lines: Stream[IO, String] =
    Stream.resource(Blocker[IO]).flatMap { blocker =>
      io.file
        .readAll[IO](Paths.get("year19/data/fuel.txt"), blocker, 4096)
        .through(text.utf8Decode)
        .through(text.lines)
        .filter(_.nonEmpty)
    }

  val mass: Stream[IO, Int] =
    lines.evalMap(s => IO(s.toInt))

  def fuelRequired(mass: Int): Int =
    (mass / 3.0).floor.toInt - 2

  val p1: Stream[IO, Int] =
    mass.map(fuelRequired).fold1(_ + _)

  val p2: Stream[IO, Int] =
    mass.flatMap(fuelMass).fold1(_ + _)

  def fuelMass(mass: Int): Stream[Pure, Int] =
    Stream.unfold(mass) { m =>
      fuelRequired(m) match {
        case m if m > 0 => Some(m -> m)
        case _          => None
      }
    }

  def lastOrDie[F[_]: RaiseThrowable, O](e: String): Pipe[F, O, O] =
    _.last.flatMap {
      case Some(o) => Stream.emit(o)
      case None    => Stream.raiseError(new Error(e))
    }

  def run(args: List[String]): IO[ExitCode] =
    Stream(
      p1.through(lastOrDie("P1 failed")).map(p1 => s"1. Total Fuel Requirement: $p1"),
      p2.through(lastOrDie("P2 failed")).map(p2 => s"2. Mass of the added fuel: $p2")
    ).parJoinUnbounded.showLinesStdOut.compile.drain.as(ExitCode.Success)

}
