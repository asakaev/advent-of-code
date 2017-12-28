package particles

import org.scalatest.FunSuite

import scala.io.Source

class ParticlesSuite extends FunSuite with Solver with StringParserPoints {

  val lines     = Source.fromResource("particles-input.txt").getLines
  val particles = parse(lines.toSeq)

  test("manhattan distance") {
    val p1 = Point(3, 0, 0)
    val p2 = Point(4, -1, -2)

    assert(manhattanDistance(p1) == 3)
    assert(manhattanDistance(p2) == 7)
  }

  test("particle update") {
    val s1 = Particle(0, Point(2366, 784, -597), Point(-12, -41, 50), Point(-5, 1, -2))
    val s2 = Particle(0, Point(2349, 744, -549), Point(-17, -40, 48), Point(-5, 1, -2))

    assert(update(s1) == s2)
  }

  test("minimum distance") {
    val ps1 = Seq(
      Particle(0, Point(3, 0, 0), Point(2, 0, 0), Point(-1, 0, 0)),
      Particle(1, Point(4, 0, 0), Point(0, 0, 0), Point(-2, 0, 0))
    )

    val ps2 = Seq(
      Particle(0, Point(4, 0, 0), Point(1, 0, 0), Point(-1, 0, 0)),
      Particle(1, Point(2, 0, 0), Point(-2, 0, 0), Point(-2, 0, 0))
    )

    val ps3 = Seq(
      Particle(0, Point(4, 0, 0), Point(0, 0, 0), Point(-1, 0, 0)),
      Particle(1, Point(-2, 0, 0), Point(-4, 0, 0), Point(-2, 0, 0))
    )

    val ps4 = Seq(
      Particle(0, Point(3, 0, 0), Point(-1, 0, 0), Point(-1, 0, 0)),
      Particle(1, Point(-8, 0, 0), Point(-6, 0, 0), Point(-2, 0, 0))
    )

    assert(minDistance(ps1).id == 0)
    assert(minDistance(ps2).id == 1)
    assert(minDistance(ps3).id == 1)
    assert(minDistance(ps4).id == 0)
  }

  test("closest particle to position <0,0,0>") {
    assert(closest(particles) == 170)
  }

  test("particles left after after all collisions are resolved") {
    assert(collider(particles) == 571)
  }

}
