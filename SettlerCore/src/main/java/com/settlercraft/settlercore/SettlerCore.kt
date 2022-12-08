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
        instance = this
        saveDefaultConfig()
        server.pluginManager.registerEvents(SettlerListeners(), this)

        Settlers.loadSettlers()
        Settlers.startMsgLoop()

        val pairTest = Pair("Hello", "World")
        logger.info("${pairTest.first} From SettlerCore")
        logger.info("${pairTest.second} From SettlerCore")

        API.start()
    }

    override fun onDisable() {
        API.stop()
    }
}