package circus

import circus.CircusDef.Nodes
import common.Decoder.decode

import scala.io.Source

object Circus extends App with Solver {
  import StringParserCircus.nodesDecoder

  val input = Source.fromResource("circus-input.txt").mkString
  val nodes = decode[Nodes](input).get

  println(s"bottom node: ${solution(nodes)}")
  println(s"weight need to be to balance: ${solution2(nodes)}")
}
