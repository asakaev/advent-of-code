package calibration

import java.nio.file.Paths
import java.util.concurrent.Executors

import cats.effect.{ ExitCode, IO, IOApp, Resource }
import cats.implicits._
import common.Decoder
import common.Parser._
import fs2.{ Stream, io, text }

import scala.concurrent.ExecutionContext
import scala.util.Try

object Calibration extends IOApp {

  trait State
  case object Init                  extends State
  case class Search(seen: Set[Int]) extends State
  case class Found(duplicate: Int)  extends State

  private val blockingExecutionContext =
    Resource.make(IO(ExecutionContext.fromExecutorService(Executors.newFixedThreadPool(2))))(ec => IO(ec.shutdown()))

  implicit val intDecoder: Decoder[Int] =
    s => Try(s.toInt).toEither

  def reducer(state: State, v: Int): State =
    state match {
      case Init                             => Search(Set(v))
      case Search(seen) if seen.contains(v) => Found(v)
      case Search(seen)                     => Search(seen + v)
      case _                                => state
    }

  val lines: Stream[IO, String] =
    Stream.resource(blockingExecutionContext).flatMap { blockingEC =>
      io.file
        .readAll[IO](Paths.get("year18/data/calibration.txt"), blockingEC, 4096)
        .through(text.utf8Decode)
        .through(text.lines)
    }

  val freqChanges: Stream[IO, Int] =
    lines.map(decode[Int](_).toOption).unNone

  val resultingFreq: IO[Option[Int]] =
    freqChanges.scan(0)(_ + _).compile.last

  val duplicateFreq: IO[Option[Int]] =
    freqChanges.repeat
      .scan(0)(_ + _)
      .unchunk
      .scan[State](Init)(reducer)
      .collectFirst { case Found(duplicate) => duplicate }
      .compile
      .last

  def run(args: List[String]): IO[ExitCode] =
    (resultingFreq, duplicateFreq).parTupled
      .flatMap { case (rf, df) => IO(println(s"Resulting freq: $rf")) *> IO(println(s"Duplicate freq: $df")) }
      .as(ExitCode.Success)

}
