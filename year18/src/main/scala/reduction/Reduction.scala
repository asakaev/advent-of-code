package reduction

import java.nio.file.Paths
import java.util.concurrent.Executors

import cats.effect.{Blocker, ExitCode, IO, IOApp, Resource}
import cats.implicits._
import fs2.{Stream, io, text}

import scala.concurrent.ExecutionContext

object Reduction extends IOApp {

  val lines: Stream[IO, String] =
    Stream.resource(Blocker[IO]).flatMap { blocker =>
      io.file
        .readAll[IO](Paths.get("year18/data/reduction.txt"), blocker, 4096)
        .through(text.utf8Decode)
        .through(text.lines)
    }

  val polymer: IO[Option[String]] =
    lines.head.compile.last

  val ur: IO[Option[Int]] =
    polymer.map(_.map(unitsRemain))

  val sp: IO[Option[Int]] =
    polymer.map(_.map(shortestPolymer))

  override def run(args: List[String]): IO[ExitCode] =
    (ur, sp).parTupled
      .flatMap {
        case (u, s) => IO(println(s"Units remain: $u")) >> IO(println(s"Shortest polymer: $s"))
      }
      .as(ExitCode.Success)
}
