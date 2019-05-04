package reduction

import java.nio.file.Paths
import java.util.concurrent.Executors

import cats.effect.{ ExitCode, IO, IOApp, Resource }
import cats.implicits._
import fs2.{ io, text, Stream }

import scala.concurrent.ExecutionContext

object Reduction extends IOApp {

  private val blockingExecutionContext =
    Resource.make(IO(ExecutionContext.fromExecutorService(Executors.newFixedThreadPool(2))))(ec => IO(ec.shutdown()))

  val lines: Stream[IO, String] =
    Stream.resource(blockingExecutionContext).flatMap { blockingEC =>
      io.file
        .readAll[IO](Paths.get("year18/data/reduction.txt"), blockingEC, 4096)
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
      .flatMap { case (u, s) => IO(println(s"Units remain: $u")) >> IO(println(s"Shortest polymer: $s")) }
      .as(ExitCode.Success)
}
