package org.miker

import kotlin.test.assertEquals
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class ScratchTest() {
    @Test
    internal fun lazy() {
        val foo = Scratch.factory()
        assertEquals("foo", foo.value)
        assertEquals("foo", foo.value)
    }
}