package checksum

trait Solver {
  def solution(spreadsheet: Seq[Seq[Int]]): Int =
    spreadsheet.map(xs => xs.max - xs.min).sum

  def evenlyDivisible(spreadsheet: Seq[Seq[Int]]): Int =
    spreadsheet.map { xs =>
      xs.combinations(2)
        .toList
        .map(_.sorted.toList)
        .collectFirst { case a :: b :: Nil if b % a == 0 => b / a }
        .getOrElse(0)
    }.sum

}
