package common

object Parser {
  def decode[A](s: String)(implicit D: Decoder[A]): Either[Throwable, A] = D.apply(s)
}
