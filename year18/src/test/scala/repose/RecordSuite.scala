package repose

import java.time.Instant

import fastparse._
import org.scalatest.FunSuite
import repose.Record.{ BeginsShift, FallsAsleep, WakesUp }

class RecordSuite extends FunSuite {

  test("parser 1") {
    val s        = "[1518-11-12 00:00] Guard #3011 begins shift"
    val expected = Parsed.Success(Record(Instant.parse("1518-11-12T00:00:00Z"), BeginsShift(3011)), 30)
    assert(parse(s, Record.Parser.record(_)) == expected)
  }

  test("parser 2") {
    val s        = "[1518-11-12 00:30] falls asleep"
    val expected = Parsed.Success(Record(Instant.parse("1518-11-12T00:30:00Z"), FallsAsleep), 20)
    assert(parse(s, Record.Parser.record(_)) == expected)
  }

  test("parser 3") {
    val s        = "[1518-09-24 00:59] wakes up"
    val expected = Parsed.Success(Record(Instant.parse("1518-09-24T00:59:00Z"), WakesUp), 20)
    assert(parse(s, Record.Parser.record(_)) == expected)
  }

  test("decoder 1") {
    val s = "[1518-11-12 00:00] Guard #3011 begins shift"
    assert(Record.decodeRecord(s).isRight)
  }

  test("decoder 2") {
    val s = "[1518-11-12 00:00] Guard 3011 begins shift"
    assert(Record.decodeRecord(s).isLeft)
  }

}
