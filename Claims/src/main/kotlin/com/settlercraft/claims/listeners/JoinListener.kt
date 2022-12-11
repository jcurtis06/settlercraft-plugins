package com.settlercraft.claims.listeners

import com.settlercraft.claims.claim.ClaimManager
import com.settlercraft.settlercore.settler.Settlers
import com.settlercraft.settlercore.settler.actionbar.StatusMessage
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent

class JoinListener: Listener {
    @EventHandler
    fun onJoin(e: PlayerJoinEvent) {
        val settler = Settlers.getSettler(e.player.uniqueId) ?: return
        val msg = StatusMessage("claim-${e.player.uniqueId}") {
            val player = Bukkit.getPlayer(settler.uuid)

            if (ClaimManager.isInClaim(player!!.location)) {
                "§c${ClaimManager.getClaimOwner(player.location.chunk).name}"
            } else {
                "§aWild"
            }
        }

        settler.addStatusMsg(msg)
    }

    @EventHandler
    fun onLeave(e: PlayerQuitEvent) {
        val settler = Settlers.getSettler(e.player.uniqueId) ?: return
        settler.removeStatusMsg("claim-${e.player.uniqueId}")
    }
}