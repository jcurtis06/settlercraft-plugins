package com.settlercraft.claims.claim

import org.bukkit.Chunk
import org.bukkit.Location
import org.bukkit.persistence.PersistentDataType
import java.util.UUID

class ClaimedChunk(var location: Location, var owner: UUID) {
    constructor(chunk: Chunk, owner: UUID) : this(
        Location(chunk.world, chunk.x.shl(4).toDouble(), 0.0, chunk.z.shl(4).toDouble()), owner
    )

    fun updatePersistentData() {
        val container = location.chunk.persistentDataContainer
        container.set(ClaimManager.claimedKey, PersistentDataType.STRING, owner.toString())
    }

    fun delPersistentData() {
        val container = location.chunk.persistentDataContainer
        container.remove(ClaimManager.claimedKey)
    }
}