package com.settlercraft.claims.claim

import org.bukkit.Location
import java.util.UUID
import kotlin.math.floor

class Claim(var location: Location, var ownerUuid: UUID) {
    fun isInClaim(point: Location): Boolean {
        val x = floor(point.x.times(16)).div(16.0)
        val z = floor(point.z.times(16)).div(16.0)
        val inXBounds = (x >= location.x && x < location.x + 16)
        val inZBounds = (z >= location.z && z < location.z + 16)
        if (inXBounds && inZBounds)
            return true
        return false
    }

    /*
        val chunk = player.location.chunk
        val worldChunkX = chunk.x.shl(4)
        val worldChunkZ = chunk.z.shl(4)

        val bottomLeft = BlockVector3.at(worldChunkX, 0, worldChunkZ)
        val topRight = BlockVector3.at(worldChunkX + 16, 0, worldChunkZ + 16)
     */
}