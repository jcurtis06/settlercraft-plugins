package com.settlercraft.settlercore.utils

class Two<A, B>(val first: A, val second: B) {
    override fun toString(): String {
        return "Pair(first=$first, second=$second)"
    }
}