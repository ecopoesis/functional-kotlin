package org.miker

object `2_2` {
    val <T> List<T>.tail: List<T>
        get() = drop(1)

    val <T> List<T>.head: T
        get() = first()

    fun <A> isSorted(list: List<A>, order: (A, A) -> Boolean): Boolean {
        tailrec fun loop(prev: A, list: List<A>): Boolean {
            val current = list.head
            val tail = list.tail
            return if (!order(prev, current)) {
                false
            } else if (tail.isEmpty()) {
                true
            } else {
                loop(current, list.tail)
            }
        }

        return loop(list.head, list.tail)
    }
}