package slice

import java.nio.file.Paths
import java.util.concurrent.Executors

import cats.effect.{ ExitCode, IO, IOApp, Resource }
import cats.implicits._
import common.Parser._
import fs2.{ io, text, Stream }
import slice.Rectangle.Point

import scala.concurrent.ExecutionContext

object Slice extends IOApp {

  private val blockingExecutionContext =
    Resource.make(IO(ExecutionContext.fromExecutorService(Executors.newFixedThreadPool(2))))(ec => IO(ec.shutdown()))

  val lines: Stream[IO, String] =
    Stream.resource(blockingExecutionContext).flatMap { blockingEC =>
      io.file
        .readAll[IO](Paths.get("year18/data/slice.txt"), blockingEC, 4096)
        .through(text.utf8Decode)
        .through(text.lines)
    }

  val claims: Stream[IO, Claim] =
    lines.map(decode[Claim](_).toOption).unNone

  val inchesWithinTwo: IO[Option[Int]] =
    claims
      .flatMap(_.rectangle.area)
      .fold(Map.empty[Point, Int]) { (m, p) =>
        m.updated(p, m.getOrElse(p, 0) + 1)
      }
      .map(_.values.count(_ > 1))
      .compile
      .last

  final case class State(ids: Set[Int], m: Map[Point, Int])

  val notOverlappedId: IO[Option[Int]] =
    claims
      .map(_.id)
      .fold(Set.empty[Int])(_ + _)
      .flatMap { ids =>
        claims
          .flatMap { c =>
            c.rectangle.area.map { p =>
              (p, c.id)
            }
          }
          .fold(State(ids, Map.empty)) {
            case (State(remains, seen), (p, id)) =>
              seen.get(p) match {
                case Some(id0) => State(remains - (id0, id), seen)
                case None      => State(remains, seen.updated(p, id))
              }
          }
          .map(_.ids.headOption)
          .unNone
      }
      .compile
      .last

  def run(args: List[String]): IO[ExitCode] =
    (inchesWithinTwo, notOverlappedId).parTupled
      .flatMap { case (r1, r2) => IO(println(s"Part 1: $r1")) >> IO(println(s"Part 2: $r2")) }
      .as(ExitCode.Success)

}
