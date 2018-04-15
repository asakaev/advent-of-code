package circus

trait Solver {
  import CircusDef._

  def solution(nodes: Nodes): String  = tree(nodes)(_.name).map(_.value).get
  def solution2(nodes: Nodes): Weight = tree(nodes)(_.weight).flatMap(imbalance).get
}
