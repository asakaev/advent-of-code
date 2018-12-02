package tubes

import scala.annotation.tailrec

trait Solver extends GameDef {

  def reverse(step: Step): Step =
    step match {
      case Up    => Down
      case Down  => Up
      case Left  => Right
      case Right => Left
    }

  def newDirection(prev: Option[Step], neighbors: List[(Packet, Step)]): Option[(Packet, Step)] =
    prev match {
      case Some(s) => neighbors.find { case (_, m) => m != reverse(s) }
      case None    => neighbors.headOption
    }

  def sameDirection(prev: Option[Step], neighbors: List[(Packet, Step)]): Option[(Packet, Step)] =
    prev.flatMap { s =>
      neighbors.find { case (_, m) => m == s }
    }

  def next(prev: Option[Step], neighbors: List[(Packet, Step)]): Option[(Packet, Step)] =
    sameDirection(prev, neighbors).orElse(newDirection(prev, neighbors))

  /**
    * Steps are reversed
    */
  def solution(initPos: Pos): List[Step] = {
    @tailrec
    def solutionAux(nextPacket: Packet, steps: List[Step]): List[Step] =
      next(steps.headOption, nextPacket.legalNeighbors) match {
        case Some((p, s)) => solutionAux(p, s +: steps)
        case None         => steps
      }

    solutionAux(Packet(initPos), List.empty[Step])
  }

  /**
    * Cells are reversed
    */
  def traverse(initPos: Pos, steps: List[Step]): Seq[Cell] =
    steps
      .foldLeft((Packet(initPos), Seq.empty[Cell])) { (acc, step) =>
        val (p, res) = acc
        val packetUpdated = step match {
          case Up    => p.up
          case Down  => p.down
          case Left  => p.left
          case Right => p.right
        }

        (packetUpdated, terrain(packetUpdated.pos) +: res)
      }
      ._2

  def collectLetters(cells: Seq[Cell]): String =
    cells.collect { case Letter(l) => l }.mkString

}
