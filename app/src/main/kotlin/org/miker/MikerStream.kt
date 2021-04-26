package org.miker

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

        fun <A> continually(a: A): MikerStream<A> =
            cons({ a }, { continually(a) })
    }
}

data class StreamCons<out A>(
    val head: () -> A,
    val tail: () -> MikerStream<A>
) : MikerStream<A>()

object Empty : MikerStream<Nothing>()

fun <A> MikerStream<A>.toListBook(): MikerList<A> {
    tailrec fun go(xs: MikerStream<A>, acc: MikerList<A>): MikerList<A> = when (xs) {
        is Empty -> acc
        is StreamCons -> go(xs.tail(), Cons(xs.head(), acc))
    }
    return (go(this, Nil)).reverse()
}

fun <A> MikerStream<A>.toList(): MikerList<A> = this.foldRight(MikerList.empty<A>()) {
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