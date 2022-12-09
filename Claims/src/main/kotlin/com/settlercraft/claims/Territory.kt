package com.settlercraft.claims

import org.bukkit.util.Vector
import java.util.*
import kotlin.collections.ArrayList

class Territory(var ownerUuid: UUID) {
    private val uuidToAuthority = mapOf<UUID, AuthorityLevel>()
    private val timeBeforeRepo = 1000
    private val innerClaims = ArrayList<Claim>()

    private var timeSinceLastReclaim = timeBeforeRepo
    fun tryConnect(claim: Claim): ClaimError {
        if (getAuthorityLevel(claim.ownerUuid) != AuthorityLevel.CO_OWNER)
            return ClaimError.FAILED_TO_CONNECT
        for (otherClaim in innerClaims) {
            if (
                    otherClaim.location.x - 16 == claim.location.x ||
                    otherClaim.location.x + 16 == claim.location.x ||
                    otherClaim.location.z - 16 == claim.location.z ||
                    otherClaim.location.z + 16 == claim.location.z
                ) {
                innerClaims.add(otherClaim)
                return ClaimError.SUCCESS
            }
        }
        return ClaimError.FAILED_TO_CONNECT
    }

    fun isInTerritory(point: Vector): Boolean {
        for (claim in innerClaims)
            if (claim.isInClaim(point))
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

    fun deleteClaim(claim: Claim) = innerClaims.remove(claim)
    fun forceAddClaim(claim: Claim) = innerClaims.add(claim)

    fun needsRepo() = (timeSinceLastReclaim > 0)
    fun tick() = --timeSinceLastReclaim
}