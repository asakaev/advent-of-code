package reallocation

import scala.annotation.tailrec

trait Solver extends ReallocationDef {

  case class Result(alreadySeenState: MemoryBanks, cycles: Int)

  /**
    * Redistribution cycles completed before a configuration is produced that has been seen before
    */
  def solution1(mbs: MemoryBanks): Int =
    reallocate(mbs).cycles

  /**
    * Cycles are in the infinite loop that arises from the configuration in puzzle input
    */
  def solution2(mbs: MemoryBanks): Int =
    routine(reallocate(mbs).alreadySeenState) { case (history, _) => history }.cycles

  def reallocate(mbs: MemoryBanks): Result =
    routine(mbs) { case (history, reallocated) => history :+ reallocated }

  def routine(mbs: MemoryBanks)(update: (Seq[MemoryBanks], MemoryBanks) => Seq[MemoryBanks]): Result = {
    @tailrec def loop(history: Seq[MemoryBanks], mbs: MemoryBanks, cycles: Int): Result =
      redistribute(mbs) match {
        case xs if history.contains(xs) => Result(xs, cycles)
        case xs                         => loop(update(history, xs), xs, cycles + 1)
      }

    loop(Seq(mbs), mbs, 1)
  }

}
