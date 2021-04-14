package org.miker

import kotlin.test.assertEquals
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class `2_2Test`() {
    @Test
    internal fun sorted() {
        assertEquals(true, `2_2`.isSorted(listOf(1, 2, 3, 4)) { a, b -> a < b })
    }

    @Test
    internal fun notSorted() {
        assertEquals(false, `2_2`.isSorted(listOf(5, 2, 3, 4)) { a, b -> a < b })
    }
}