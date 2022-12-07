package com.settlercraft.settlercore.settler

import com.settlercraft.settlercore.SettlerCore
import com.settlercraft.settlercore.data.Database
import org.bukkit.scheduler.BukkitRunnable
import java.util.*

@Suppress("MemberVisibilityCanBePrivate")
object Settlers {
    private val settlers: MutableList<Settler> = mutableListOf()
    private val blankUUID = UUID.fromString("00000000-0000-0000-0000-000000000000")

    /**
     * Gets a settler by their UUID
     * @param uuid The UUID of the settler
     * @return The settler, or null if not found
     * @see Settler
     * @see settlerUUID
     */
    fun getSettler(uuid: UUID): Settler? {
        return settlers.find { it.uuid == uuid }
    }

    /**
     * Gets a settler's UUID by their name
     * @param name The name of the settler
     * @return The UUID of the settler, or an empty UUID if not found
     * @see Settler
     */
    fun settlerUUID(name: String): UUID? {
        return settlers.find { it.name == name }?.uuid
    }

    /**
     * Loads all settlers from the database
     * @see Settler
     * @see Database
     */
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
                println("Loaded Settler $name with $$cash (${uuid})")
            }
        }
    }

    /**
     * Starts looping through online players and updating their status messages
     * Do not call this method more than once or else it may cause issues
     * @see Settler
     */
    fun startMsgLoop() {
        object: BukkitRunnable() {
            override fun run() {
                for (player in SettlerCore.instance!!.server.onlinePlayers) {
                    val settler = getSettler(player.uniqueId)
                    if (settler != null) {
                        player.sendActionBar(settler.formatStatusMsg())
                    }
                }
            }
        }.runTaskTimer(SettlerCore.instance!!, 0, 20)
    }

    /**
     * Reads a settler from the database
     * @see Settler
     * @see Database
     */
    fun reloadSettler(name: String) {
        Database.connect()
        val con = Database.connection

        val stmt = con.createStatement()
        val rs = stmt.executeQuery("SELECT * FROM settlers WHERE name = '$name'")
        while (rs.next()) {
            val uuid = rs.getString("uuid")
            val cash = rs.getDouble("cash")
            val resName = rs.getString("name")
            if (uuid != null) {
                getSettler(UUID.fromString(uuid))!!.money = cash

                println("Reloaded Settler $resName with $$cash (${uuid})")
            }
        }
    }

    /**
     * Registers a settler locally.
     * This does not save the settler to the database.
     * @param uuid The UUID of the settler
     * @param name The name of the settler
     * @param cash The cash of the settler
     * @see Settler
     */
    private fun registerSettler(uuid: UUID, name: String, cash: Double = 0.0) {
        if (getSettler(uuid) != null) return
        settlers.add(Settler(uuid, name, cash))
    }
}