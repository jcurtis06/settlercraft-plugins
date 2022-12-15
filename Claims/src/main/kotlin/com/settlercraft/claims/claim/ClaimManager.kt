package com.settlercraft.claims.claim

import com.settlercraft.claims.Claims
import com.settlercraft.settlercore.data.Database
import com.settlercraft.settlercore.settler.Settlers
import com.settlercraft.settlercore.settler.actionbar.StatusMessage
import org.bukkit.*
import org.bukkit.entity.Player
import org.bukkit.persistence.PersistentDataType
import org.bukkit.plugin.java.JavaPlugin
import java.util.*
import kotlin.math.pow

object ClaimManager {
    val claims = mutableListOf<ClaimedChunk>()

    private val playerClaims = mutableMapOf<UUID, MutableList<ClaimedChunk>>()
    private val playerLocks = mutableMapOf<UUID, ClaimLock>()

    private const val flatPrice: Double = 20.0
    private const val perClaimPrice: Double = 1.5
    val claimedKey = NamespacedKey(Claims.instance as JavaPlugin, "claimed-chunk")

    fun getPlayerLock(uuid: UUID): ClaimLock {
        return playerLocks.getOrPut(uuid) { ClaimLock() }
    }

    fun getChunkLock(chunk: Chunk, lock: Lock): Boolean {
        return getPlayerLock(getClaimOwner(chunk).uniqueId).getLock(lock)
    }

    fun toggleLock(uuid: UUID, lock: Lock) {
        val playerLock = getPlayerLock(uuid)
        setLock(uuid, lock, !playerLock.getLock(lock))
    }

    fun setLock(uuid: UUID, lock: Lock, value: Boolean) {
        val playerLock = getPlayerLock(uuid)
        playerLock.locks[lock] = value

        val con = Database.connect()
        val stmt = con.prepareStatement("UPDATE chunks SET ${lock.name} = ? WHERE owner = ?")
        stmt.setBoolean(1, value)
        stmt.setString(2, uuid.toString())
        stmt.executeUpdate()
        stmt.close()
    }

    fun isInClaim(point: Location): Boolean {
        val container = point.chunk.persistentDataContainer
        return container.has(claimedKey, PersistentDataType.STRING)
    }

    fun getClaimOwner(chunk: Chunk): OfflinePlayer {
        val container = chunk.persistentDataContainer
        val uuid = UUID.fromString(container.get(claimedKey, PersistentDataType.STRING))
        return Claims.instance!!.server.getOfflinePlayer(uuid)
    }

    fun getClaimedChunk(point: Location): ClaimedChunk? {
        return claims.firstOrNull { it.location.chunk.chunkKey == point.chunk.chunkKey }
    }

    fun addClaim(claim: ClaimedChunk): ClaimStatus {
        claims.add(claim)
        claim.updatePersistentData()
        playerClaims.putIfAbsent(claim.owner, mutableListOf())
        playerClaims[claim.owner]!!.add(claim)
        val settler = Settlers.getSettler(claim.owner)

        if (settler == null) {
            println("Settler ${claim.owner} is null")
            return ClaimStatus.SUCCESS
        }

        settler.chunks++
        Database.setColWhere("settlers", "chunks", "uuid", claim.owner.toString(), settler.chunks)

        val con = Database.connect()
        val stmt = con.prepareStatement("INSERT INTO chunks (chunk_key, owner) VALUES (?, ?)")
        stmt.setString(1, claim.location.chunk.chunkKey.toString())
        stmt.setString(2, claim.owner.toString())
        stmt.execute()

        return ClaimStatus.SUCCESS
    }

    fun delClaim(claim: ClaimedChunk, uuid: UUID): ClaimStatus {
        if (claim.owner != uuid)
            return ClaimStatus.NOT_YOURS
        claims.remove(claim)
        claim.delPersistentData()
        playerClaims[claim.owner]!!.remove(claim)

        val settler = Settlers.getSettler(claim.owner)!!

        settler.chunks--
        Database.setColWhere("settlers", "chunks", "uuid", claim.owner.toString(), settler.chunks)

        val con = Database.connect()
        val stmt = con.prepareStatement("DELETE FROM chunks WHERE chunk_key = ?")
        stmt.setString(1, claim.location.chunk.chunkKey.toString())
        stmt.execute()

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
            val interact = rs.getBoolean("INTERACT")
            val build = rs.getBoolean("BUILD")
            val pvp = rs.getBoolean("PVP")

            val lock = ClaimLock()
            lock.locks[Lock.INTERACT] = interact
            lock.locks[Lock.BUILD] = build
            lock.locks[Lock.PVP] = pvp

            playerLocks[owner] = lock

            playerClaims.putIfAbsent(claim.owner, mutableListOf())
            playerClaims[claim.owner]!!.add(claim)
            claims.add(claim)
        }

        println("Loaded ${claims.size} claims")
    }

    fun setStatusMsg(p: Player) {
        val settler = Settlers.getSettler(p.uniqueId) ?: return
        val msg = StatusMessage("claim-${p.uniqueId}") {
            val player = Bukkit.getPlayer(settler.uuid)

            if (isInClaim(player!!.location)) {
                "§c${getClaimOwner(player.location.chunk).name}"
            } else {
                "§aWild"
            }
        }

        settler.addStatusMsg(msg)
    }

    fun priceOfLandFor(uuid: UUID) = onLogNFunction(Settlers.getSettler(uuid)!!.chunks) * 20

    private fun onLogNFunction(n: Int): Int {
        var result = 0
        var n = n
        while (n > 0) {
            result += n
            n = n shr 1
        }
        return result
    }
}