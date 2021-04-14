package org.miker

import kotlin.test.assertEquals
import org.junit.jupiter.api.Test

class `3_ListTest` {
    @Test
    internal fun tail() {
        val list = `3_List`.of(1, 1, 1)
        assertEquals(2, `3_List`.sum(list.tail()))
    }

    @Test
    internal fun setHead() {
        val list = `3_List`.of(1, 1, 1)
        assertEquals(7, `3_List`.sum(list.setHead(5)))
    }

    @Test
    internal fun drop() {
        val list = `3_List`.of(1, 1, 1)
        assertEquals(1, `3_List`.sum(list.drop(2)))
    }

    @Test
    internal fun dropWhile() {
        val list = `3_List`.of(1, 1, 1, 2)
        assertEquals(2, `3_List`.sum(list.dropWhile() { x -> x == 1}))
    }

    @Test
    internal fun append() {
        val l1 = `3_List`.of(1, 1, 1)
        val l2 = `3_List`.of(2, 2, 2)
        assertEquals(9, `3_List`.sum(l1.append(l2)))
    }

    @Test
    internal fun init() {
        val list = `3_List`.of(1, 2, 5)
        assertEquals(3, `3_List`.sum(list.init()))
    }

    @Test
    internal fun foldSum() {
        val list = `3_List`.of(1, 2, 5)
        assertEquals(`3_List`.sum(list), list.sum())
    }

    @Test
    internal fun foldProduct() {
        val list = `3_List`.of(1.0, 2.0, 5.0)
        assertEquals(`3_List`.product(list), list.product())
    }

    @Test
    internal fun length() {
        val list = `3_List`.of(1, 2, 5)
        assertEquals(3, list.length())
    }

    @Test
    internal fun sumL() {
        val list = `3_List`.of(1, 2, 5)
        assertEquals(list.sum(), list.sumL())
    }

    @Test
    internal fun sumR() {
        val list = `3_List`.of(1.0, 2.0, 5.0)
        assertEquals(list.product(), list.productL())
    }

    @Test
    internal fun lengthL() {
        val list = `3_List`.of(1, 2, 5)
        assertEquals(3, list.lengthL())
    }

    @Test
    internal fun stringify() {
        val list = `3_List`.of(1, 2, 5)
        assertEquals("1 2 5", list.stringify())
    }

    @Test
    internal fun reverse() {
        val list = `3_List`.of(1, 2, 5)
        assertEquals("5 2 1", list.reverse().stringify())
    }

    @Test
    internal fun foldLeftR() {
        val list = `3_List`.of(1, 2, 5)
        assertEquals(8, list.foldLeftR(0) {a, b -> a + b})
    }

    @Test
    internal fun foldRightL() {
        val list = `3_List`.of(1, 2, 5)
        assertEquals(8, list.foldRightL(0) {a, b -> a + b})
    }

    @Test
    internal fun appendF() {
        val l1 = `3_List`.of(1, 2, 3)
        val l2 = `3_List`.of(4, 5, 6)
        assertEquals("1 2 3 4 5 6", l1.appendF(l2).stringify())
    }

    @Test
    internal fun concat() {
        val list = `3_List`.of(`3_List`.of(1, 2, 3), `3_List`.of(4, 5, 6))
        assertEquals("1 2 3 4 5 6", `3_List`.concat(list).stringify())
    }

    @Test
    internal fun objConcat() {
        val list = `3_List`.of(`3_List`.of(1, 2, 3), `3_List`.of(4, 5, 6))
        assertEquals("1 2 3 4 5 6", list.concat().stringify())
    }

    @Test
    internal fun itemAppend() {
        val list = `3_List`.of(1, 2, 3)
        assertEquals("1 2 3 4", list.append(4).stringify())
    }

    @Test
    internal fun increment() {
        val list = `3_List`.of(1, 2, 3)
        assertEquals("3 4 5", list.increment(2).stringify())
    }

    @Test
    internal fun tos() {
        val list = `3_List`.of(1, 2, 3)
        assertEquals("1 2 3", list.tos().stringify())
    }

    @Test
    internal fun map() {
        val list = `3_List`.of(1, 2, 3)
        assertEquals("2 3 4", list.map{ a -> a + 1 }.stringify())
    }

    @Test
    internal fun filter() {
        val list = `3_List`.of(1, 2, 3)
        assertEquals("2", list.filter{ a -> a % 2 == 0 }.stringify())
    }

    @Test
    internal fun flapMap() {
        val list = `3_List`.of(1, 2, 3)
        assertEquals("1 1 2 2 3 3", list.flatMap{ a -> `3_List`.of(a, a) }.stringify())
    }

    @Test
    internal fun flatFilter() {
        val list = `3_List`.of(1, 2, 3)
        assertEquals("2", list.flatFilter{ a -> a % 2 == 0 }.stringify())
    }

    @Test
    internal fun merge() {
        val l1 = `3_List`.of(1, 2, 3)
        val l2 = `3_List`.of(4, 5, 6)
        assertEquals("5 7 9", l1.merge(l2).stringify())
    }

    @Test
    internal fun zipWith() {
        val l1 = `3_List`.of(1, 2, 3)
        val l2 = `3_List`.of(4.0, 5.0, 6.0)
        assertEquals("5.0s 7.0s 9.0s", l1.zipWith(l2){ a, b -> (a.toDouble() + b).toString() + "s" }.stringify())
    }
}