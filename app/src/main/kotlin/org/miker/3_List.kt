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

        fun <A> concat(list: `3_List`<`3_List`<A>>): `3_List`<A> =
            list.foldRightL(empty()) { l1, l2 -> l1.append(l2) }
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
        is Cons -> f(head, tail.foldRight(z, f))
    }

fun `3_List`<Int>.sum(): Int =
    foldRight(0) {a, b -> a + b}

fun `3_List`<Double>.product(): Double =
    foldRight(1.0) {a, b -> a * b}

fun <A> `3_List`<A>.length(): Int =
    foldRight(0) {_, acc -> acc + 1}

tailrec fun <A, B> `3_List`<A>.foldLeft(z: B, f: (B, A) -> B): B =
    when (this) {
        is Nil -> z
        is Cons -> tail.foldLeft(f(z, head), f)
    }

fun `3_List`<Int>.sumL(): Int =
    foldLeft(0) {a, b -> a + b}

fun `3_List`<Double>.productL(): Double =
    foldLeft(1.0) {a, b -> a * b}

fun <A> `3_List`<A>.lengthL(): Int =
    foldLeft(0) {acc, _ -> acc + 1}

fun <A> `3_List`<A>.reverse(): `3_List`<A> =
    foldLeft(`3_List`.empty()) {acc, i -> Cons(i, acc)}

fun <A> `3_List`<A>.stringify(): String =
    foldLeft("") { acc, i -> acc + (if (acc.isEmpty()) "" else " ") + i.toString() }

fun <A, B> `3_List`<A>.foldLeftR(z: B, f: (B, A) -> B): B =
    foldRight(
        { b: B -> b },
        { a, g -> { b -> g(f(b, a)) } }
    )(z)

fun <A, B> `3_List`<A>.foldRightL(z: B, f: (A, B) -> B): B =
    foldLeft(
        { b: B -> b },
        { g, a -> { b -> g(f(a, b)) }}
    )(z)

fun <A> `3_List`<A>.appendF(l: `3_List`<A>): `3_List`<A> =
    foldRightL(l) { a, b -> Cons(a, b) }

fun <A> `3_List`<`3_List`<A>>.concat(): `3_List`<A> =
    foldRightL(`3_List`.empty()) { l1, l2 -> l1.append(l2) }

fun <A> `3_List`<A>.append(i: A): `3_List`<A> =
    foldRightL(`3_List`.of(i)) { j, acc -> Cons(j, acc) }

fun `3_List`<Int>.increment(inc: Int): `3_List`<Int> =
    foldRightL(`3_List`.empty()) { i, list -> Cons(i + inc, list) }

fun <A> `3_List`<A>.tos(): `3_List`<String> =
    foldRightL(`3_List`.empty()) { i, list -> Cons(i.toString(), list)}

fun <A, B> `3_List`<A>.map(f: (A) -> (B)): `3_List`<B> =
    foldRightL(`3_List`.empty()) { i, list -> Cons(f(i), list)}

fun <A> `3_List`<A>.filter(f: (A) -> Boolean): `3_List`<A> =
    foldRightL(`3_List`.empty()) { i, list -> if (f(i)) Cons(i, list) else list}

fun <A, B> `3_List`<A>.flatMap(f: (A) -> `3_List`<B>): `3_List`<B> =
    foldLeft(`3_List`.empty()) { list, i -> list.append(f(i))}

fun <A> `3_List`<A>.flatFilter(f: (A) -> Boolean): `3_List`<A> =
    flatMap { i -> if (f(i)) `3_List`.of(i) else `3_List`.empty() }

fun `3_List`<Int>.merge(l: `3_List`<Int>): `3_List`<Int> =
    when (this) {
        is Nil -> Nil
        is Cons -> when (l) {
            is Nil -> Nil
            is Cons -> Cons(this.head + l.head, this.tail.merge(l.tail))
        }
    }

fun <A, B, C> `3_List`<A>.zipWith(l: `3_List`<B>, f: (A, B) -> C): `3_List`<C> =
    when (this) {
        is Nil -> Nil
        is Cons -> when (l) {
            is Nil -> Nil
            is Cons -> Cons(f(this.head, l.head), this.tail.zipWith(l.tail, f))
        }
    }

object Nil : `3_List`<Nothing>() {
    override fun toString(): String = "Nil"
}

data class Cons<out A>(
    val head: A,
    val tail: `3_List`<A>
) : `3_List`<A>()