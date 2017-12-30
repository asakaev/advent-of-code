package captcha

trait StringParserCaptcha {
  def parse(s: String): Seq[Int] = s.map(_.toString.toInt)
}
