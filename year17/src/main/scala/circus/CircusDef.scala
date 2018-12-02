package circus

import java.lang.Math.abs

object CircusDef {
  import circus.RoseTree._

  final case class Node(name: String, weight: Int, forest: Seq[String] = Seq.empty)

  type Weight = Int
  type Nodes  = Seq[Node]

  sealed trait Balance
  final case class Balanced(total: Weight, parent: Option[Weight] = None) extends Balance
  final case class Unbalanced(rootDiff: Weight)                           extends Balance

  def imbalance(t: RoseTree[Int]): Option[Int] =
    fold[Weight, Balance](t)(Balanced(_))(balance) match {
      case Unbalanced(diff) => Some(diff)
      case _                => None
    }

  def balance(root: Weight, forest: Seq[Balance]): Balance = {

    def different(bs: Seq[Balanced]) =
      bs.groupBy(_.total).values.collectFirst { case xs if xs.size == 1 => xs.head }

    def partition(xs: Seq[Balance]) =
      xs.foldLeft((List.empty[Balanced], Option.empty[Unbalanced])) {
        case ((bs, o), b: Balanced)   => (b :: bs) -> o
        case ((bs, _), u: Unbalanced) => bs        -> Some(u)
      }

    val (balancedSeq, maybeUnbalanced) = partition(forest)
    (balancedSeq.map(_.total), maybeUnbalanced, different(balancedSeq)) match {
      case (_, Some(unbalanced), _)             => unbalanced
      case (ts, _, _) if ts.toSet.size == 1     => Balanced(root + ts.sum, Some(root))
      case (ts, _, Some(Balanced(_, Some(pw)))) => Unbalanced(abs(pw - (ts.max - ts.min)))
      case (ts, _, _)                           => Unbalanced(abs(root - (ts.max - ts.min)))
    }
  }

  def tree[A](xs: Seq[Node])(f: Node => A): Option[RoseTree[A]] = {
    def root(xs: Nodes, name: String) = !xs.exists { _.forest.contains(name) }
    def find(xs: Nodes, name: String) = xs.find(_.name == name)

    def treeAux(ns: Nodes)(n: Node): RoseTree[A] =
      n.forest.flatMap(find(ns, _)).map(treeAux(ns)(_)) match {
        case Nil => Leaf(f(n))
        case ts  => Branch(f(n), ts)
      }

    xs.find(n => root(xs, n.name)).map(treeAux(xs)(_))
  }

}
