package passphrases

trait Solver {

  def solution1(xs: Seq[String]): Int =
    xs.count(withoutDuplicates)

  def solution2(xs: Seq[String]): Int =
    xs.count(withoutAnagrams)

  val separator = ' '

  def withoutDuplicates(str: String): Boolean =
    str.split(separator) match {
      case xs if xs.size == xs.toSet.size => true
      case _                              => false
    }

  def withoutAnagrams(str: String): Boolean =
    str.split(separator) match {
      case xs if xs.size == xs.map(_.sorted).toSet.size => true
      case _                                            => false
    }

}
