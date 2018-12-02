package circus

import circus.RoseTree._
import org.scalatest.FunSuite

class RoseTreeSuite extends FunSuite {

  test("tree fold with numbers") {
    val tree   = Branch(1, Seq(Leaf(2), Leaf(3)))
    val result = fold(tree)(identity)((x, xs) => (x +: xs).sum)
    assert(result == 6)
  }

  test("tree with empty forest fold with numbers") {
    val tree   = Branch(1, Seq.empty)
    val result = fold(tree)(identity)((x, xs) => (x +: xs).sum)
    assert(result == 1)
  }

  test("tree fold with letters") {
    val tree   = Branch('A', Seq(Leaf('B'), Leaf('C')))
    val result = fold(tree)(_.toString)((x, xs) => (x +: xs).mkString)
    assert(result == "ABC")
  }

}
