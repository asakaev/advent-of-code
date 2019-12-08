package particles

trait StringParserPoints extends ParticlesDef {

  def parse(lines: Seq[String]): Seq[Particle] =
    lines.zipWithIndex.map {
      case (s, n) => parseLine(s, n)
    }

  private def parseLine(s: String, n: Int): Particle =
    particle(n, parts(s).map(numbers).map(point))

  private def particle(id: Int, points: Seq[Point]): Particle =
    points.toList match {
      case p :: v :: a :: Nil => Particle(id, p, v, a)
      case _                  => throw new Exception(s"Particle.none: $points")
    }

  private def parts(s: String): List[String] =
    s.split(", ").toList

  private def numbers(s: String): String =
    s.substring(3, s.length - 1)

  private def point(s: String): Point =
    s.split(',').map(_.trim).map(_.toInt).toList match {
      case x :: y :: z :: Nil => Point(x, y, z)
      case _                  => throw new Exception(s"Point.none: [$s]")
    }

}
