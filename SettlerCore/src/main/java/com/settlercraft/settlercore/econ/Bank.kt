package com.settlercraft.settlercore.econ

import com.settlercraft.settlercore.settler.Settler

class Bank {
    val accounts = mutableMapOf<Settler, Double>()

    fun withdraw(settler: Settler, amount: Double) {
        accounts[settler] = accounts[settler]!! - amount

    }
}