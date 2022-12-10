package com.settlercraft.claims.listeners

import com.settlercraft.claims.AuthorityLevel
import com.settlercraft.claims.claim.ClaimHandler
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.player.PlayerInteractEvent

class BlockListener: Listener {
    @EventHandler
    fun onBlockBreak(e: BlockBreakEvent) {
        val territory = ClaimHandler.getTerritoryAt(e.block.location) ?: return

        if (territory.getAuthorityLevel(e.player.uniqueId) < AuthorityLevel.FULL_ACCESS) {
            e.isCancelled = true
            return
        }
    }

    @EventHandler
    fun onBlockPlace(e: BlockPlaceEvent) {
        val territory = ClaimHandler.getTerritoryAt(e.block.location) ?: return

        if (territory.getAuthorityLevel(e.player.uniqueId) < AuthorityLevel.FULL_ACCESS) {
            e.isCancelled = true
            return
        }
    }

    @EventHandler
    fun onInteract(e: PlayerInteractEvent) {
        val block = e.clickedBlock ?: return

        val territory = ClaimHandler.getTerritoryAt(block.location) ?: return

        if (territory.getAuthorityLevel(e.player.uniqueId) < AuthorityLevel.INTERACT_ONLY) {
            e.isCancelled = true
            return
        }
    }
}