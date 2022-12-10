package com.settlercraft.claims.listeners

import com.settlercraft.claims.claim.ClaimHandler
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent

class BlockListener: Listener {
    @EventHandler
    fun onBlockBreak(event: BlockBreakEvent) {
        val isClaimed = ClaimHandler.getClaimAt(event.block.location.toVector()) != null

        if (isClaimed) {
            event.isCancelled = true
        }
    }
}