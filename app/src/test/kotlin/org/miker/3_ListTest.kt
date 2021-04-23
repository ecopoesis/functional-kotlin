package org.miker

import kotlin.test.assertEquals
import org.junit.jupiter.api.Test
import kotlin.test.assertFalse
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

class `3_ListTest` {
    @Test
    internal fun tail() {
        val list = MikerList.of(1, 1, 1)
        assertEquals(2, MikerList.sum(list.tail()))
    }

    @Test
    internal fun setHead() {
        val list = MikerList.of(1, 1, 1)
        assertEquals(7, MikerList.sum(list.setHead(5)))
    }

    @Test
    internal fun drop() {
        val list = MikerList.of(1, 1, 1)
        assertEquals(1, MikerList.sum(list.drop(2)))
    }

    @Test
    internal fun dropWhile() {
        val list = MikerList.of(1, 1, 1, 2)
        assertEquals(2, MikerList.sum(list.dropWhile() { x -> x == 1}))
    }

    @Test
    internal fun append() {
        val l1 = MikerList.of(1, 1, 1)
        val l2 = MikerList.of(2, 2, 2)
        assertEquals(9, MikerList.sum(l1.append(l2)))
    }

    @Test
    internal fun init() {
        val list = MikerList.of(1, 2, 5)
        assertEquals(3, MikerList.sum(list.init()))
    }

    @Test
    internal fun foldSum() {
        val list = MikerList.of(1, 2, 5)
        assertEquals(MikerList.sum(list), list.sum())
    }

    @Test
    internal fun foldProduct() {
        val list = MikerList.of(1.0, 2.0, 5.0)
        assertEquals(MikerList.product(list), list.product())
    }

    @Test
    internal fun length() {
        val list = MikerList.of(1, 2, 5)
        assertEquals(3, list.length())
    }

    @Test
    internal fun sumL() {
        val list = MikerList.of(1, 2, 5)
        assertEquals(list.sum(), list.sumL())
    }

    @Test
    internal fun sumR() {
        val list = MikerList.of(1.0, 2.0, 5.0)
        assertEquals(list.product(), list.productL())
    }

    @Test
    internal fun lengthL() {
        val list = MikerList.of(1, 2, 5)
        assertEquals(3, list.lengthL())
    }

    @Test
    internal fun stringify() {
        val list = MikerList.of(1, 2, 5)
        assertEquals("1 2 5", list.stringify())
    }

    @Test
    internal fun reverse() {
        val list = MikerList.of(1, 2, 5)
        assertEquals("5 2 1", list.reverse().stringify())
    }

    @Test
    internal fun foldLeftR() {
        val list = MikerList.of(1, 2, 5)
        assertEquals(8, list.foldLeftR(0) {a, b -> a + b})
    }

    @Test
    internal fun foldRightL() {
        val list = MikerList.of(1, 2, 5)
        assertEquals(8, list.foldRightL(0) {a, b -> a + b})
    }

    @Test
    internal fun appendF() {
        val l1 = MikerList.of(1, 2, 3)
        val l2 = MikerList.of(4, 5, 6)
        assertEquals("1 2 3 4 5 6", l1.appendF(l2).stringify())
    }

    @Test
    internal fun concat() {
        val list = MikerList.of(MikerList.of(1, 2, 3), MikerList.of(4, 5, 6))
        assertEquals("1 2 3 4 5 6", MikerList.concat(list).stringify())
    }

    @Test
    internal fun objConcat() {
        val list = MikerList.of(MikerList.of(1, 2, 3), MikerList.of(4, 5, 6))
        assertEquals("1 2 3 4 5 6", list.concat().stringify())
    }

    @Test
    internal fun itemAppend() {
        val list = MikerList.of(1, 2, 3)
        assertEquals("1 2 3 4", list.append(4).stringify())
    }

    @Test
    internal fun increment() {
        val list = MikerList.of(1, 2, 3)
        assertEquals("3 4 5", list.increment(2).stringify())
    }

    @Test
    internal fun tos() {
        val list = MikerList.of(1, 2, 3)
        assertEquals("1 2 3", list.tos().stringify())
    }

    @Test
    internal fun map() {
        val list = MikerList.of(1, 2, 3)
        assertEquals("2 3 4", list.map{ a -> a + 1 }.stringify())
    }

    @Test
    internal fun filter() {
        val list = MikerList.of(1, 2, 3)
        assertEquals("2", list.filter{ a -> a % 2 == 0 }.stringify())
    }

    @Test
    internal fun flatMap() {
        val list = MikerList.of(1, 2, 3)
        assertEquals("1 1 2 2 3 3", list.flatMap{ a -> MikerList.of(a, a) }.stringify())
    }

    @Test
    internal fun flatFilter() {
        val list = MikerList.of(1, 2, 3)
        assertEquals("2", list.flatFilter{ a -> a % 2 == 0 }.stringify())
    }

    @Test
    internal fun merge() {
        val l1 = MikerList.of(1, 2, 3)
        val l2 = MikerList.of(4, 5, 6)
        assertEquals("5 7 9", l1.merge(l2).stringify())
    }

    @Test
    internal fun zipWith() {
        val l1 = MikerList.of(1, 2, 3)
        val l2 = MikerList.of(4.0, 5.0, 6.0)
        assertEquals("5.0s 7.0s 9.0s", l1.zipWith(l2){ a, b -> (a.toDouble() + b).toString() + "s" }.stringify())
    }

    @Test
    internal fun startsWith() {
        val l1 = MikerList.of(1, 2, 3)
        val l2 = MikerList.of(1, 2)
        assertTrue(l1.startsWith(l2))
    }

    @Test
    internal fun startsWithFalse() {
        val l1 = MikerList.of(1, 2, 3)
        val l2 = MikerList.of(2)
        assertFalse(l1.startsWith(l2))
    }

    @Test
    internal fun hasSubsequence() {
        val l1 = MikerList.of(1, 2, 3, 4)
        val l2 = MikerList.of(2, 3)
        assertTrue(l1.hasSubsequence(l2))
    }

    @Test
    internal fun hasSubsequenceFalse() {
        val l1 = MikerList.of(1, 2, 3, 4)
        val l2 = MikerList.of(4, 3)
        assertFalse(l1.hasSubsequence(l2))
    }

    @Test
    internal fun isEmpty() {
        val list = MikerList.of(1, 2, 3)
        assertFalse(list.isEmpty())
    }

    @Test
    internal fun isEmptyTrue() {
        val list = MikerList.empty<Int>()
        assertTrue(list.isEmpty())
    }

    @Test
    internal fun sumDouble() {
        val list = MikerList.of(1.0, 2.0, 3.0)
        assertEquals(6.0, list.sum())
    }

    @Test
    internal fun sequence() {
        val list = MikerList.of(Some(1), Some(2), Some(3))
        assertNotEquals(None, list.sequence())
    }

    @Test
    internal fun sequenceNone() {
        val list = MikerList.of(Some(1), None, Some(3))
        assertEquals(None, list.sequence())
    }

    @Test
    internal fun traverse() {
        val list = MikerList.of(1, 2, 3).traverse { x -> Some(x + 1) }
        assertNotEquals(None, list)
    }

    @Test
    internal fun sequenceT() {
        val list = MikerList.of(Some(1), Some(2), Some(3))
        assertNotEquals(None, list.sequenceT())
    }

    @Test
    internal fun traverseE() {
        val list = MikerList.of(1).traverseE { x -> Right(x + 1) }
        assertEquals(Right(MikerList.of(2)), list)
    }

    @Test
    internal fun sequenceE() {
        val list = MikerList.of(Right(1))
        assertEquals(Right(MikerList.of(1)), list.sequenceE())
    }
}