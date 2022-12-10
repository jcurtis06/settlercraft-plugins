package com.settlercraft.claims.listeners

import com.settlercraft.claims.claim.ClaimManager
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.entity.EntityExplodeEvent
import org.bukkit.event.player.PlayerCommandSendEvent
import org.bukkit.event.player.PlayerInteractEvent

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
        val blocks: MutableList<Block> = mutableListOf()

        for (block in e.blockList()) {
            if (ClaimManager.isInClaim(block.location)) {
                blocks.add(block)
            }
        }

        e.blockList().removeAll(blocks)
    }

    @EventHandler
    fun onBlockPlace(e: BlockPlaceEvent) {
        if (ClaimManager.isInClaim(e.block.location)) {
            e.isCancelled = true
            e.player.sendMessage("§cYou cannot place blocks in ${ClaimManager.getClaimOwner(e.block.chunk).name}'s territory!")
        }
    }

    @EventHandler
    fun onInteract(e: PlayerInteractEvent) {
        if (e.clickedBlock == null) return
        if (!e.clickedBlock!!.type.isInteractable) return
        if (ClaimManager.isInClaim(e.clickedBlock!!.location)) {
            e.isCancelled = true
            e.player.sendMessage("§cOi m8, u can't do that in ${ClaimManager.getClaimOwner(e.clickedBlock!!.chunk).name}'s territory!")
        }
    }
}