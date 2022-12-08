package com.settlercraft.claims

import org.bukkit.util.Vector
import java.util.UUID

class Claim(var location: Vector, var owneruuid: UUID) {

    constructor(x: Int, y: Int, owneruuid: UUID) : this(Vector(x, y, 0), owneruuid)
    fun isInClaim(point: Vector): Boolean {
        var x = point.x
        var y = point.y
        var inXBounds = (x >= location.x && x < location.x + 16)
        var inYBounds = (y >= location.y && y < location.y + 16)
        if (inXBounds && inYBounds)
            return true
        return false
    }
}