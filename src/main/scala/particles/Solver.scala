package particles

import scala.annotation.tailrec

trait Solver extends ParticlesDef {

  def closest(ps: Seq[Particle]): Int = {
    @tailrec
    def solutionAux(ps: Seq[Particle], n: Int): Int =
      if (n <= 0) minDistance(ps).id
      else solutionAux(ps.map(update), n - 1)

    val ticks = maxAcceleration(ps) * 5
    solutionAux(ps, ticks)
  }

  def collider(ps: Seq[Particle]): Int = {
    @tailrec
    def colliderAux(ps: Seq[Particle], n: Int): Int =
      if (n <= 0) ps.size
      else colliderAux(uniquePosition(ps.map(update)), n - 1)

    val ticks = (maxAcceleration(ps) * 0.6).round.toInt
    colliderAux(ps, ticks)
  }

  def uniquePosition(particles: Seq[Particle]): Seq[Particle] =
    particles
      .groupBy(_.position)
      .collect { case (_, ps) if ps.size == 1 => ps.head }
      .toSeq

  def minDistance(ps: Seq[Particle]): Particle =
    ps.minBy(p => manhattanDistance(p.position))

  def manhattanDistance(p: Point): Int =
    Seq(p.x, p.y, p.z).map(_.abs).sum

  def maxAcceleration(ps: Seq[Particle]): Int =
    ps.map(_.acceleration).map(manhattanDistance).max

}
