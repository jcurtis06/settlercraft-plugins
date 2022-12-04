package com.settlercraft.settlercore.settler

import java.util.UUID

data class Settler(val uuid: UUID, var name: String, var money: Double = 0.0)