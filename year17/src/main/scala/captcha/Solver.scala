package captcha

trait Solver {
  def nextDigit(digits: Int*): Int     = solution(digits, 1)
  def halfwayAround(digits: Int*): Int = solution(digits, digits.size / 2)

  def solution(xs: Seq[Int], pairIndex: Int): Int =
    xs.indices
      .map(i => xs(i) -> xs((i + pairIndex) % xs.size))
      .collect { case (a, b) if a == b => a }
      .groupBy(identity)
      .map { case (n, ns) => n * ns.size }
      .sum

}
