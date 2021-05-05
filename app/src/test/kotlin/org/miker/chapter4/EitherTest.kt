package org.miker.chapter4

import kotlin.test.assertEquals
import org.junit.jupiter.api.Test

class EitherTest {
    @Test
    internal fun mapRight() {
        val foo: MikerEither<String, Int> = Right(1)
        assertEquals(Right(2), foo.map { a -> a + 1 })
    }

    @Test
    internal fun mapLeft() {
        val foo: MikerEither<String, Int> = Left("an error")
        assertEquals(Left("an error"), foo.map { a -> a + 1 })
    }

    @Test
    internal fun flatMapRight() {
        val foo: MikerEither<String, Int> = Right(1)
        assertEquals(Right(2), foo.flatMap { a -> Right(a + 1) })
    }

    @Test
    internal fun flatMapLeft() {
        val foo: MikerEither<String, Int> = Left("an error")
        assertEquals(Left("an error"), foo.flatMap { a -> Right(a + 1) })
    }

    @Test
    internal fun orElseRight() {
        val foo: MikerEither<String, Int> = Right(1)
        assertEquals(Right(1), foo.orElse { Left("different error") })
    }

    @Test
    internal fun orElseLeft() {
        val foo: MikerEither<String, Int> = Left("an error")
        assertEquals(Left("different error"), foo.orElse { Left("different error") })
    }

    @Test
    internal fun map2RightRight() {
        val foo: MikerEither<String, Int> = Right(1)
        val bar: MikerEither<String, Int> = Right(2)
        assertEquals(Right(3), MikerEither.map2(foo, bar) { a, b -> a + b })
    }

    @Test
    internal fun map2RightLeft() {
        val foo: MikerEither<String, Int> = Right(1)
        val bar: MikerEither<String, Int> = Left("an error")
        assertEquals(Left("an error"), MikerEither.map2(foo, bar) { a, b -> a + b })
    }

    @Test
    internal fun map2LeftRight() {
        val foo: MikerEither<String, Int> = Left("an error")
        val bar: MikerEither<String, Int> = Right(1)
        assertEquals(Left("an error"), MikerEither.map2(foo, bar) { a, b -> a + b })
    }

    @Test
    internal fun map2LeftLeft() {
        val foo: MikerEither<String, Int> = Left("an error")
        val bar: MikerEither<String, Int> = Left("another error")
        assertEquals(Left("an error"), MikerEither.map2(foo, bar) { a, b -> a + b })
    }
}
