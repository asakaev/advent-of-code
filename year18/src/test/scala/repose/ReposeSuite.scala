package repose

import java.time.Instant

import org.scalatest.FunSuite
import repose.Record.{ BeginsShift, FallsAsleep, WakesUp }
import repose.Repose.{ spans, Span }

class ReposeSuite extends FunSuite {

  val id = 1

  test("spans 1") {
    val t1 = Instant.ofEpochSecond(1)
    val t2 = Instant.ofEpochSecond(3)
    val t3 = Instant.ofEpochSecond(5)

    val rs = List(
      Record(t1, BeginsShift(id)),
      Record(t2, FallsAsleep),
      Record(t3, WakesUp)
    )

    assert(spans(id, rs) == List(Span(t2, t3)))
  }

  test("spans 2") {
    val t1 = Instant.ofEpochSecond(1)
    val t2 = Instant.ofEpochSecond(3)
    val t3 = Instant.ofEpochSecond(5)

    val rs = List(
      Record(t1, BeginsShift(id)),
      Record(t2, FallsAsleep),
      Record(t3, BeginsShift(42))
    )

    assert(spans(id, rs) == List(Span(t2, t3)))
  }

  test("spans 3") {
    val t1 = Instant.ofEpochSecond(1)
    val t2 = Instant.ofEpochSecond(3)
    val t3 = Instant.ofEpochSecond(5)

    val t4 = Instant.ofEpochSecond(10)
    val t5 = Instant.ofEpochSecond(15)

    val rs = List(
      Record(t1, BeginsShift(id)),
      Record(t2, FallsAsleep),
      Record(t3, BeginsShift(42)),
      Record(t4, FallsAsleep),
      Record(t5, WakesUp),
    )

    assert(spans(id, rs) == List(Span(t2, t3)))
  }

}
