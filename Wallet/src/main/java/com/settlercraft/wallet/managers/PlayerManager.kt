package com.settlercraft.wallet.managers

import com.settlercraft.wallet.player.PlayerWallet
import java.util.UUID

object PlayerManager {
    val players = mutableListOf<PlayerWallet>()

    fun getWallet(uuid: UUID): PlayerWallet? {
        return players.firstOrNull { it.uuid == uuid }
    }

    fun registerPlayer(uuid: UUID) {
        DataManager.registerIfAbsent(uuid, 0.0)
        players.add(PlayerWallet(uuid, DataManager.getColWhere("balance", "uuid", uuid.toString())))
    }

    fun unregisterPlayer(uuid: UUID) {
        players.removeIf { it.uuid == uuid }
    }
}