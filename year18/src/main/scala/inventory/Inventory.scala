package inventory

import java.nio.file.Paths
import java.util.concurrent.Executors

import cats.effect.{ ExitCode, IO, IOApp, Resource }
import cats.implicits._
import fs2.{ Stream, io, text }

import scala.concurrent.ExecutionContext

object Inventory extends IOApp {

  case class Candidate(two: Boolean, three: Boolean)
  case class State(two: Int, three: Int)

  private val blockingExecutionContext =
    Resource.make(IO(ExecutionContext.fromExecutorService(Executors.newFixedThreadPool(2))))(ec => IO(ec.shutdown()))

  def candidate(boxId: String): Candidate = {
    val grouped = boxId.groupBy(identity).mapValues(_.size)
    val two     = grouped.collectFirst { case (_, n) if n == 2 => true }.getOrElse(false)
    val three   = grouped.collectFirst { case (_, n) if n == 3 => true }.getOrElse(false)
    Candidate(two, three)
  }

  def checksum(s: State): Int =
    s.two * s.three

  def reducer(s: State, c: Candidate): State =
    c match {
      case Candidate(true, true) => State(s.two + 1, s.three + 1)
      case Candidate(true, _)    => s.copy(two = s.two + 1)
      case Candidate(_, true)    => s.copy(three = s.three + 1)
      case _                     => s
    }

  def diffByOneChar(lh: String, rh: String): Option[String] = {
    val common = lh.zip(rh).map {
      case (l, r) if l == r => Some(l)
      case _                => None
    }

    if (common.count(_.isEmpty) == 1) Some(common.flatten.mkString)
    else None
  }

  def combinations(xs: List[String]): Stream[IO, (String, String)] =
    xs match {
      case Nil     => Stream.empty
      case y :: ys => Stream.emits(ys).map((y, _))
    }

  val lines: Stream[IO, String] =
    Stream.resource(blockingExecutionContext).flatMap { blockingEC =>
      io.file
        .readAll[IO](Paths.get("year18/data/inventory.txt"), blockingEC, 4096)
        .through(text.utf8Decode)
        .through(text.lines)
    }

  val boxIds: Stream[IO, String] =
    lines.filter(_.nonEmpty)

  val boxChecksum: IO[Option[Int]] =
    boxIds
      .map(candidate)
      .fold(State(0, 0))(reducer)
      .map(checksum)
      .compile
      .last

  val commonLettersBoxes: IO[Option[String]] =
    boxIds
      .scan(List.empty[String])((xs, x) => x :: xs)
      .flatMap(combinations)
      .map((diffByOneChar _).tupled)
      .unNone
      .head
      .compile
      .last

  def run(args: List[String]): IO[ExitCode] =
    (boxChecksum, commonLettersBoxes).parTupled
      .flatMap { case (bc, cl) => IO(println(s"Box checksum: $bc")) *> IO(println(s"Common letters boxes: $cl")) }
      .as(ExitCode.Success)

}
