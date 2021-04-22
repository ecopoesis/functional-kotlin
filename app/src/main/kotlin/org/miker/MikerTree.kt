package org.miker

sealed class Tree<out A> {
    companion object {

        // who needs type safety
        fun <Int> of(a: Any): Tree<Int> =
            when (a) {
                is Array<*> -> {
                    when (a.size) {
                        2 -> Branch(of(a[0] as Any), of(a[1] as Any))
                        1 -> Leaf(a[0] as Int)
                        else -> throw IllegalStateException("Bad tree")
                    }
                }
                is Any -> Leaf(a as Int)
                else -> throw IllegalStateException("Bad tree")
            }
    }
}

data class Leaf<A>(val value: A) : Tree<A>()

data class Branch<A>(
    val left: Tree<A>,
    val right: Tree<A>
) : Tree<A>()

/**
 * leaves and nodes
 */
fun <A> Tree<A>.size(): Int =
    when (this) {
        is Leaf -> 1
        is Branch -> 1 + this.left.size() + this.right.size()
    }

fun Tree<Int>.maximum(): Int =
    when (this) {
        is Leaf -> this.value
        is Branch -> maxOf(this.left.maximum(), this.right.maximum())
    }

fun <A> Tree<A>.depth(): Int =
    when (this) {
        is Leaf -> 0
        is Branch -> 1 + maxOf(this.left.depth(), this.right.depth())
    }

fun <A, B> Tree<A>.map(f: (A) -> (B)): Tree<B> =
    when (this) {
        is Leaf -> Leaf(f(this.value))
        is Branch -> Branch(this.left.map(f), this.right.map(f))
    }

fun <A, B> Tree<A>.fold(leaf: (A) -> B, branch: (B, B) -> B): B =
    when (this) {
        is Leaf -> leaf(this.value)
        is Branch -> branch(this.left.fold(leaf, branch), this.right.fold(leaf, branch))
    }

fun <A> Tree<A>.sizeF(): Int = this.fold({1}, { b1, b2 -> 1 + b1 + b2 })

fun Tree<Int>.maximumF(): Int = this.fold({a -> a}, {b1, b2 -> maxOf(b1, b2)})

fun <A> Tree<A>.depthF(): Int = this.fold({0}, { b1, b2 -> 1 + maxOf(b1, b2) })

fun <A, B> Tree<A>.mapF(f: (A) -> (B)): Tree<B> = this.fold({ a: A -> Leaf(f(a)) }, { b1: Tree<B>, b2: Tree<B> -> Branch(b1, b2) })
