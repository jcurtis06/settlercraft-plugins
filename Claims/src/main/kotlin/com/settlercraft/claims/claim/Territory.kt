package com.settlercraft.claims.claim

import com.settlercraft.claims.AuthorityLevel
import org.bukkit.Location
import java.util.*
import kotlin.collections.ArrayList

@Suppress("unused", "MemberVisibilityCanBePrivate")
class Territory(var ownerUuid: UUID) {
    private val uuidToAuthority = mapOf<UUID, AuthorityLevel>()
    private val timeBeforeRepo = 1000
    private val innerClaims = ArrayList<ClaimedChunk>()

    private var timeSinceLastReclaim = timeBeforeRepo

    fun tryConnect(claim: ClaimedChunk): ClaimStatus {
        println("Trying to connect claim to territory")
        if (getAuthorityLevel(claim.owner) != AuthorityLevel.CO_OWNER)
            return ClaimStatus.FAILED_TO_CONNECT
        for (otherClaim in innerClaims) {
            if (
                    otherClaim.location.x - 16 == claim.location.x ||
                    otherClaim.location.x + 16 == claim.location.x ||
                    otherClaim.location.z - 16 == claim.location.z ||
                    otherClaim.location.z + 16 == claim.location.z
                ) {
                innerClaims.add(otherClaim)
                println("Added inner claim ${otherClaim.location}")
                return ClaimStatus.SUCCESS
            }
        }
        return ClaimStatus.FAILED_TO_CONNECT
    }

    fun isInTerritory(point: Location): Boolean {
        for (claim in innerClaims)
            if (ClaimManager.isInClaim(point))
                return true
        return false
    }

    fun getAuthorityLevel(uuid: UUID): AuthorityLevel {
        if (uuid !in uuidToAuthority)
            return AuthorityLevel.NO_CHANGE
        if (uuid == ownerUuid)
            return AuthorityLevel.CO_OWNER
        return uuidToAuthority[uuid]!!
    }

    fun deleteClaim(claim: ClaimedChunk) = innerClaims.remove(claim)

    fun forceAddClaim(claim: ClaimedChunk) = innerClaims.add(claim)

    fun needsRepo() = (timeSinceLastReclaim > 0)

    fun tick() = --timeSinceLastReclaim

    fun resetRepoClock() = timeBeforeRepo
}
