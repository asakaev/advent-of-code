package captcha

import org.scalatest.FunSuite

class CaptchaSuite extends FunSuite with Solver {
  test("next digit") {
    assert(nextDigit(1, 1, 2, 2) == 3)
    assert(nextDigit(1, 1, 1, 1) == 4)
    assert(nextDigit(1, 2, 3, 4) == 0)
    assert(nextDigit(9, 1, 2, 1, 2, 1, 2, 9) == 9)
  }

  test("halfway around") {
    assert(halfwayAround(1, 2, 1, 2) == 6)
    assert(halfwayAround(1, 2, 2, 1) == 0)
    assert(halfwayAround(1, 2, 3, 4, 2, 5) == 4)
    assert(halfwayAround(1, 2, 3, 1, 2, 3) == 12)
    assert(halfwayAround(1, 2, 1, 3, 1, 4, 1, 5) == 4)
  }
}
