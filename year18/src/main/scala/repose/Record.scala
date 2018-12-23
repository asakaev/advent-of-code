package repose

import java.time.{ Instant, LocalDateTime, ZoneOffset }

import cats.Order
import cats.implicits._
import common.Decoder
import fastparse.NoWhitespace._
import fastparse._
import repose.Record.GuardEvent

final case class Record(t: Instant, e: GuardEvent)

object Record {

  sealed trait GuardEvent
  case class BeginsShift(id: Int) extends GuardEvent
  case object FallsAsleep         extends GuardEvent
  case object WakesUp             extends GuardEvent

  object Parser {
    def num4[_: P] = P(CharIn("0-9").rep(4).!.map(_.toInt))
    def num2[_: P] = P(CharIn("0-9").rep(2).!.map(_.toInt))

    def time[_: P] = P(num4 ~ "-" ~ num2 ~ "-" ~ num2 ~ " " ~ num2 ~ ":" ~ num2).map {
      case (y, m, d, h, mm) => LocalDateTime.of(y, m, d, h, mm).atOffset(ZoneOffset.UTC).toInstant
    }

    def beginsShift[_: P] = P("Guard #" ~ CharsWhileIn("0-9").!.map(_.toInt).map(BeginsShift))
    def wakesUp[_: P]     = P("w").map(_ => WakesUp)
    def fallsAsleep[_: P] = P("f").map(_ => FallsAsleep)

    def guardEvent[_: P] = P(beginsShift | wakesUp | fallsAsleep)

    def record[_: P] = P("[" ~ time ~ "]" ~ " " ~ guardEvent).map { case (t, ev) => Record(t, ev) }
  }

  implicit val decodeRecord: Decoder[Record] = { input =>
    parse(input, Record.Parser.record(_)).fold(
      (_, _, _) => Left(new Exception(s"Can not decode Record from [$input]")),
      (record, _) => Right(record)
    )
  }

  implicit val instantOrder: Order[Instant] = Order.by(_.getEpochSecond)
  implicit val recordOrder: Order[Record]   = Order.by(_.t)

}
