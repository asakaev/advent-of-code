package slice

import org.scalatest.FunSuite
import slice.Rectangle.{ Point, Size }

class RectangleSuite extends FunSuite {

  test("area 1") {
    val r = Rectangle(Point(1, 1), Size(1, 1))
    assert(r.area.compile.toList == List(Point(1, 1)))
  }

  test("area 2") {
    val r = Rectangle(Point(1, 2), Size(2, 3))

    val expected = List(Point(1, 2), Point(2, 2), Point(1, 3), Point(2, 3), Point(1, 4), Point(2, 4))
    assert(r.area.compile.toList == expected)
  }

}
