package org.miker

import arrow.core.Option
import arrow.core.Some
import arrow.core.computations.option
import arrow.core.extensions.option.fx.fx
import arrow.core.none
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import kotlin.test.assertEquals
import org.junit.jupiter.api.Test

class `4_ArrowTest` {
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
}