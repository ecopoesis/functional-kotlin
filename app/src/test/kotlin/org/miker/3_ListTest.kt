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
}