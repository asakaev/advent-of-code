package common

trait Decoder[A] {
  def apply(s: String): Either[Throwable, A]
}
