package com.settlercraft.settlercore

import com.settlercraft.settlercore.data.API
import com.settlercraft.settlercore.settler.SettlerListeners
import com.settlercraft.settlercore.settler.Settlers
import org.bukkit.Bukkit
import org.bukkit.Statistic
import org.bukkit.plugin.java.JavaPlugin
import org.json.simple.JSONObject
import java.util.*

class SettlerCore : JavaPlugin() {
    companion object {
        var instance: SettlerCore? = null
    }

    override fun onEnable() {
        instance = this
        saveDefaultConfig()
        server.pluginManager.registerEvents(SettlerListeners(), this)

        Settlers.loadSettlers()
        Settlers.startMsgLoop()

        API.start()
    }

    override fun onDisable() {
        API.stop()
    }
}