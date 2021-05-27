package org.miker.chapter6

import org.junit.jupiter.api.Test
import org.miker.chapter3.Cons
import org.miker.chapter3.MikerList
import org.miker.chapter3.Nil
import kotlin.test.assertEquals

class SimpleRngTest {
    @Test
    internal fun happy() {
        val (n1, r1) = SimpleRng(42).nextInt()
        assertEquals(16159453, n1)

        val (n2, _) = r1.nextInt()
        assertEquals(-1281479697, n2)
    }

    @Test
    internal fun nonNegativeInt() {
        val (n1, r1) = nonNegativeInt(SimpleRng(42))
        assertEquals(16159453, n1)

        val (n2, _) = nonNegativeInt(r1)
        assertEquals(1281479696, n2)
    }

    @Test
    internal fun double() {
        val (d, r1) = double(SimpleRng(42))
        assertEquals(0.007524831686168909, d)
    }

    @Test
    internal fun ints() {
        val (list, _) = ints(3, SimpleRng(42))
        assertEquals(Cons(16159453, Cons(-1281479697, Cons(-340305902, Nil))), list)
    }

    @Test
    internal fun sequence() {
        val r = SimpleRng(42)
        val list = sequence(MikerList.of(unit(1), unit(2), unit(3)))(r).first
        assertEquals(Cons(1, Cons(2, Cons(3, Nil))), list)
    }
}
