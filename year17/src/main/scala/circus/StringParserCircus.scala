package circus

import scala.util.{ Failure, Try }

object StringParserCircus {
  import circus.CircusDef.{ Node, Nodes }
  import common.Decoder
  import common.util._

  implicit val nodesDecoder: Decoder[Nodes] = (input: String) => {
    def nodes(xs: Seq[String]) = xs.map(node)

    def node(s: String) = {
      def left(l: String) = l.split(' ').toList match {
        case n :: wS :: Nil => Try(wS.substring(1, wS.length - 1).toInt).map(n -> _)
        case _              => Failure(new Exception(s"NodeLeft.error: $l"))
      }

      def right(r: String) = r.split(", ").toSeq

      s.split(" -> ").toList match {
        case l :: r :: Nil => left(l).map { case (n, w) => Node(n, w, right(r)) }
        case l :: Nil      => left(l).map { case (n, w) => Node(n, w) }
        case _             => Failure(new Exception(s"Node.wrong: $s"))
      }
    }

    (split _).andThen(nodes).andThen(transform).apply(input)
  }

}
