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
            val name = rs.getString("name")
            if (uuid != null) {
                registerSettler(UUID.fromString(uuid), name, cash)
                println("Loaded Settler ${name} with $$cash (${uuid})")
            }
        }
    }

    /**
     * Reloads the settlers from the database
     */
    fun reloadSettler(name: String) {
        Database.connect()
        val con = Database.connection

        val stmt = con.createStatement()
        val rs = stmt.executeQuery("SELECT * FROM settlers WHERE name = '$name'")
        while (rs.next()) {
            val uuid = rs.getString("uuid")
            val cash = rs.getDouble("cash")
            val name = rs.getString("name")
            if (uuid != null) {
                getSettler(UUID.fromString(uuid))!!.money = cash

                println("Reloaded Settler ${name} with $$cash (${uuid})")
            }
        }
    }

    private fun registerSettler(uuid: UUID, name: String, cash: Double = 0.0) {
        if (getSettler(uuid) != null) return
        settlers.add(Settler(uuid, name, cash))
    }
}