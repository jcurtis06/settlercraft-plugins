package com.settlercraft.claims.listeners

import com.settlercraft.claims.claim.ClaimManager
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerMoveEvent

class MovementListener: Listener {
    @EventHandler
    fun onMove(e: PlayerMoveEvent) {
        val from = e.from
        val to = e.to

        if (from.chunk != to.chunk) {
            if (ClaimManager.isInClaim(to)) {
                e.player.sendMessage("You are now in a claim!")
            } else {
                // todo: Add status message
                e.player.sendMessage("You are now outside of a claim!")
            }
        }
    }
}