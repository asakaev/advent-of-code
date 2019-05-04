package repose

import java.time.Duration

import cats.Eq
import cats.kernel.laws.discipline.MonoidTests
import cats.tests.CatsSuite
import org.scalacheck.{Arbitrary, Gen}
import repose.DurationLawTests._
import repose.instances._

// TODO: fix flaky test
class DurationLawTests extends CatsSuite {
  checkAll("Duration.MonoidLaws", MonoidTests[Duration].monoid)
}

object DurationLawTests {

  implicit lazy val arbDuration: Arbitrary[Duration] =
    Arbitrary(Gen.chooseNum(Long.MinValue, Long.MaxValue).map(_ / 4).map(Duration.ofSeconds))

  implicit lazy val eqDuration: Eq[Duration] = Eq.fromUniversalEquals

}
