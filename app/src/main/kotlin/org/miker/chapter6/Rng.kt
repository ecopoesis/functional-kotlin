package org.miker.chapter6

import org.miker.chapter3.Cons
import org.miker.chapter3.MikerList
import org.miker.chapter3.foldRight

interface Rng {
    fun nextInt(): Pair<Int, Rng>
}

typealias Rand<A> = (Rng) -> Pair<A, Rng>

fun nonNegativeInt(rng: Rng): Pair<Int, Rng> {
    val (i, r) = rng.nextInt()
    return Pair(if (i < 0) -(i + 1) else i, r)
}

fun double(rng: Rng): Pair<Double, Rng> {
    val (i, r) = nonNegativeInt(rng)
    return Pair(i / (Int.MAX_VALUE.toDouble() + 1) , r)
}

fun intDouble(rng: Rng): Pair<Pair<Int, Double>, Rng> {
    val (i, r1) = rng.nextInt()
    val (d, r2) = double(r1)
    return Pair(Pair(i, d), r2)
}

fun doubleInt(rng: Rng): Pair<Pair<Double, Int>, Rng> {
    val (d, r1) = double(rng)
    val (i, r2) = r1.nextInt()
    return Pair(Pair(d, i), r2)

}

fun double3(rng: Rng): Pair<Triple<Double, Double, Double>, Rng> {
    val (d1, r1) = double(rng)
    val (d2, r2) = double(r1)
    val (d3, r3) = double(r2)
    return Pair(Triple(d1, d2, d3), r3)
}

fun ints(count: Int, rng: Rng): Pair<MikerList<Int>, Rng> = when (count) {
        0 -> Pair(MikerList.empty<Int>(), rng)
        else -> {
            val (i, r1) = rng.nextInt()
            val (list, r2) = ints(count - 1, r1)
            Pair(Cons(i, list), r2)
        }
    }

val intR: Rand<Int> = { rng -> rng.nextInt() }

fun <A> unit(a: A): Rand<A> = { rng -> Pair(a, rng) }

fun <A, B> map(s: Rand<A>, f: (A) -> B): Rand<B> = { rng ->
        val (a, rng2) = s(rng)
        Pair(f(a), rng2)
    }

val doubleR: Rand<Double> = map(::nonNegativeInt) { it / (Int.MAX_VALUE.toDouble() + 1) }

fun <A, B, C> map2(ra: Rand<A>, rb: Rand<B>, f: (A, B) -> C): Rand<C> = {
    val (a, r1) = ra(it)
    val (b, r2) = rb(r1)
    Pair(f(a, b), r2)
}

fun <A, B> both(ra: Rand<A>, rb: Rand<B>): Rand<Pair<A, B>> =
    map2(ra, rb) { a, b -> Pair(a, b) }

val intDoubleR: Rand<Pair<Int, Double>> = both(intR, doubleR)

val doubleIntR: Rand<Pair<Double, Int>> = both(doubleR, intR)

fun <A> sequence(list: MikerList<Rand<A>>): Rand<MikerList<A>> =
    list.foldRight(unit(MikerList.empty())) { f, acc->
        map2(f, acc) { h, t -> Cons(h, t) }
    }

fun nonNegativeLessThan_A(n: Int): Rand<Int> =
    map(::nonNegativeInt) { it % n }

fun nonNegativeIntLessThan(n: Int): Rand<Int> =
    { rng ->
        val (i, rng2) = nonNegativeInt(rng)
        val mod = i % n
        if (i + (n - 1) - mod >= 0)
            Pair(mod, rng2)
        else nonNegativeIntLessThan(n)(rng2)
    }

fun <A, B> flatMap(f: Rand<A>, g: (A) -> Rand<B>): Rand<B> = {
    val (i, rng) = f(it)
    g(i)(rng)
}

fun nonNegativeIntLessThanM(n: Int): Rand<Int> =
    flatMap(::nonNegativeInt) {
        val mod = it % n
        if (it + (n - 1) - mod >= 0) unit(mod)
        else nonNegativeIntLessThanM(n)
    }

fun <A, B> mapF(s: Rand<A>, f: (A) -> B): Rand<B> = flatMap(s) { unit(f(it)) }

fun <A, B, C> map2F(ra: Rand<A>, rb: Rand<B>, f: (A, B) -> C): Rand<C> =
    flatMap(ra) { a -> map(rb) { b -> f(a, b) } }
