package intcode

import java.nio.file.Paths

import cats.effect._
import cats.implicits._
import fs2._
import intcode.Computer._

object IntCode extends IOApp {

  val lines: Stream[IO, String] =
    Stream.resource(Blocker[IO]).flatMap { blocker =>
      io.file
        .readAll[IO](Paths.get("year19/data/intcode.txt"), blocker, 4096)
        .through(text.utf8Decode)
        .through(text.lines)
        .dropLast
    }

  val codes: Stream[IO, Int] =
    lines.flatMap(s => Stream.emits(s.split(","))).evalMap(s => IO(s.toInt))

  val memory: IO[Vector[Int]] =
    codes.compile.toVector

  val p1: IO[Int] =
    memory.flatMap(xs => computer(xs, Input(12, 2)))

  def combinations(a: Int, b: Int): Stream[IO, (Int, Int)] =
    for {
      l <- Stream.range(a, b + 1)
      r <- Stream.range(a, b + 1)
    } yield l -> r

  val inputs: Stream[IO, Input] =
    combinations(0, 99).map(Input.tupled)

  val expected: Int = 19690720

  def checksum(i: Input): Int =
    100 * i.noun + i.verb

  val p2: IO[Int] =
    Stream
      .eval(memory)
      .flatMap { m =>
        inputs.evalMap { i =>
          computer(m, i).map(_ -> i)
        }
      }
      .collectFirst { case (v, i) if v == expected => checksum(i) }
      .compile
      .lastOrError

  def run(args: List[String]): IO[ExitCode] =
    (p1, p2).parTupled
      .flatMap {
        case (p1, p2) =>
          IO(println(s"1. Computed value: $p1")) *>
            IO(println(s"2. Input checksum that produce $expected: $p2"))
      }
      .as(ExitCode.Success)

}
