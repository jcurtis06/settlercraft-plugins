package com.settlercraft.claims.listeners

import com.settlercraft.claims.claim.ClaimHandler
import com.settlercraft.settlercore.settler.Settlers
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerMoveEvent

class MovementListener: Listener {
    @EventHandler
    fun onMove(e: PlayerMoveEvent) {
        val from = e.from
        val to = e.to

        if (from.chunk != to.chunk) {
            val settler = Settlers.getSettler(e.player.uniqueId) ?: return

            if (ClaimHandler.getClaimAt(to.toVector()) != null) {
                e.player.sendMessage("You are now in a claim!")
            } else {
                // todo: Add status message
                e.player.sendMessage("You are now outside of a claim!")
            }
        }
    }
}