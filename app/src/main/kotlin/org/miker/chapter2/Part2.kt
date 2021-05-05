package org.miker.chapter2

object Part2 {
    val <T> List<T>.tail: List<T>
        get() = drop(1)

    val <T> List<T>.head: T
        get() = first()

    fun <A> isSorted(list: List<A>, order: (A, A) -> Boolean): Boolean {
        tailrec fun loop(prev: A, list: List<A>): Boolean {
            val current = list.head
            val tail = list.tail
            return when {
                !order(prev, current) -> false
                tail.isEmpty() -> true
                else -> loop(current, list.tail)
            }
        }

        return loop(list.head, list.tail)
    }
}
