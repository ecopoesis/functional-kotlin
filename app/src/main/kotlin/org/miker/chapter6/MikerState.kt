package org.miker.chapter6

import org.miker.chapter3.Cons
import org.miker.chapter3.MikerList
import org.miker.chapter3.foldRight


data class MikerState<S, out A>(val run: (S) -> Pair<A, S>) {
    companion object {
        fun <S, A> unit(a: A): MikerState<S, A> = MikerState { s -> Pair(a, s) }

        fun <S, A, B, C> map2(ra: MikerState<S, A>, rb: MikerState<S, B>, f: (A, B) -> C): MikerState<S, C> =
            ra.flatMap { a -> rb.flatMap { b -> unit(f(a, b)) } }

        fun <S, A> sequence(list: MikerList<MikerState<S, A>>): MikerState<S, MikerList<A>> =
            list.foldRight(unit(MikerList.empty())) { f, acc->
                map2(f, acc) { h, t -> Cons(h, t) }
            }
    }

    fun <B> flatMap(f: (A) -> MikerState<S, B>): MikerState<S, B> = MikerState {
        val (a, s) = this.run(it)
        f(a).run(s)
    }

    fun <B> map(f: (A) -> (B)): MikerState<S, B> = flatMap { unit(f(it)) }
}
