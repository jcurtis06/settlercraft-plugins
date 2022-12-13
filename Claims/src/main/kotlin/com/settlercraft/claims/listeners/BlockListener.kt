package com.settlercraft.claims.listeners

import com.settlercraft.claims.claim.ClaimManager
import com.settlercraft.claims.claim.Lock
import org.bukkit.block.Block
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockBurnEvent
import org.bukkit.event.block.BlockFromToEvent
import org.bukkit.event.block.BlockIgniteEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.entity.EntityExplodeEvent
import org.bukkit.event.player.PlayerBucketEmptyEvent
import org.bukkit.event.player.PlayerInteractEvent

class BlockListener: Listener {
    @EventHandler
    fun onBlockBreak(e: BlockBreakEvent) {
        if (ClaimManager.isInClaim(e.block.location)) {
            if (ClaimManager.getChunkLock(e.block.chunk, Lock.BUILD)) {
                e.isCancelled = true
                e.player.sendMessage("§cYou cannot break blocks in ${ClaimManager.getClaimOwner(e.block.chunk).name}'s territory!")
                return
            }
        }
    }

    @EventHandler
    fun onBlockExplode(e: EntityExplodeEvent) {
        val blocks: MutableList<Block> = mutableListOf()

        for (block in e.blockList()) {
            if (ClaimManager.isInClaim(block.location)) {
                if (ClaimManager.getChunkLock(block.chunk, Lock.BUILD)) {
                    blocks.add(block)
                }
            }
        }

        e.blockList().removeAll(blocks)
    }

    @EventHandler
    fun onBlockPlace(e: BlockPlaceEvent) {
        if (ClaimManager.isInClaim(e.block.location)) {
            if (ClaimManager.getChunkLock(e.block.chunk, Lock.BUILD)) {
                e.isCancelled = true
                e.player.sendMessage("§cYou cannot place blocks in ${ClaimManager.getClaimOwner(e.block.chunk).name}'s territory!")
                return
            }
        }
    }

    @EventHandler
    fun onBlockInteract(e: PlayerInteractEvent) {
        if (e.clickedBlock == null) return
        if (!e.clickedBlock!!.type.isInteractable) return
        if (ClaimManager.isInClaim(e.clickedBlock!!.location)) {
            if (ClaimManager.getChunkLock(e.clickedBlock!!.chunk, Lock.INTERACT)) {
                e.isCancelled = true
                e.player.sendMessage("§cYou interact with that in ${ClaimManager.getClaimOwner(e.clickedBlock!!.chunk).name}'s territory!")
            }
        }
    }

    @EventHandler
    fun onBucketEmpty(e: PlayerBucketEmptyEvent) {
        if (ClaimManager.isInClaim(e.blockClicked.location)) {
            if (ClaimManager.getChunkLock(e.blockClicked.chunk, Lock.BUILD)) {
                e.isCancelled = true
                e.player.sendMessage("§cYou cannot place blocks in ${ClaimManager.getClaimOwner(e.blockClicked.chunk).name}'s territory!")
            }
        }
    }

    @EventHandler
    fun liquidFlowEvent(e: BlockFromToEvent) {
        val toBlock = e.toBlock

        var toLock = false

        if (ClaimManager.isInClaim(toBlock.location)) {
            if (ClaimManager.getChunkLock(toBlock.chunk, Lock.BUILD))
                toLock = true
        }

        if (toLock)
            e.isCancelled = true
    }

    @EventHandler
    fun onBlockBurn(e: BlockBurnEvent) {
        if (ClaimManager.isInClaim(e.block.location)) {
            if (ClaimManager.getChunkLock(e.block.chunk, Lock.BUILD))
                e.isCancelled = true
        }
    }

    @EventHandler
    fun onFireSpread(e: BlockIgniteEvent) {
        if (ClaimManager.isInClaim(e.block.location)) {
            if (ClaimManager.getChunkLock(e.block.chunk, Lock.BUILD))
                e.isCancelled = true
        }
    }
}