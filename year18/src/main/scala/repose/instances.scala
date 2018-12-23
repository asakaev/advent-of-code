package repose

import java.time.Duration

import cats.Monoid

object instances {

  implicit val durationAdditionMonoid: Monoid[Duration] = new Monoid[Duration] {
    def empty                             = Duration.ZERO
    def combine(x: Duration, y: Duration) = x.plus(y)
  }

}
