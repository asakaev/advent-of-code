package trampolines

trait StringParserTrampolines {
  def parse(str: Vector[String]): Vector[Int] = str.map(_.toInt)
}
