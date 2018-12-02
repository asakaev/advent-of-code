package circus

import circus.CircusDef._
import circus.RoseTree._
import common.Decoder.decode
import org.scalatest.FunSuite

import scala.io.Source

class CircusSuite extends FunSuite with Solver {
  import StringParserCircus.nodesDecoder

  val inputExample: String = Source.fromResource("circus-example.txt").mkString
  val input: String        = Source.fromResource("circus-input.txt").mkString

  val nodesExample = Seq(
    Node("pbga", 66),
    Node("xhth", 57),
    Node("ebii", 61),
    Node("havc", 66),
    Node("ktlj", 57),
    Node("fwft", 72, Seq("ktlj", "cntj", "xhth")),
    Node("qoyq", 66),
    Node("padx", 45, Seq("pbga", "havc", "qoyq")),
    Node("tknk", 41, Seq("ugml", "padx", "fwft")),
    Node("jptl", 61),
    Node("ugml", 68, Seq("gyxo", "ebii", "jptl")),
    Node("gyxo", 61),
    Node("cntj", 57)
  )

  val nodes: Nodes = decode[Nodes](input).get

  test("parse") {
    assert(decode[Nodes](inputExample).get == nodesExample)
  }

  test("tree builder") {
    val expected = Branch(
      "tknk",
      List(
        Branch("ugml", List(Leaf("gyxo"), Leaf("ebii"), Leaf("jptl"))),
        Branch("padx", List(Leaf("pbga"), Leaf("havc"), Leaf("qoyq"))),
        Branch("fwft", List(Leaf("ktlj"), Leaf("cntj"), Leaf("xhth")))
      )
    )

    val maybeTree = tree(nodesExample)(_.name)
    assert(maybeTree.contains(expected))
  }

  test("imbalance: balanced") {
    val tree = Branch(100, Seq(Leaf(300), Leaf(300)))
    assert(imbalance(tree).isEmpty)
  }

  test("imbalance: unbalanced") {
    val tree = Branch(100, Seq(Leaf(350), Leaf(200), Leaf(350)))
    assert(imbalance(tree).get == 50)
  }

  test("imbalance: balanced inside") {
    val tree = Branch(100, Seq(Leaf(200), Branch(100, Seq(Leaf(50), Leaf(50)))))
    assert(imbalance(tree).isEmpty)
  }

  test("imbalance: unbalanced inside") {
    val tree = Branch(100, Seq(Leaf(200), Branch(100, Seq(Leaf(70), Leaf(50), Leaf(70))), Leaf(200)))
    assert(imbalance(tree).get == 80)
  }

  test("balance: unbalanced weight") {
    val tree   = Branch(100, Seq(Leaf(200), Leaf(10), Leaf(200)))
    val result = fold[Int, Balance](tree)(Balanced(_))(balance)
    assert(result == Unbalanced(90))
  }

  test("solution1 example") {
    assert(solution(nodesExample) == "tknk")
  }

  test("solution1") {
    assert(solution(nodes) == "gynfwly")
  }

  test("solution2 example") {
    assert(solution2(nodesExample) == 60)
  }

  test("solution2") {
    assert(solution2(nodes) == 1526)
  }

}
