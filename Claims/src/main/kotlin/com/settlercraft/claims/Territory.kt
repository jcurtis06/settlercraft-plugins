package com.settlercraft.claims

import java.util.*
import kotlin.collections.ArrayList

class Territory(var ownerUuid: UUID) {
    private val timeBeforeRepo = 1000
    private val innerClaims = ArrayList<Claim>()

    private var timeSinceLastReclaim = timeBeforeRepo
    fun tryConnect(claim: Claim): ClaimError {
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

    fun forceAddClaim(claim: Claim) = innerClaims.add(claim)

    fun needsRepo() = (timeSinceLastReclaim > 0)
    fun tick() = --timeSinceLastReclaim
}