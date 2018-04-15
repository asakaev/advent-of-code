package circus

import scala.util.{ Failure, Success, Try }

object StringParserCircus {
  import circus.CircusDef.{ Node, Nodes }
  import common.Decoder

  implicit val nodesDecoder: Decoder[Nodes] = (input: String) => {
    def split(s: String)       = s.split('\n').toSeq
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

  def transform[A](xs: Seq[Try[A]]): Try[Seq[A]] =
    xs.collectFirst { case Failure(e) => e }
      .map(Failure(_))
      .getOrElse(Success(xs.collect { case Success(a) => a }))

}
