package org.miker.chapter6

//import arrow.mtl.State

sealed class Input

object Coin : Input()
object Turn : Input()

data class Machine(
    val locked: Boolean,
    val candies: Int,
    val coins: Int
)
/*
fun simulateMachine(
    inputs: List<Input>
): State<Machine, Tuple2<Int, Int>> =
*/
