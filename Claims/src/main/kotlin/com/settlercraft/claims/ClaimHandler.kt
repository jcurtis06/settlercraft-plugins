package com.settlercraft.claims

import org.bukkit.util.Vector
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.pow
import kotlin.math.roundToInt

object ClaimHandler {
    private val claims = mutableListOf<Claim>()
    private val claimsByUuid = HashMap<UUID,  MutableList<Claim>>()
    private val claimsByXZ = HashMap<String, Claim>()
    private val territories = mutableListOf<Territory>()
    private val territoriesByUuid= HashMap<UUID,  MutableList<Territory>>()
    private val territoriesByXZ = HashMap<String,  Territory>()
    private const val flatPrice: Double = 20.0
    private const val perClaimPrice: Double = 1.5

    fun getClaims() = claims
    fun getClaimAt(point: Vector): Claim? {
        val x: Double = point.x.times(16).roundToInt().div(16.0)
        val z: Double = point.z.times(16).roundToInt().div(16.0)
        return claimsByXZ["$x,$z"]
    }

    fun tryConnect(claim: Claim) {
        var found = false
        var ter: Territory? = null
        for (territory in territories)
            if (territory.tryConnect(claim) == ClaimError.SUCCESS) {
                found = true
                ter = territory
                break
            }
        if (!found) {
            ter = Territory(claim.ownerUuid)
            ter.forceAddClaim(claim)
        }
        if (claim.ownerUuid !in territoriesByUuid)
            territoriesByUuid[claim.ownerUuid] = mutableListOf()
        ter!!
        // I love the idea that this is just me yelling "ter" at the computer for absolutely no reason
        territoriesByUuid[claim.ownerUuid]!!.add(ter)
        territoriesByXZ[claim.location.x.toString() + "," + claim.location.z.toString()] = ter
    }

    fun addClaim(claim: Claim): ClaimError {
        if (getClaimAt(claim.location) != null)
            return ClaimError.FAILED_TO_CLAIM
        if (claim.ownerUuid !in claimsByUuid)
            claimsByUuid[claim.ownerUuid] = mutableListOf()
        claims.add(claim)
        claimsByUuid[claim.ownerUuid]!!.add(claim)
        claimsByXZ[claim.location.x.toString() + "," + claim.location.z.toString()] = claim
        tryConnect(claim)
        return ClaimError.SUCCESS
    }

    fun deleteClaim(claim: Claim): ClaimError {
        if (claim !in claims)
            return ClaimError.FAILED_TO_UNCLAIM
        claims.remove(claim)
        claimsByUuid[claim.ownerUuid]!!.remove(claim)
        return ClaimError.SUCCESS
    }

    fun numOfClaimsBy(uuid: UUID): Int {
        if (uuid !in claimsByUuid)
            return 0
        return claimsByUuid[uuid]!!.size
    }

    fun priceOfLandFor(uuid: UUID): Double {
        if (uuid !in claimsByUuid)
            return flatPrice
        return flatPrice * perClaimPrice.pow(claimsByUuid[uuid]!!.size.toDouble())
    }
}