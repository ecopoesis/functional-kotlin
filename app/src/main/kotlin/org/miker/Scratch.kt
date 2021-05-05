package org.miker

class Scratch {
    companion object {
        fun factory() = lazy {
            println("Executing lazy")
            "foo"
        }
    }
}