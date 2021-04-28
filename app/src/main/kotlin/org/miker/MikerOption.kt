package org.miker

sealed class MikerOption<out A> {
    companion object {
        fun <A> empty(): MikerOption<A> = None

        fun <A, B> lift(f: (A) -> B): (MikerOption<A>) -> MikerOption<B> =
            { oa -> oa.map(f) }


        fun <A> catches(a: () -> A): MikerOption<A> =
            try {
                Some(a())
            } catch (e: Throwable) {
                None
            }

        fun <A, B, C> map2(a: MikerOption<A>, b: MikerOption<B>, f: (A, B) -> C): MikerOption<C> =
            a.flatMap { a -> b.map { b -> f(a, b) } }
    }
}

data class Some<out A>(val get: A) : MikerOption<A>()
object None : MikerOption<Nothing>()

fun <A, B> MikerOption<A>.map(f: (A) -> B): MikerOption<B> =
    when (this) {
        is None -> None
        is Some -> Some(f(this.get))
    }

fun <A, B> MikerOption<A>.flatMap(f: (A) -> MikerOption<B>): MikerOption<B> = this.map(f).getOrElse { None }

fun <A> MikerOption<A>.getOrElse(default: () -> A): A =
    when (this) {
        is None -> default()
        is Some -> this.get
    }

fun <A> MikerOption<A>.orElse(ob: () -> MikerOption<A>): MikerOption<A> = this.map { Some(it) }.getOrElse { ob() }

fun <A> MikerOption<A>.filter(f: (A) -> Boolean): MikerOption<A> = this.flatMap { if (f(it)) Some(it) else None }

fun <A> MikerOption<A>.isEmpty(): Boolean = this == None