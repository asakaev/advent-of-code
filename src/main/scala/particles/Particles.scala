package particles

import scala.io.Source

object Particles extends App with StringParserPoints with Solver {
  val lines     = Source.fromResource("particles-input.txt").getLines
  val particles = parse(lines.toSeq)
  println(s"Closest particle to position <0,0,0>: ${closest(particles)}")
  println(s"Particles are left after all collisions are resolved: ${collider(particles)}")
}
