package com.settlercraft.wallet.listeners

import com.settlercraft.wallet.managers.PlayerManager
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent

class JoinLeave: Listener {
    @EventHandler
    fun onJoin(e: PlayerJoinEvent) {
        PlayerManager.registerPlayer(e.player.uniqueId)
    }

    @EventHandler
    fun onLeave(e: PlayerQuitEvent) {
        PlayerManager.unregisterPlayer(e.player.uniqueId)
    }
}