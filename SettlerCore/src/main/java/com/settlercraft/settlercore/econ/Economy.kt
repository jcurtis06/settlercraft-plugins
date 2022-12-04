package com.settlercraft.settlercore.econ

import com.settlercraft.settlercore.data.Database
import com.settlercraft.settlercore.settler.Settlers
import java.util.UUID

/**
 * Manages the economy of the server
 */
object Economy {
    /**
     * Gets the balance of a player
     * @param uuid The UUID of the player
     * @return A pair, first is balance, second is EconomyError
     * @see EconomyError
     */
    fun getBalance(uuid: UUID): Pair<Double?, EconomyError> {
        if (Settlers.getSettler(uuid) == null) return Pair(null, EconomyError.SETTLER_NOT_FOUND)
        return Pair(Settlers.getSettler(uuid)!!.money, EconomyError.SUCCESS)
    }

    /**
     * Sets the balance of a player
     * @param uuid The UUID of the player
     * @param amount The amount to set the balance to
     * @return An EconomyError to indicate success or failure and the reason
     * @see EconomyError
     */
    fun setBalance(uuid: UUID, amount: Double): EconomyError {
        if (Settlers.getSettler(uuid) == null) return EconomyError.SETTLER_NOT_FOUND
        Settlers.getSettler(uuid)!!.money = amount
        println("Setting balance of $uuid to $amount...")
        Database.setColWhere("settlers", "cash", "uuid", uuid, amount)
        println("Set $uuid's balance to $amount")
        return EconomyError.SUCCESS
    }

    /**
     * Adds money to a player's balance
     * @param uuid The UUID of the player
     * @param amount The amount to add to the balance
     * @return An EconomyError to indicate success or failure and the reason
     * @see EconomyError
     */
    fun deposit(uuid: UUID, amount: Double): EconomyError {
        if (Settlers.getSettler(uuid) == null) return EconomyError.SETTLER_NOT_FOUND
        setBalance(uuid, getBalance(uuid).first!! + amount)
        return EconomyError.SUCCESS
    }

    /**
     * Removes money from a player's balance
     * @param uuid The UUID of the player
     * @param amount The amount to remove from the balance
     * @return An EconomyError to indicate success or failure and the reason
     * @see EconomyError
     */
    fun withdraw(uuid: UUID, amount: Double): EconomyError {
        if (getBalance(uuid).first == null) return EconomyError.SETTLER_NOT_FOUND
        setBalance(uuid, getBalance(uuid).first!! - amount)
        return EconomyError.SUCCESS
    }

    /**
     * Transfers money from one player to another
     * @param from The UUID of the player to transfer from
     * @param to The UUID of the player to transfer to
     * @param amount The amount to transfer
     * @return An EconomyError to indicate success or failure and the reason
     * @see EconomyError
     */
    fun transfer(from: UUID, to: UUID, amount: Double): EconomyError {
        if (Settlers.getSettler(from) == null) return EconomyError.SETTLER_NOT_FOUND
        if (Settlers.getSettler(to) == null) return EconomyError.SETTLER_NOT_FOUND
        if (getBalance(from).first!! < amount) return EconomyError.INSUFFICIENT_FUNDS

        withdraw(from, amount)
        deposit(to, amount)
        return EconomyError.SUCCESS
    }
}