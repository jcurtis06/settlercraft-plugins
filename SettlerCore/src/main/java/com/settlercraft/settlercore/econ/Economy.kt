package com.settlercraft.settlercore.econ

import com.settlercraft.settlercore.data.Database
import com.settlercraft.settlercore.settler.Settlers
import java.util.UUID

/**
 * Manages the economy of the server
 */
object Economy {
    fun getBalance(uuid: UUID): Pair<Double?, EconomyError> {
        if (Settlers.getSettler(uuid) == null) return Pair(null, EconomyError.SETTLER_NOT_FOUND)
        return Pair(Settlers.getSettler(uuid)!!.wallet.cash, EconomyError.SUCCESS)
    }

    fun setBalance(uuid: UUID, amount: Double): EconomyError {
        if (Settlers.getSettler(uuid) == null) return EconomyError.SETTLER_NOT_FOUND
        Settlers.getSettler(uuid)!!.wallet.cash = amount
        println("Setting balance of $uuid to $amount...")
        Database.setColWhere("settlers", "cash", "uuid", uuid, amount)
        println("Set $uuid's balance to $amount")
        return EconomyError.SUCCESS
    }

    fun deposit(uuid: UUID, amount: Double): EconomyError {
        if (Settlers.getSettler(uuid) == null) return EconomyError.SETTLER_NOT_FOUND
        setBalance(uuid, getBalance(uuid).first!! + amount)
        return EconomyError.SUCCESS
    }

    fun withdraw(uuid: UUID, amount: Double): EconomyError {
        if (getBalance(uuid).first == null) return EconomyError.SETTLER_NOT_FOUND
        setBalance(uuid, getBalance(uuid).first!! - amount)
        return EconomyError.SUCCESS
    }

    fun transfer(from: UUID, to: UUID, amount: Double): EconomyError {
        if (Settlers.getSettler(from) == null) return EconomyError.SETTLER_NOT_FOUND
        if (Settlers.getSettler(to) == null) return EconomyError.SETTLER_NOT_FOUND
        if (getBalance(from).first!! < amount) return EconomyError.INSUFFICIENT_FUNDS

        withdraw(from, amount)
        deposit(to, amount)
        return EconomyError.SUCCESS
    }
}