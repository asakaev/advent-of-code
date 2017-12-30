package checksum

trait StringParserChecksum {
  def parse(ss: Seq[String]): Seq[Seq[Int]] =
    ss.map(_.split(' ').map(_.toInt).toSeq)
}
