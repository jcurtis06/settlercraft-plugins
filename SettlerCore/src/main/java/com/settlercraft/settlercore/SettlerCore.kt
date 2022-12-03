package com.settlercraft.settlercore

import com.settlercraft.settlercore.econ.Economy
import com.settlercraft.settlercore.settler.SettlerListeners
import com.settlercraft.settlercore.settler.Settlers
import org.bukkit.plugin.java.JavaPlugin

class SettlerCore : JavaPlugin() {
    companion object {
        var instance: SettlerCore? = null
    }

    override fun onEnable() {
        instance = this
        saveDefaultConfig()
        server.pluginManager.registerEvents(SettlerListeners(), this)
        Settlers.loadSettlers()

        for (p in server.onlinePlayers) {
            Economy.deposit(p.uniqueId, 100.0)
        }
    }
}