package com.settlercraft.claims.claim

import org.bukkit.Location
import org.bukkit.util.Vector
import java.util.*
import kotlin.math.floor
import kotlin.math.pow

object ClaimHandler {
    private val claims = mutableListOf<Claim>()
    private val territories = mutableListOf<Territory>()
    private const val flatPrice: Double = 20.0
    private const val perClaimPrice: Double = 1.5

    fun getClaims() = claims

    fun getClaimAt(point: Location): Claim? {
        val x: Int = floor(point.x.div(16)).times(16).toInt()
        val z: Int = floor(point.z.div(16)).times(16).toInt()
        for (claim in claims)
            if (claim.isInClaim(point))
                return claim
        return null
    }

    fun getTerritoryAt(point: Location): Territory? {
        val x: Int = floor(point.x.div(16)).times(16) as Int
        val z: Int = floor(point.z.div(16)).times(16) as Int
        for (territory in territories)
            if (territory.isInTerritory(point))
                return territory
        return null
    }

    private fun tryConnect(claim: Claim) {
        var found = false
        var ter: Territory?
        for (territory in territories)
            if (territory.tryConnect(claim) == ClaimError.SUCCESS) {
                found = true
                break
            }
        if (!found) {
            ter = Territory(claim.ownerUuid)
            ter.forceAddClaim(claim)
            territories.add(ter)
        }
    }

    fun addClaim(claim: Claim): ClaimError {
        if (getClaimAt(claim.location) != null)
            return ClaimError.FAILED_TO_CLAIM
        claims.add(claim)
        tryConnect(claim)
        return ClaimError.SUCCESS
    }

    fun deleteClaim(claim: Claim): ClaimError {
        if (claim !in claims)
            return ClaimError.FAILED_TO_UNCLAIM
        claims.remove(claim)
        getTerritoryAt(claim.location)?.deleteClaim(claim)
        return ClaimError.SUCCESS
    }

    fun numOfClaimsBy(uuid: UUID): Int {
        var count = 0
        for (claim in claims)
            if (claim.ownerUuid == uuid)
                count++
        return count
    }

    fun priceOfLandFor(uuid: UUID) = flatPrice * perClaimPrice.pow(numOfClaimsBy(uuid))
}