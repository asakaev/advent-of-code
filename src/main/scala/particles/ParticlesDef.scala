package particles

trait ParticlesDef {

  case class Point(x: Int, y: Int, z: Int)
  case class Particle(id: Int, position: Point, velocity: Point, acceleration: Point)

  def update(p: Particle): Particle = {
    val velocityUpdated = Point(
      p.velocity.x + p.acceleration.x,
      p.velocity.y + p.acceleration.y,
      p.velocity.z + p.acceleration.z
    )

    val positionUpdated = Point(
      p.position.x + velocityUpdated.x,
      p.position.y + velocityUpdated.y,
      p.position.z + velocityUpdated.z
    )

    p.copy(velocity = velocityUpdated, position = positionUpdated)
  }

}
