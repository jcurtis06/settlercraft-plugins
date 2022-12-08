package com.settlercraft.claims

import org.bukkit.util.Vector
import java.util.*
import kotlin.collections.ArrayList

class ClaimHandler {
    private var claims = ArrayList<Claim>()
    private var claimsByuuid = HashMap<UUID, ArrayList<Claim>>()
    private val flatPrice: Double = 20.0
    private val perClaimPrice: Double = 1.5
    fun isClaimed(point: Vector): Boolean {
        for (claim in claims)
            if (claim.isInClaim(point))
                return true
        return false
    }

    fun addClaim(uuidOfClaimer: UUID, claim: Claim): Boolean {
        if (isClaimed(claim.location))
            return false
        if (uuidOfClaimer !in claimsByuuid) {
            claimsByuuid[uuidOfClaimer] = ArrayList<Claim>()
            claimsByuuid[uuidOfClaimer]!!.add(claim)
        }
        claims.add(claim)
        claimsByuuid[uuidOfClaimer]!!.add(claim)
        return true
    }

    fun deleteClaim(claim: Claim): Boolean {
        if (claim !in claims)
            return false
        claims.remove(claim)
        claimsByuuid.get(claim.owneruuid)!!.remove(claim)
        return true
    }

    fun numOfClaimsBy(uuid: UUID): Int {
        if (uuid !in claimsByuuid)
            return 0
        return claimsByuuid[uuid]!!.size
    }

    fun priceOfLandFor(uuid: UUID): Double {
        if (uuid !in claimsByuuid)
            return flatPrice
        return flatPrice * Math.pow(perClaimPrice, claimsByuuid[uuid]!!.size.toDouble())
    }
}