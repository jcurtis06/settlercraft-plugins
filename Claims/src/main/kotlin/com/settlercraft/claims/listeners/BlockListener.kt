package com.settlercraft.claims.listeners

import com.settlercraft.claims.AuthorityLevel
import com.settlercraft.claims.claim.ClaimManager
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockExplodeEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.entity.EntityExplodeEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.world.ChunkLoadEvent

class BlockListener: Listener {
    @EventHandler
    fun onBlockBreak(e: BlockBreakEvent) {
        if (ClaimManager.isInClaim(e.block.location)) {
            e.isCancelled = true
            e.player.sendMessage("§cYou cannot break blocks in ${ClaimManager.getClaimOwner(e.block.chunk).name}'s territory!")
        }
    }

    @EventHandler
    fun onBlockExplode(e: EntityExplodeEvent) {
        println("Entity Explode")

    }

    @EventHandler
    fun onBlockPlace(e: BlockPlaceEvent) {

    }

    @EventHandler
    fun onInteract(e: PlayerInteractEvent) {

    }
}