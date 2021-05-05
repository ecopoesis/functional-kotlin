package org.miker.chapter3

import kotlin.test.assertEquals
import org.junit.jupiter.api.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class TreeTest {
    @Test
    internal fun ofLeaf() {
        val tree = Tree.of<Int>(1)
        assertEquals(1, tree.size())
    }

    @Test
    internal fun ofLeafArrary() {
        val tree = Tree.of<Int>(arrayOf(1))
        assertEquals(1, tree.size())
    }

    @Test
    internal fun of() {
        val tree = Tree.of<Int>(arrayOf(1, arrayOf(2, 3)))
        assertEquals(5, tree.size())
    }

    @Test
    internal fun maximum() {
        val tree = Tree.of<Int>(arrayOf(1, arrayOf(2, 3)))
        assertEquals(3, tree.maximum())
    }

    @Test
    internal fun depth() {
        val tree = Tree.of<Int>(arrayOf(1, arrayOf(2, arrayOf(3, 4))))
        assertEquals(3, tree.depth())
    }

    @Test
    internal fun map() {
        val tree = Tree.of<Int>(arrayOf(1, arrayOf(2, arrayOf(3, 4)))).map { a -> a + 5 }
        assertEquals(9, tree.maximum())
    }

    @Test
    internal fun sizeF() {
        val tree = Tree.of<Int>(arrayOf(1, arrayOf(2, 3)))
        assertEquals(5, tree.sizeF())
    }

    @Test
    internal fun maximumF() {
        val tree = Tree.of<Int>(arrayOf(1, arrayOf(2, 3)))
        assertEquals(3, tree.maximumF())
    }

    @Test
    internal fun depthF() {
        val tree = Tree.of<Int>(arrayOf(1, arrayOf(2, arrayOf(3, 4))))
        assertEquals(3, tree.depthF())
    }

    @Test
    internal fun mapF() {
        val tree = Tree.of<Int>(arrayOf(1, arrayOf(2, arrayOf(3, 4)))).mapF { a -> a + 5 }
        assertEquals(9, tree.maximum())
    }
}
