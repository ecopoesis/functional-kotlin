package org.miker

import kotlin.test.assertEquals
import org.junit.jupiter.api.Test
import org.miker.chapter2.Part1
import org.miker.chapter2.Part2
import kotlin.test.assertEquals

class Part2Test() {
    @Test
    internal fun sorted() {
        assertEquals(true, Part2.isSorted(listOf(1, 2, 3, 4)) { a, b -> a < b })
    }

    @Test
    internal fun notSorted() {
        assertEquals(false, Part2.isSorted(listOf(5, 2, 3, 4)) { a, b -> a < b })
    }
}
