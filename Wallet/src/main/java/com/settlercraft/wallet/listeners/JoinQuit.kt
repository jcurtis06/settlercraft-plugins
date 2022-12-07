package com.settlercraft.wallet.listeners

import com.settlercraft.settlercore.settler.Settlers
import com.settlercraft.settlercore.settler.actionbar.StatusMessage
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent

class JoinQuit: Listener {
    @EventHandler
    fun onJoin(e: PlayerJoinEvent) {
        Settlers.getSettler(e.player.uniqueId)?.let {
            it.addStatusMsg(StatusMessage("money-${it.name}") { it.money.toInt().toString() })
        }
    }

    @EventHandler
    fun onLeave(e: PlayerQuitEvent) {
        Settlers.getSettler(e.player.uniqueId)?.removeStatusMsg("money-${e.player.name}")
    }
}