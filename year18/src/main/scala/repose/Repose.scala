package repose

import java.nio.file.Paths
import java.time._
import java.util.concurrent.Executors

import cats.Monoid
import cats.effect.{ ExitCode, IO, IOApp, Resource }
import cats.implicits._
import common.Parser._
import fs2.{ io, text, Pure, Stream }
import repose.Record.{ BeginsShift, FallsAsleep, WakesUp }
import repose.instances._

import scala.concurrent.ExecutionContext

object Repose extends IOApp {

  private val blockingExecutionContext =
    Resource.make(IO(ExecutionContext.fromExecutorService(Executors.newFixedThreadPool(2))))(ec => IO(ec.shutdown()))

  val lines: Stream[IO, String] =
    Stream.resource(blockingExecutionContext).flatMap { blockingEC =>
      io.file
        .readAll[IO](Paths.get("year18/data/repose.txt"), blockingEC, 4096)
        .through(text.utf8Decode)
        .through(text.lines)
    }

  val records: Stream[IO, Record] =
    lines.map(decode[Record](_).toOption).unNone

  sealed trait Status
  case class Sleep(at: Instant) extends Status
  case object Awake             extends Status
  case object NA                extends Status

  final case class GuardState(status: Status, spans: List[Span])

  final case class Span(start: Instant, stopExclusive: Instant) {
    def duration: Duration = Duration.between(start, stopExclusive)
  }

  // TODO: can be done in one pass. Map[Id, List[Span]]. store current id and append to map list
  def spans(id: Int, rs: List[Record]): List[Span] =
    rs.foldLeft(GuardState(NA, List.empty)) { (acc, r) =>
        (acc.status, r.e) match {
          case (_, BeginsShift(id0)) if id0 == id => acc.copy(Awake)
          case (Sleep(at), _: BeginsShift)        => acc.copy(NA, Span(at, r.t) :: acc.spans)
          case (_, _: BeginsShift)                => acc.copy(NA)
          case (Sleep(at), WakesUp)               => acc.copy(Awake, Span(at, r.t) :: acc.spans)
          case (Awake, FallsAsleep)               => acc.copy(Sleep(r.t))
          case _                                  => acc
        }
      }
      .spans

  final case class StateA(curr: Option[Int], m: Map[Int, List[Record]])

  def append(id: Int, m: Map[Int, List[Record]], r: Record): Map[Int, List[Record]] =
    m.updated(id, r :: m.getOrElse(id, List.empty))

  def eventsByGuard(s: StateA, r: Record): StateA =
    r.e match {
      case BeginsShift(id) => s.copy(curr = Some(id), m = append(id, s.m, r))
      case FallsAsleep     => s.curr.map(c => s.copy(m = append(c, s.m, r))).getOrElse(s)
      case WakesUp         => s.curr.map(c => s.copy(m = append(c, s.m, r))).getOrElse(s)
    }

  final case class StateB(sleepAt: Option[Instant], spans: List[Span])

  def accumulate(s: StateB, r: Record): StateB =
    r.e match {
      case FallsAsleep => s.copy(sleepAt = Some(r.t))
      case WakesUp     => s.sleepAt.map(t => s.copy(None, Span(t, r.t) :: s.spans)).getOrElse(s)
      case _           => s
    }

  def minute(i: Instant): Int =
    LocalDateTime.ofInstant(i, ZoneOffset.UTC).getMinute

  def minutes(span: Span): Stream[Pure, Int] =
    Stream.range(minute(span.start), minute(span.stopExclusive))

  def hash(id: Int, minute: Int): Int =
    id * minute

  val spans: Stream[IO, Map[Int, List[Span]]] =
    records
      .fold(List.empty[Record])((rs, r) => r :: rs)
      .map { rs =>
        rs.sorted
          .foldLeft(StateA(None, Map.empty))(eventsByGuard)
          .m
          .mapValues(_.reverse)
          .mapValues { rs =>
            rs.foldLeft(StateB(None, List.empty))(accumulate).spans
          }
          .filter(_._2.nonEmpty)
      }

  def minutes2(span: Span): Range =
    Range(minute(span.start), minute(span.stopExclusive))

  val p1: IO[Option[Int]] =
    spans
      .flatMap { m =>
        val durations: Map[Int, (Duration, List[Span])] =
          m.mapValues { spans =>
            (Monoid[Duration].combineAll(spans.map(_.duration)), spans)
          }

        val most   = durations.maxBy(_._2._1)
        val mostId = most._1

        val spans2: List[Span] = most._2._2

        Stream
          .emits(spans2)
          .flatMap(minutes)
          .fold(Map.empty[Int, Int]) { (acc, minute) =>
            acc.get(minute) match {
              case Some(m0) => acc.updated(minute, m0 + 1)
              case None     => acc.updated(minute, 1)
            }
          }
          .map(_.maxBy(_._2)._1)
          .map { minute =>
            hash(mostId, minute)
          }

      }
      .compile
      .last

  val p2: IO[Option[Int]] =
    spans
      .map { m: Map[Int, List[Span]] =>
        val guardMinuteOccurrence = m.mapValues { ss =>
          ss.flatMap { s =>
              minutes2(s).toList
            }
            .groupBy(identity)
            .mapValues(_.size)
            .maxBy(_._2)
        }

        val (id, (minute, _)) = guardMinuteOccurrence.maxBy(_._2._2)

        hash(id, minute)
      }
      .compile
      .last

  def run(args: List[String]): IO[ExitCode] =
    (p1, p2).parTupled
      .flatMap { case (r1, r2) => IO(println(s"Part 1: $r1")) >> IO(println(s"Part 2: $r2")) }
      .as(ExitCode.Success)

}
