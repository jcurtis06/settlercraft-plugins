package com.settlercraft.settlercore.econ

import com.settlercraft.settlercore.settler.Settler

data class Wallet(val owner: Settler, var cash: Double = 0.0)