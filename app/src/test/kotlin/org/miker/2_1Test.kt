package org.miker

import kotlin.test.assertEquals
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class `2_1Test`() {
    @Test
    internal fun fib1() {
        assertEquals(0, `2_1`.fib(1))
    }

    @Test
    internal fun fib5() {
        assertEquals(3, `2_1`.fib(5))
    }
}