package org.miker

object `2_3` {
    // (A) -> ((B) -> C)
    // (A) -> (B) -> C

    fun <A, B, C> curry(f: (A, B) -> C): (A) -> (B) -> C =
        { a: A -> { b: B -> f(a, b) } }

    fun <A, B, C> uncurry(f: (A) -> (B) -> C): (A, B) -> C =
        { a: A, b: B -> f(a)(b) }

    fun <A, B, C> compose(f: (B) -> C, g: (A) -> B): (A) -> C =
        { a: A -> f(g(a)) }
}