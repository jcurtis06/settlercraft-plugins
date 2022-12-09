package com.settlercraft.claims

import org.bukkit.util.Vector
import java.util.UUID
import kotlin.math.floor
import kotlin.math.roundToInt

class Claim(var location: Vector, var ownerUuid: UUID) {
    constructor(x: Int, z: Int, ownerUuid: UUID) : this(Vector(x, 0, z), ownerUuid)
    fun isInClaim(point: Vector): Boolean {
        var x = floor(point.x.times(16)).div(16.0)
        var z = point.z
        var inXBounds = (x >= location.x && x < location.x + 16)
        var inZBounds = (z >= location.z && z < location.z + 16)
        if (inXBounds && inZBounds)
            return true
        return false
    }
}