package org.miker.chapter4

sealed class MikerEither<out E, out A> {
    companion object {
        fun <A> catches(a: () -> A): MikerEither<Exception, A> =
            try {
                Right(a())
            } catch (e: Exception) {
                Left(e)
            }

        fun <E, A, B, C> map2(ae: MikerEither<E, A>, be: MikerEither<E, B>, f: (A, B) -> C): MikerEither<E, C> =
            ae.flatMap { a -> be.map { b -> f(a, b) } }
    }
}

data class Left<out E>(val value: E) : MikerEither<E, Nothing>()
data class Right<out A>(val value: A) : MikerEither<Nothing, A>()

fun <E, A, B> MikerEither<E, A>.map(f: (A) -> B): MikerEither<E, B> =
    when (this) {
        is Left -> this
        is Right -> Right(f(this.value))
    }

fun <E, A, B> MikerEither<E, A>.flatMap(f: (A) -> MikerEither<E, B>): MikerEither<E, B> =
    when (this) {
        is Left -> this
        is Right -> f(this.value)
    }

fun <E, A> MikerEither<E, A>.orElse(f: () -> MikerEither<E, A>): MikerEither<E, A> =
    when (this) {
        is Left -> f()
        is Right -> this
    }
