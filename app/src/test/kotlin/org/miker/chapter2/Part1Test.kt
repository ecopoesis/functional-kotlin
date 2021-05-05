package org.miker

import kotlin.test.assertEquals
import org.junit.jupiter.api.Test
import org.miker.chapter2.Part1
import kotlin.test.assertEquals

class Part1Test() {
    @Test
    internal fun fib1() {
        assertEquals(0, Part1.fib(1))
    }

    @Test
    internal fun fib5() {
        assertEquals(3, Part1.fib(5))
    }
}
