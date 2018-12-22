package slice

import fs2._
import slice.Rectangle.{ Point, Size }

final case class Rectangle(leftTop: Point, size: Size) {
  def area: Stream[Pure, Point] =
    for {
      y <- Stream.emits(leftTop.y until leftTop.y + size.height)
      x <- Stream.emits(leftTop.x until leftTop.x + size.width)
    } yield Point(x, y)
}

object Rectangle {
  final case class Point(x: Int, y: Int)
  final case class Size(width: Int, height: Int)
}
