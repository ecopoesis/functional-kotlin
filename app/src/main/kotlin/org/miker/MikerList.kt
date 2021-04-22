package org.miker

import kotlin.math.pow
import kotlin.math.sign

sealed class MikerList<out A> {

    companion object {
        fun <A> of(vararg aa: A): MikerList<A> {
            val tail = aa.sliceArray(1 until aa.size)
            return if (aa.isEmpty()) Nil else Cons(aa[0], of(*tail))
        }

        fun <A> empty(): MikerList<A> = Nil

        fun sum(ints: MikerList<Int>): Int =
            when (ints) {
                is Nil -> 0
                is Cons -> ints.head + sum(ints.tail)
            }

        fun product(doubles: MikerList<Double>): Double =
            when (doubles) {
                is Nil -> 1.0
                is Cons ->
                    if (doubles.head == 0.0) 0.0
                    else doubles.head * product(doubles.tail)
            }

        fun <A> concat(list: MikerList<MikerList<A>>): MikerList<A> =
            list.foldRightL(empty()) { l1, l2 -> l1.append(l2) }
    }
}

fun <A> MikerList<A>.tail(): MikerList<A> =
    when (this) {
        is Cons -> tail
        is Nil -> throw IllegalStateException("Nil cannot have a `tail`")
    }

fun <A> MikerList<A>.setHead(x: A): MikerList<A> =
    when (this) {
        is Cons -> Cons(x, tail)
        is Nil -> throw IllegalStateException("Cannot set head on Nil")
    }

fun <A> MikerList<A>.drop(n: Int): MikerList<A> =
    when (n) {
        0 -> this
        else -> when (this)  {
            is Nil -> throw IllegalStateException("Cannot set drop the list that much")
            is Cons -> tail().drop(n-1)
        }
    }

fun <A> MikerList<A>.dropWhile(pred: (A) -> Boolean): MikerList<A> =
    when (this) {
        is Nil -> this
        is Cons -> if (pred(head)) tail.dropWhile(pred) else this
    }

fun <A> MikerList<A>.append(l: MikerList<A>): MikerList<A> =
    when (this) {
        is Nil -> l
        is Cons -> Cons(head, tail.append(l))
    }

/**
 * return a list with everything but the last element
 */
fun <A> MikerList<A>.init(): MikerList<A> =
    when (this) {
        is Nil -> throw IllegalStateException("Cannot init Nil list")
        is Cons -> if (tail == Nil) Nil else Cons(head, tail.init())
    }

fun <A, B> MikerList<A>.foldRight(z: B, f: (A, B) -> B): B =
    when (this) {
        is Nil -> z
        is Cons -> f(head, tail.foldRight(z, f))
    }

fun MikerList<Int>.sum(): Int = foldRight(0) {a, b -> a + b}

fun MikerList<Double>.sum(): Double = foldRight(0.0) {a, b -> a + b}

fun MikerList<Double>.product(): Double =
    foldRight(1.0) {a, b -> a * b}

fun <A> MikerList<A>.length(): Int =
    foldRight(0) {_, acc -> acc + 1}

fun <A> MikerList<A>.isEmpty(): Boolean = length() == 0

tailrec fun <A, B> MikerList<A>.foldLeft(z: B, f: (B, A) -> B): B =
    when (this) {
        is Nil -> z
        is Cons -> tail.foldLeft(f(z, head), f)
    }

fun MikerList<Int>.sumL(): Int =
    foldLeft(0) {a, b -> a + b}

fun MikerList<Double>.productL(): Double =
    foldLeft(1.0) {a, b -> a * b}

fun <A> MikerList<A>.lengthL(): Int =
    foldLeft(0) {acc, _ -> acc + 1}

fun <A> MikerList<A>.reverse(): MikerList<A> =
    foldLeft(MikerList.empty()) { acc, i -> Cons(i, acc)}

fun <A> MikerList<A>.stringify(): String =
    foldLeft("") { acc, i -> acc + (if (acc.isEmpty()) "" else " ") + i.toString() }

fun <A, B> MikerList<A>.foldLeftR(z: B, f: (B, A) -> B): B =
    foldRight(
        { b: B -> b },
        { a, g -> { b -> g(f(b, a)) } }
    )(z)

fun <A, B> MikerList<A>.foldRightL(z: B, f: (A, B) -> B): B =
    foldLeft(
        { b: B -> b },
        { g, a -> { b -> g(f(a, b)) }}
    )(z)

fun <A> MikerList<A>.appendF(l: MikerList<A>): MikerList<A> =
    foldRightL(l) { a, b -> Cons(a, b) }

fun <A> MikerList<MikerList<A>>.concat(): MikerList<A> =
    foldRightL(MikerList.empty()) { l1, l2 -> l1.append(l2) }

fun <A> MikerList<A>.append(i: A): MikerList<A> =
    foldRightL(MikerList.of(i)) { j, acc -> Cons(j, acc) }

fun MikerList<Int>.increment(inc: Int): MikerList<Int> =
    foldRightL(MikerList.empty()) { i, list -> Cons(i + inc, list) }

fun <A> MikerList<A>.tos(): MikerList<String> =
    foldRightL(MikerList.empty()) { i, list -> Cons(i.toString(), list)}

fun <A, B> MikerList<A>.map(f: (A) -> (B)): MikerList<B> =
    foldRightL(MikerList.empty()) { i, list -> Cons(f(i), list)}

fun <A> MikerList<A>.filter(f: (A) -> Boolean): MikerList<A> =
    foldRightL(MikerList.empty()) { i, list -> if (f(i)) Cons(i, list) else list}

fun <A, B> MikerList<A>.flatMap(f: (A) -> MikerList<B>): MikerList<B> =
    foldLeft(MikerList.empty()) { list, i -> list.append(f(i))}

fun <A> MikerList<A>.flatFilter(f: (A) -> Boolean): MikerList<A> =
    flatMap { i -> if (f(i)) MikerList.of(i) else MikerList.empty() }

tailrec fun <A> MikerList<A>.startsWith(l: MikerList<A>): Boolean =
    when (this) {
        is Nil -> l == Nil
        is Cons -> when (l) {
            is Nil -> true
            is Cons ->
                if (head == l.head)
                    tail.startsWith(l.tail)
                else
                    false
        }
    }

tailrec fun <A> MikerList<A>.hasSubsequence(sub: MikerList<A>): Boolean =
    when (this) {
        is Nil -> false
        is Cons -> when (sub) {
            is Nil -> false
            is Cons ->
                if (this.startsWith(sub))
                    true
                else
                    tail.hasSubsequence(sub)
        }
    }

fun MikerList<Int>.merge(l: MikerList<Int>): MikerList<Int> =
    when (this) {
        is Nil -> Nil
        is Cons -> when (l) {
            is Nil -> Nil
            is Cons -> Cons(this.head + l.head, this.tail.merge(l.tail))
        }
    }

fun <A, B, C> MikerList<A>.zipWith(l: MikerList<B>, f: (A, B) -> C): MikerList<C> =
    when (this) {
        is Nil -> Nil
        is Cons -> when (l) {
            is Nil -> Nil
            is Cons -> Cons(f(this.head, l.head), this.tail.zipWith(l.tail, f))
        }
    }

// chapter 4

fun MikerList<Double>.mean(): Option<Double> =
    if (isEmpty()) None
    else Some(sum() / length())

fun MikerList<Double>.variance(): Option<Double> =
    this.mean().flatMap { m ->
        this.map { x ->
            (x - m).pow(2)
        }.mean()
    }

fun <A> MikerList<Option<A>>.sequence(): Option<MikerList<A>> =
    foldRight(Some(MikerList.empty())) { optionI: Option<A>, optionL: Option<MikerList<A>> ->
        Option.map2(optionI, optionL) { i: A, l: MikerList<A> ->
            Cons(i, l)
        }
    }

object Nil : MikerList<Nothing>() {
    override fun toString(): String = "Nil"
}

data class Cons<out A>(
    val head: A,
    val tail: MikerList<A>
) : MikerList<A>()