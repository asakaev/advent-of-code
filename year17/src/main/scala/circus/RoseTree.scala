package circus

sealed trait RoseTree[T] { val value: T }

object RoseTree {
  final case class Branch[T](value: T, forest: Seq[RoseTree[T]]) extends RoseTree[T]
  final case class Leaf[T](value: T)                             extends RoseTree[T]

  def fold[A, B](t: RoseTree[A])(f: A => B)(g: (A, Seq[B]) => B): B = t match {
    case Leaf(v)           => f(v)
    case Branch(v, forest) => g(v, forest.map(fold(_)(f)(g)))
  }
}
