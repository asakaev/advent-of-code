package common

import scala.util.{ Failure, Success, Try }

package object util {

  def transform[A](xs: Seq[Try[A]]): Try[Seq[A]] =
    xs.collectFirst { case Failure(e) => e }
      .map(Failure(_))
      .getOrElse(Success(xs.collect { case Success(a) => a }))

  def split(s: String): Seq[String] = s.split('\n').toSeq

}
