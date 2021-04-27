package org.miker

import kotlin.test.assertEquals
import org.junit.jupiter.api.Test
import kotlin.test.assertFalse
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

class `5_MikerStream` {
    @Test
    internal fun toList() {
        val stream = MikerStream.of(1, 2, 3)
        assertEquals(Cons(1, Cons(2, Cons(3, Nil))), stream.toList())
    }

    @Test
    internal fun take() {
        val stream = MikerStream.of(1, 2, 3)
        assertEquals(Cons(1, Cons(2, Nil)), stream.take(2).toList())
    }

    @Test
    internal fun drop() {
        val stream = MikerStream.of(1, 2, 3)
        assertEquals(Cons(2, Cons(3, Nil)), stream.drop(1).toList())
    }

    @Test
    internal fun takeWhile() {
        val stream = MikerStream.of(1, 2, 3)
        assertEquals(Cons(1, Nil), stream.takeWhile { a -> a % 2 != 0 }.toList())
    }

}