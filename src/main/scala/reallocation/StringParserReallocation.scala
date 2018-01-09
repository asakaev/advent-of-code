package reallocation

trait StringParserReallocation {
  def parse(str: String): Seq[Int] = str.split(' ').map(_.toInt)
}
