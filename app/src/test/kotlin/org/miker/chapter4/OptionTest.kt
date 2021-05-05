package org.miker.chapter4

import kotlin.test.assertEquals
import org.junit.jupiter.api.Test

class OptionTest {
    @Test
    internal fun map() {
        val foo = Some(1)
        assertEquals(Some(2), foo.map { a -> a + 1 })
    }

    @Test
    internal fun mapNone() {
        val foo = MikerOption.empty<Int>()
        assertEquals(None, foo.map { a -> a + 1 })
    }

    @Test
    internal fun getOrElse() {
        val foo = Some(1)
        assertEquals(1, foo.getOrElse { 2 })
    }

    @Test
    internal fun getOrElseNone() {
        val foo = MikerOption.empty<Int>()
        assertEquals(2, foo.getOrElse { 2 })
    }

    @Test
    internal fun flatMap() {
        val foo = Some(1)
        assertEquals(Some(2), foo.flatMap { a -> Some(a + 1) })
    }

    @Test
    internal fun flatMapNone() {
        val foo = MikerOption.empty<Int>()
        assertEquals(None, foo.flatMap { Some(it + 1) })
    }

    @Test
    internal fun orElse() {
        val foo = Some(1)
        assertEquals(Some(1), foo.orElse { Some(2) })
    }

    @Test
    internal fun orElseNone() {
        val foo = MikerOption.empty<Int>()
        assertEquals(Some(2), foo.orElse { Some(2) })
    }

    @Test
    internal fun filter() {
        val foo = Some(1)
        assertEquals(Some(1), foo.filter { it % 2 == 1 } )
    }

    @Test
    internal fun filterFail() {
        val foo = Some(2)
        assertEquals(None, foo.filter { it % 2 == 1 } )
    }

    @Test
    internal fun filterNone() {
        val foo = MikerOption.empty<Int>()
        assertEquals(None, foo.filter { it % 2 == 1 } )
    }

    @Test
    internal fun map2() {
        val foo = Some(1)
        val bar = Some(2)
        assertEquals(Some(3), MikerOption.map2(foo, bar) { a, b -> a + b })
    }
}
