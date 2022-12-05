package com.settlercraft.settlercore

import com.settlercraft.settlercore.data.API
import com.settlercraft.settlercore.settler.SettlerListeners
import com.settlercraft.settlercore.settler.Settlers
import org.bukkit.plugin.java.JavaPlugin

class SettlerCore : JavaPlugin() {
    companion object {
        var instance: SettlerCore? = null
    }

    override fun onEnable() {
        println("SQL Information:")
        println("Host: ${config.getString("mysql.host")}")
        println("Port: ${config.getString("mysql.port")}")
        println("Database: ${config.getString("mysql.database")}")
        println("User: ${config.getString("mysql.user")}")
        println("Password: ${config.getString("mysql.password")}")
        instance = this
        saveDefaultConfig()
        server.pluginManager.registerEvents(SettlerListeners(), this)

        Settlers.loadSettlers()

        API.start()
    }

    override fun onDisable() {
        API.stop()
    }
}