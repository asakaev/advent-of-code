import scala.annotation.tailrec

package object reduction {

  def sameType(l: Char, r: Char): Boolean =
    l.toLower == r.toLower

  def oppositePolarity(l: Char, r: Char): Boolean =
    (l.isLower && !r.isLower) || (!l.isLower && r.isLower)

  def triggered(l: Char, r: Char): Boolean =
    sameType(l, r) && oppositePolarity(l, r)

  case class State(out: String, in: String)

  def scan(s: String): String = {
    @tailrec def scanAux(s: State): State =
      if (s.in.length < 2) State(s.out + s.in, "")
      else if (triggered(s.in(0), s.in(1))) scanAux(State(s.out.dropRight(1), s.out.takeRight(1) + s.in.drop(2)))
      else scanAux(State(s.out + s.in(0), s.in.drop(1)))

    scanAux(State("", s)).out
  }

  def unitsRemain(s: String): Int =
    scan(s).length

  def alpha(s: String): Set[Char] =
    s.map(_.toLower).toSet

  def remove(c: Char, s: String): String =
    s.filter(_.toLower != c)

  def shortestPolymer(s: String): Int =
    alpha(s).map(remove(_, s)).map(unitsRemain).min

}
