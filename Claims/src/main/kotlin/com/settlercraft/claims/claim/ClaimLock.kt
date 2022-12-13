package com.settlercraft.claims.claim

class ClaimLock {
    val locks = mutableMapOf<Lock, Boolean>()

    init {
        for (lock in Lock.values())
            locks[lock] = false
    }

    fun getLock(lock: Lock): Boolean {
        return locks[lock] ?: false
    }
}