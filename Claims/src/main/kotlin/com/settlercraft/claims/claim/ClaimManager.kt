package com.settlercraft.claims.claim

import com.settlercraft.claims.Claims
import com.settlercraft.settlercore.data.Database
import com.settlercraft.settlercore.settler.Settlers
import org.bukkit.Chunk
import org.bukkit.Location
import org.bukkit.NamespacedKey
import org.bukkit.OfflinePlayer
import org.bukkit.persistence.PersistentDataType
import org.bukkit.plugin.java.JavaPlugin
import java.util.*
import kotlin.math.pow

object ClaimManager {
    /*
    - Wars can be started against online players
    - When a war is started, the defender gets x amount of days to prepare
     */
    private val claims = mutableListOf<ClaimedChunk>()

    private val playerClaims = mutableMapOf<UUID, MutableList<ClaimedChunk>>()

    // private val territories = mutableListOf<Territory>()
    private const val flatPrice: Double = 20.0
    private const val perClaimPrice: Double = 1.5
    val claimedKey = NamespacedKey(Claims.instance as JavaPlugin, "claimed-chunk")

    fun isInClaim(point: Location): Boolean {
        val container = point.chunk.persistentDataContainer
        return container.has(claimedKey, PersistentDataType.STRING)
    }

    fun getClaimOwner(chunk: Chunk): OfflinePlayer {
        val container = chunk.persistentDataContainer
        val uuid = UUID.fromString(container.get(claimedKey, PersistentDataType.STRING))
        return Claims.instance!!.server.getOfflinePlayer(uuid)
    }

    fun addClaim(claim: ClaimedChunk): ClaimStatus {
        //if (isInClaim(claim.location)) return ClaimStatus.FAILED_TO_CLAIM
        claims.add(claim)
        claim.updatePersistentData()
        playerClaims.putIfAbsent(claim.owner, mutableListOf())
        playerClaims[claim.owner]!!.add(claim)
        //tryConnect(claim)
        val settler = Settlers.getSettler(claim.owner)!!
        settler.chunks += 1

        val con = Database.connect()
        val stmt = con.prepareStatement("INSERT INTO chunks (chunk_key, owner) VALUES (?, ?)")
        stmt.setString(1, claim.location.chunk.chunkKey.toString())
        stmt.setString(2, claim.owner.toString())
        stmt.execute()

        println("Added claim lol")
        return ClaimStatus.SUCCESS
    }

    fun loadClaims() {
        var count = 0

        val con = Database.connect()
        val stmt = con.prepareStatement("SELECT * FROM chunks")
        val rs = stmt.executeQuery()
        while (rs.next()) {
            count++
            val chunkKey = rs.getString("chunk_key")
            val owner = UUID.fromString(rs.getString("owner"))
            val chunk = Claims.instance!!.server.getWorld("world")!!.getChunkAt(chunkKey.toLong())
            val claim = ClaimedChunk(chunk, owner)
            playerClaims.putIfAbsent(claim.owner, mutableListOf())
            playerClaims[claim.owner]!!.add(claim)
        }

        println("Loaded $count claims")
    }

/*
    fun getTerritoryAt(point: Location): Territory? {
        val x: Int = floor(point.x.div(16)).times(16).toInt()
        val z: Int = floor(point.z.div(16)).times(16).toInt()
        for (territory in territories)
            if (territory.isInTerritory(point))
                return territory
        return null
    }

    private fun tryConnect(claim: ClaimedChunk) {
        var found = false
        var ter: Territory?
        for (territory in territories)
            if (territory.tryConnect(claim) == ClaimError.SUCCESS) {
                found = true
                break
            }
        if (!found) {
            ter = Territory(claim.owner)
            ter.forceAddClaim(claim)
            territories.add(ter)
        }
    }

    fun deleteClaim(claim: ClaimedChunk): ClaimError {
        if (claim !in claims)
            return ClaimError.FAILED_TO_UNCLAIM
        claims.remove(claim)
        getTerritoryAt(claim.location)?.deleteClaim(claim)
        return ClaimError.SUCCESS
    }
    fun numOfClaimsBy(uuid: UUID): Int {
        var count = 0
        for (claim in claims)
            if (claim.owner == uuid)
                count++
        return count
    }
*/
    fun priceOfLandFor(uuid: UUID) = flatPrice * perClaimPrice.pow(Settlers.getSettler(uuid)!!.chunks)
}