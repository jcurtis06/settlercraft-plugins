package com.settlercraft.settlercore.settler

import com.settlercraft.settlercore.data.Database
import java.util.*

object Settlers {
    private val settlers: MutableList<Settler> = mutableListOf()

    fun getSettler(uuid: UUID): Settler? {
        return settlers.find { it.uuid == uuid }
    }

    fun loadSettlers() {
        Database.connect()
        val con = Database.connection

        val stmt = con.createStatement()
        val rs = stmt.executeQuery("SELECT * FROM settlers")
        while (rs.next()) {
            val uuid = rs.getString("uuid")
            val cash = rs.getDouble("cash")
            if (uuid != null) {
                registerSettler(UUID.fromString(uuid), cash)
                println("Loaded Settler $uuid with $$cash")
            }
        }
    }

    private fun registerSettler(uuid: UUID, cash: Double = 0.0) {
        if (getSettler(uuid) != null) return

        val settler = Settler(uuid)
        settler.wallet.cash = cash
        settlers.add(settler)
    }
}