package org.miker

import java.util.stream.Stream

sealed class MikerStream<out A> {

    companion object {

        //smart constructors
        fun <A> of(vararg xs: A): MikerStream<A> =
            if (xs.isEmpty()) empty()
            else cons({ xs[0] },
                { of(*xs.sliceArray(1 until xs.size)) })

        fun <A> cons(hd: () -> A, tl: () -> MikerStream<A>): MikerStream<A> {
            val head: A by lazy(hd)
            val tail: MikerStream<A> by lazy(tl)
            return StreamCons({ head }, { tail })
        }

        fun <A> empty(): MikerStream<A> = Empty
    }
}

data class StreamCons<out A>(
    val head: () -> A,
    val tail: () -> MikerStream<A>
) : MikerStream<A>()

object Empty : MikerStream<Nothing>()

fun <A> MikerStream<A>.continually(a: A): MikerStream<A> =
    MikerStream.cons({ a }, { continually(a) })

fun <A> MikerStream<A>.toListBook(): MikerList<A> {
    tailrec fun go(xs: MikerStream<A>, acc: MikerList<A>): MikerList<A> = when (xs) {
        is Empty -> acc
        is StreamCons -> go(xs.tail(), Cons(xs.head(), acc))
    }
    return (go(this, Nil)).reverse()
}

fun <A> MikerStream<A>.toList(): MikerList<A> =
    this.foldRight(MikerList.empty()) {
        b, acc: MikerList<A> -> Cons(b, acc)
}

tailrec fun <A, B> MikerStream<A>.foldLeft(z: B, f: (B, A) -> B): B =
    when (this) {
        is Empty -> z
        is StreamCons -> tail().foldLeft(f(z, this.head()), f)
    }

fun <A, B> MikerStream<A>.foldRight(z: B, f: (A, B) -> B): B =
    foldLeft(
        { b: B -> b },
        { g, a -> { b -> g(f(a, b)) }}
    )(z)

fun <A> MikerStream<A>.takeBook(n: Int): MikerStream<A> {
    fun go(xs: MikerStream<A>, n: Int): MikerStream<A> = when (xs) {
        is Empty -> MikerStream.empty()
        is StreamCons ->
            if (n == 0) MikerStream.empty()
            else MikerStream.cons(xs.head, { go(xs.tail(), n - 1) })
    }
    return go(this, n)
}

fun <A> MikerStream<A>.take(n: Int): MikerStream<A> {
    val (taken, _) = this.foldLeft(Pair(MikerStream.empty<A>(), n)) { (acc, m), b ->
        if (m == 0)
            Pair(acc, 0)
        else
            Pair(MikerStream.cons({ b }, { acc }), m - 1)
    }
    return taken.reverse()
}

fun <A> MikerStream<A>.reverse(): MikerStream<A> =
    foldLeft(MikerStream.empty()) { acc, i -> MikerStream.cons({ i }, { acc })}

fun <A> MikerStream<A>.drop(n: Int): MikerStream<A> {
    tailrec fun go(xs: MikerStream<A>, n: Int): MikerStream<A> = when (xs) {
        is Empty -> MikerStream.empty()
        is StreamCons ->
            if (n == 0) xs
            else go(xs.tail(), n - 1)
    }
    return go(this, n)
}

fun <A> MikerStream<A>.takeWhile(p: (A) -> Boolean): MikerStream<A> =
    when (this) {
        is Empty -> MikerStream.empty()
        is StreamCons ->
            if (p(this.head()))
                MikerStream.cons(this.head, { this.tail().takeWhile(p) })
            else MikerStream.empty()
    }