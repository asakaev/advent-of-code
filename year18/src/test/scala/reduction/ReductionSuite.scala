package reduction

import org.scalatest.FunSuite

class ReductionSuite extends FunSuite {

  test("empty input") {
    assert(scan("") == "")
  }

  test("one symbol") {
    assert(scan("a") == "a")
  }

  test("triggered1") {
    assert(scan("cC") == "")
  }

  test("triggered2") {
    assert(scan("cCb") == "b")
  }

  test("triggered3") {
    assert(scan("bcC") == "b")
  }

  test("not triggered") {
    assert(scan("cA") == "cA")
  }

  test("input1") {
    assert(scan("dabAcCaCBAcCcaDA") == "dabCBAcaDA")
  }

  test("units") {
    assert(unitsRemain("dabAcCaCBAcCcaDA") == 10)
  }

  test("alpha 1") {
    assert(alpha("abc") == Set('a', 'b', 'c'))
  }

  test("alpha 2") {
    assert(alpha("aAbcD") == Set('a', 'b', 'c', 'd'))
  }

  test("remove 1") {
    assert(remove('a', "aAbcD") == "bcD")
  }
}
