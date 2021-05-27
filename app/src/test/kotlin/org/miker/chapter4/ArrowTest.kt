package org.miker.chapter4

import arrow.core.*
import arrow.core.Some
import arrow.core.computations.*
import arrow.core.identity
import arrow.typeclasses.Monoid
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import kotlin.test.assertEquals
import org.junit.jupiter.api.Test

class ArrowTest {
    private val mainThreadSurrogate = newSingleThreadContext("UI thread")

    @Before
    fun setUp() {
        Dispatchers.setMain(mainThreadSurrogate)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain() // reset main dispatcher to the original Main dispatcher
        mainThreadSurrogate.close()
    }

    @Test
    internal fun optionComprehension() = runBlocking {
        val c = option {
            val (a) = Some(1)
            val (b) = Some(1 + a)
            val (c) = Some(1 + b)
            a + b + c
        }
        assertEquals(Some(6), c)
    }

    @Test
    internal fun noneComprehension() = runBlocking {
        val aa = Some(1)
        val bb = none<Int>()
        val cc = Some(2)
        val c = option {
            val a = aa.bind()
            val b = bb.bind()
            val c = cc.bind()
            a + b + c
        }
        assertEquals(none(), c)
    }

    @Test
    internal fun optionComprehensionBind() = runBlocking {
        val c = option {
            val a = Some(1).bind()
            val b = Some(1 + a).bind()
            val c = Some(1 + b).bind()
            a + b + c
        }
        assertEquals(Some(6), c)
    }

    @Test
    internal fun comboComprendoEither() = runBlocking {
        val aa = Some(1)
        val bb: Either<String, Int> = Either.Right(2)
        val c: Either<String, Int> = either {
            val a = aa.toEither { "error" }.bind()
            val b = bb.bind()
            a + b
        }
        assertEquals(Either.Right(3), c)
    }

    @Test
    internal fun comboComprendoOption() = runBlocking {
        val aa = Some(1)
        val bb: Either<String, Int> = Either.Right(2)
        val c = option {
            val a = aa.bind()
            val b = bb.orNone().bind()
            a + b
        }
        assertEquals(Some(3), c)
    }

    @Test
    internal fun listOptionToOptionList() {
        val list = listOf(Some(1), Some(2), none(), Some(3))
        val list2 = Some(list.flatMap { it.toList() })
        assertEquals(Some(listOf(1, 2, 3)), list2)
    }

    @Test
    internal fun listOptionToList() {
        val list = listOf(none<Int>(), none())
        val list2 = Some(list.flatMap { it.toList() })
        assertEquals(Some(listOf(1, 2, 3)), list2)

    }
}

fun <L, R> Either<L, R>.orNone(): Option<R> = this.orNull().toOption()
