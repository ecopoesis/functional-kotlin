package org.miker.chapter6

data class SimpleRng(val seed: Long) : Rng {
    override fun nextInt(): Pair<Int, Rng> {
        val newSeed = (seed * 0x5DEECE66DL + 0xBL) and 0xFFFFFFFFFFFFL
        val nextRNG = SimpleRng(newSeed)
        val n = (newSeed ushr 16).toInt()
        return Pair(n, nextRNG)
    }
}
