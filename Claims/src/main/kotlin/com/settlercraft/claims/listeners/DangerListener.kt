package com.settlercraft.claims.listeners

import com.settlercraft.claims.claim.ClaimManager
import org.bukkit.Location
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.world.ChunkLoadEvent

class DangerListener: Listener {
    @EventHandler
    fun onChunkLoad(e: ChunkLoadEvent) {
        if (ClaimManager.isInClaim(Location(e.world, e.chunk.x.toDouble(), 0.0, e.chunk.z.toDouble()))) {
            val container = e.chunk.persistentDataContainer
            container.remove(ClaimManager.claimedKey)
        }
    }
}