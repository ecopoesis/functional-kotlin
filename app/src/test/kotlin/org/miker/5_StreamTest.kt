package org.miker

import kotlin.test.assertEquals
import org.junit.jupiter.api.Test
import kotlin.test.assertTrue

class `5_StreamTest` {
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

    @Test
    internal fun reverse() {
        val stream = MikerStream.of(1, 2, 3)
        assertEquals(Cons(3, Cons(2, Cons(1, Nil))), stream.reverse().toList())
    }

    @Test
    internal fun forAll() {
        val stream = MikerStream.of(2, 4, 8)
        assertTrue(stream.forAll { a -> a % 2 == 0 })
    }

    @Test
    internal fun takeWhileF() {
        val stream = MikerStream.of(1, 2, 3)
        assertEquals(Cons(1, Nil), stream.takeWhileF { a -> a % 2 != 0 }.toList())
    }

    @Test
    internal fun headOption() {
        val stream = MikerStream.of(2, 4, 8)
        assertEquals(Some(2), stream.headOption())
    }

    @Test
    internal fun map() {
        val stream = MikerStream.of(1, 2, 3)
        assertEquals(Cons(1, Cons(4, Cons(9, Nil))), stream.map{ a -> a * a }.toList())
    }

    @Test
    internal fun filter() {
        val stream = MikerStream.of(1, 2, 3)
        assertEquals(Cons(2, Nil), stream.filter{ a -> a % 2 == 0 }.toList())
    }

    @Test
    internal fun append() {
        val s1 = MikerStream.of(1, 2)
        val s2 = MikerStream.of(4, 5)
        assertEquals(Cons(1, Cons(2, Cons(4, Cons(5, Nil)))), s1.append({ s2 }).toList())
    }

    @Test
    internal fun onesExists() {
        assertTrue(MikerStream.ones().exists { it % 2 != 0 })
    }

    @Test
    internal fun constant() {
        assertTrue(MikerStream.constant(2).exists { it == 2 })
    }

    @Test
    internal fun from() {
        assertEquals(MikerStream.of(2, 3, 4).toList(), MikerStream.from(2).take(3).toList())
    }

    @Test
    internal fun fibs() {
        assertEquals(MikerStream.of(0, 1, 1, 2, 3, 5, 8, 13).toList(), MikerStream.fibs().take(8).toList())
    }

    @Test
    internal fun fibsUnfold() {
        assertEquals(MikerStream.of(0, 1, 1, 2, 3, 5, 8, 13).toList(), MikerStream.fibsUnfold().take(8).toList())
    }

    @Test
    internal fun fromUnfold() {
        assertEquals(MikerStream.of(2, 3, 4).toList(), MikerStream.fromUnfold(2).take(3).toList())
    }

    @Test
    internal fun constantUnfold() {
        assertEquals(MikerStream.of(2, 2, 2).toList(), MikerStream.constantUnfold(2).take(3).toList())
    }

    @Test
    internal fun onesU() {
        assertEquals(MikerStream.of(1, 1, 1).toList(), MikerStream.onesU().take(3).toList())
    }

    @Test
    internal fun mapU() {
        val stream = MikerStream.of(1, 2, 3)
        assertEquals(Cons(1, Cons(4, Cons(9, Nil))), stream.mapU{ a -> a * a }.toList())
    }

    @Test
    internal fun takeU() {
        val stream = MikerStream.of(1, 2, 3)
        assertEquals(Cons(1, Cons(2, Nil)), stream.takeU(2).toList())
    }

    @Test
    internal fun takeWhileU() {
        val stream = MikerStream.of(1, 2, 3)
        assertEquals(Cons(1, Nil), stream.takeWhileU { a -> a % 2 != 0 }.toList())
    }

    @Test
    internal fun zipWith() {
        val s1 = MikerStream.of(1, 2, 3)
        val s2 = MikerStream.of(4.0, 5.0, 6.0)
        assertEquals("5.0s 7.0s 9.0s", s1.zipWith(s2){ a, b -> (a.toDouble() + b).toString() + "s" }.toList().stringify())
    }
}