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

        fun ones(): MikerStream<Int> = cons({ 1 }, { ones() })

        fun <A> constant(a: A): MikerStream<A> = cons({ a }, { constant(a) })

        fun from(n: Int): MikerStream<Int> = cons({ n }, { from(n + 1) })

        fun fibs(): MikerStream<Int> {
            fun go(a: Int, b: Int): MikerStream<Int> = cons({a}, { go(b, a+b) })

            return go(0, 1)
        }

        fun <A, S> unfold(z: S, f: (S) -> MikerOption<Pair<A, S>>): MikerStream<A> =
            f(z).map { pair ->
                cons({pair.first}, { unfold(pair.second, f) })
            }.getOrElse { empty() }

        fun fibsUnfold(): MikerStream<Int> = unfold(Pair(0, 1)) { (a, b) -> Some(Pair(a, Pair(b, a + b))) }

        fun fromUnfold(n: Int): MikerStream<Int> = unfold(n) { Some(Pair(it, it + 1)) }

        fun <A> constantUnfold(a: A): MikerStream<A> = unfold(a) { Some(Pair(a, a)) }

        fun onesU(): MikerStream<Int> = unfold(1) { Some(Pair(1, 1)) }
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
    this.foldRight({ MikerList.empty() }) {
        b, acc ->  Cons(b, acc())
}

tailrec fun <A, B> MikerStream<A>.foldLeft(z: () -> B, f: (() -> B, A) -> B): B =
    when (this) {
        is Empty -> z()
        is StreamCons -> tail().foldLeft({ f(z, this.head()) }, f)
    }

fun <A, B> MikerStream<A>.foldRight(z: () -> B, f: (A, () -> B) -> B): B =
    when (this) {
        is StreamCons -> f(this.head()) { tail().foldRight(z, f) }
        else -> z()
    }

fun <A> MikerStream<A>.take(n: Int): MikerStream<A> {
    fun go(xs: MikerStream<A>, n: Int): MikerStream<A> = when (xs) {
        is Empty -> MikerStream.empty()
        is StreamCons ->
            if (n == 0) MikerStream.empty()
            else MikerStream.cons(xs.head, { go(xs.tail(), n - 1) })
    }
    return go(this, n)
}

fun <A> MikerStream<A>.reverse(): MikerStream<A> =
    foldLeft( { MikerStream.empty() }) { acc, i -> MikerStream.cons({ i },  acc )}

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

fun <A> MikerStream<A>.exists(p: (A) -> Boolean): Boolean =
    when (this) {
        is StreamCons -> p(this.head()) || this.tail().exists(p)
        else -> false
    }

fun <A> MikerStream<A>.existsF(p: (A) -> Boolean): Boolean =
    foldRight({ false }, { a, b -> p(a) || b() })

fun <A> MikerStream<A>.forAll(p: (A) -> Boolean): Boolean =
    foldRight({ true }, { a, b -> p(a) && b() })

fun <A> MikerStream<A>.takeWhileF(p: (A) -> Boolean): MikerStream<A> =
    foldRight({MikerStream.empty()}) { h, t ->
        if (p(h))
            MikerStream.cons({ h }, t)
        else MikerStream.empty()
    }

fun <A> MikerStream<A>.headOption(): MikerOption<A> =
    foldRight({ MikerOption.empty() }) { h, _ ->
        Some(h)
    }

fun <A, B> MikerStream<A>.map(f: (A) -> B): MikerStream<B> = foldRight({ MikerStream.empty() }) { h, t -> MikerStream.cons({ f(h) }, t)}

fun <A> MikerStream<A>.filter(f: (A) -> Boolean): MikerStream<A> = foldRight({ MikerStream.empty() }) { h, t ->
    if (f(h)) MikerStream.cons({ h }, t) else t()
}

fun <A> MikerStream<A>.append(sa: () -> MikerStream<A>): MikerStream<A> = foldRight(sa) { h, t -> MikerStream.cons({ h }, t)}

fun <A, B> MikerStream<A>.flatMap(f: (A) -> MikerStream<B>): MikerStream<B> =
    foldRight({ MikerStream.empty() }) { h, t -> f(h).append(t) }

fun <A, B> MikerStream<A>.mapU(f: (A) -> B): MikerStream<B> =
    MikerStream.unfold(this) { s ->
        when(s) {
            is Empty -> None
            is StreamCons -> Some(Pair(f(s.head()), s.tail()))
        }
    }

fun <A> MikerStream<A>.takeU(n: Int): MikerStream<A> =
    MikerStream.unfold(this) { s ->
        when (s) {
            is Empty -> None
            is StreamCons ->
                if (n == 0) None
                else Some(Pair(s.head(), s.tail().takeU(n -1)))
        }
    }

fun <A> MikerStream<A>.takeWhileU(p: (A) -> Boolean): MikerStream<A> =
    MikerStream.unfold(this) { s->
        when (s) {
            is Empty -> None
            is StreamCons ->
                if (p(s.head())) Some(Pair(s.head(), s.tail().takeWhileU(p)))
                else None
        }
    }

fun <A, B, C> MikerStream<A>.zipWith(that: MikerStream<B>, f: (A, B) -> C): MikerStream<C> =
    MikerStream.unfold(Pair(this, that)) { (sa, sb) ->
        when (sa) {
            is Empty -> None
            is StreamCons -> when (sb) {
                is Empty -> None
                is StreamCons ->
                    Some(Pair(
                        f(sa.head(), sb.head()),
                        Pair(sa.tail(), sb.tail())
                    ))
            }
        }
    }

fun <A, B> MikerStream<A>.zipAll(that: MikerStream<B>): MikerStream<Pair<MikerOption<A>, MikerOption<B>>> =
    MikerStream.unfold(Pair(this, that)) { (sa, sb) ->
        when (sa) {
            is Empty -> when (sb) {
                is Empty -> None
                is StreamCons -> Some(Pair(
                    Pair(None, Some(sb.head())),
                    Pair(MikerStream.empty(), sb.tail())
                ))
            }
            is StreamCons -> when (sb) {
                is Empty -> Some(Pair(
                    Pair(Some(sa.head()), None),
                    Pair(sa.tail(), MikerStream.empty())
                ))
                is StreamCons -> Some(Pair(
                    Pair(Some(sa.head()), Some(sb.head())),
                    Pair(sa.tail(), sb.tail())
                ))
            }
        }
    }