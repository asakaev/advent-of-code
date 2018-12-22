package slice

import common.Decoder
import slice.Rectangle.{ Point, Size }

import scala.util.{ Failure, Try }

case class Claim(id: Int, rectangle: Rectangle)

object Claim {

  implicit val claimDecoder: Decoder[Claim] =
    s => {

      def id(s: String) = Try(s.drop(1).toInt)

      def point(s: String) = s.dropRight(1).split(',') match {
        case Array(a, b) =>
          for {
            x <- Try(a.toInt)
            y <- Try(b.toInt)
          } yield Point(x, y)
        case _ => Failure(new Exception(s"Can not decode Point from [$s]"))
      }

      def size(s: String) = s.split('x') match {
        case Array(a, b) =>
          for {
            w <- Try(a.toInt)
            h <- Try(b.toInt)
          } yield Size(w, h)
        case _ => Failure(new Exception(s"Can not decode Rectangle from [$s]"))
      }

      val claimMaybe = s.split(' ') match {
        case Array(a, _, b, c) =>
          for {
            i <- id(a)
            p <- point(b)
            s <- size(c)
          } yield Claim(i, Rectangle(p, s))
        case _ => Failure(new Exception(s"Can not decode Claim from [$s]"))
      }

      claimMaybe.toEither
    }

}
