package reallocation

trait ReallocationDef {

  type MemoryBanks = Seq[Int]

  def redistribute(mbs: MemoryBanks): MemoryBanks = {
    val (max, maxIdx) = mbs.zipWithIndex.maxBy { case (x, _) => x }
    val indices       = (1 to max).map(i => (i + maxIdx) % mbs.size)
    indices.foldLeft(mbs.updated(maxIdx, 0)) { (mb, i) =>
      mb.updated(i, mb(i) + 1)
    }
  }

}
