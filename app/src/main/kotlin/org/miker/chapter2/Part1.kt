package org.miker.chapter2

object Part1 {
    fun fib(i: Int): Int {
        tailrec fun loop(n: Int, a: Int = 0, b: Int = 1): Int =
            when (n) {
                0 -> a
                1 -> b
                else -> loop(n - 1, b, b + a)
            }

        return loop(i-1)
    }
}
