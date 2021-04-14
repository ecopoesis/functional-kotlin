package org.miker

sealed class `3_List`<out A> {

    companion object {
        fun <A> of(vararg aa: A): `3_List`<A> {
            val tail = aa.sliceArray(1 until aa.size)
            return if (aa.isEmpty()) Nil else Cons(aa[0], of(*tail))
        }

        fun <A> empty(): `3_List`<A> = Nil

        fun sum(ints: `3_List`<Int>): Int =
            when (ints) {
                is Nil -> 0
                is Cons -> ints.head + sum(ints.tail)
            }

        fun product(doubles: `3_List`<Double>): Double =
            when (doubles) {
                is Nil -> 1.0
                is Cons ->
                    if (doubles.head == 0.0) 0.0
                    else doubles.head * product(doubles.tail)
            }
    }
}

fun <A> `3_List`<A>.tail(): `3_List`<A> =
    when (this) {
        is Cons -> tail
        is Nil -> throw IllegalStateException("Nil cannot have a `tail`")
    }

fun <A> `3_List`<A>.setHead(x: A): `3_List`<A> =
    when (this) {
        is Cons -> Cons(x, tail)
        is Nil -> throw IllegalStateException("Cannot set head on Nil")
    }

fun <A> `3_List`<A>.drop(n: Int): `3_List`<A> =
    when (n) {
        0 -> this
        else -> when (this)  {
            is Nil -> throw IllegalStateException("Cannot set drop the list that much")
            is Cons -> tail().drop(n-1)
        }
    }

fun <A> `3_List`<A>.dropWhile(pred: (A) -> Boolean): `3_List`<A> =
    when (this) {
        is Nil -> this
        is Cons -> if (pred(head)) tail.dropWhile(pred) else this
    }

fun <A> `3_List`<A>.append(l: `3_List`<A>): `3_List`<A> =
    when (this) {
        is Nil -> l
        is Cons -> Cons(head, tail.append(l))
    }

/**
 * return a list with everything but the last element
 */
fun <A> `3_List`<A>.init(): `3_List`<A> =
    when (this) {
        is Nil -> throw IllegalStateException("Cannot init Nil list")
        is Cons -> if (tail == Nil) Nil else Cons(head, tail.init())
    }

fun <A, B> `3_List`<A>.foldRight(z: B, f: (A, B) -> B): B =
    when (this) {
        is Nil -> z
        is Cons -> f(head, this.tail.foldRight(z, f))
    }

fun `3_List`<Int>.sum(): Int =
    foldRight(0) {a, b -> a + b}

fun `3_List`<Double>.product(): Double =
    foldRight(1.0) {a, b -> a * b}

fun <A> `3_List`<A>.length(): Int =
    foldRight(0) {_, acc -> acc + 1}

object Nil : `3_List`<Nothing>() {
    override fun toString(): String = "Nil"
}

data class Cons<out A>(
    val head: A,
    val tail: `3_List`<A>
) : `3_List`<A>()