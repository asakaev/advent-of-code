package reallocation

trait StringParserReallocation {
  def parse(str: String): List[Int] = str.split(' ').toList.map(_.toInt)
}
