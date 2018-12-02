package common

import scala.util.Try

trait Decoder[A] {
  def apply(s: String): Try[A]
}

object Decoder {
  def decode[A: Decoder](s: String): Try[A] = implicitly[Decoder[A]].apply(s)
}
