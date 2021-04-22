package org.miker

sealed class Option<out A> {
    companion object {
        fun <A> empty(): Option<A> = None

        fun <A, B> lift(f: (A) -> B): (Option<A>) -> Option<B> =
            { oa -> oa.map(f) }


        fun <A> catches(a: () -> A): Option<A> =
            try {
                Some(a())
            } catch (e: Throwable) {
                None
            }

        fun <A, B, C> map2(a: Option<A>, b: Option<B>, f: (A, B) -> C): Option<C> =
            a.flatMap { a -> b.map { b -> f(a, b) } }
    }
}

data class Some<out A>(val get: A) : Option<A>()
object None : Option<Nothing>()

fun <A, B> Option<A>.map(f: (A) -> B): Option<B> =
    when (this) {
        is None -> None
        is Some -> Some(f(this.get))
    }

fun <A, B> Option<A>.flatMap(f: (A) -> Option<B>): Option<B> = this.map(f).getOrElse { None }

fun <A> Option<A>.getOrElse(default: () -> A): A =
    when (this) {
        is None -> default()
        is Some -> this.get
    }

fun <A> Option<A>.orElse(ob: () -> Option<A>): Option<A> = this.map { Some(it) }.getOrElse { ob() }

fun <A> Option<A>.filter(f: (A) -> Boolean): Option<A> = this.flatMap { if (f(it)) Some(it) else None }

