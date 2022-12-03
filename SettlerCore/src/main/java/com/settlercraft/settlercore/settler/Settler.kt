package com.settlercraft.settlercore.settler

import com.settlercraft.settlercore.econ.Wallet
import java.util.UUID

class Settler(val uuid: UUID) {
    val wallet = Wallet(this)
}