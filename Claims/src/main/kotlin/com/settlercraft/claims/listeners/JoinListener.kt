package com.settlercraft.claims.listeners

import com.settlercraft.claims.claim.ClaimHandler
import com.settlercraft.settlercore.settler.Settlers
import com.settlercraft.settlercore.settler.actionbar.StatusMessage
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

class JoinListener: Listener {
    @EventHandler
    fun onJoin(e: PlayerJoinEvent) {
        val settler = Settlers.getSettler(e.player.uniqueId) ?: return

        val msg = StatusMessage("claim-${settler.uuid}") {
            val player = Bukkit.getPlayer(settler.uuid) ?: return@StatusMessage ""

            if (ClaimHandler.getClaimAt(player.location.toVector()) != null) {
                "You are in a claim!"
            } else {
                "You are outside of a claim!"
            }
        }

        settler.addStatusMsg(msg)
    }
}