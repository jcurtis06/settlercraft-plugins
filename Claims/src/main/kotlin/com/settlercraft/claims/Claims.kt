package com.settlercraft.claims

import com.settlercraft.claims.listeners.BlockListener
import com.settlercraft.claims.listeners.JoinListener
import com.settlercraft.claims.listeners.MovementListener
import com.settlercraft.claims.ui.ClaimMain
import org.bukkit.event.Listener
import org.bukkit.plugin.java.JavaPlugin

class Claims: JavaPlugin() {
    companion object {
        var instance: Claims? = null
    }

    override fun onEnable() {
        instance = this

        if (server.pluginManager.getPlugin("Settlercore") == null) {
            logger.severe("SettlerCore not found! Disabling...")
            server.pluginManager.disablePlugin(this)
            return
        }

        logger.info("Enabled!")

        for (player in server.onlinePlayers) {
            player.sendMessage("Hello, ${player.name}!")
            ClaimMain().open(player)
        }

        server.pluginManager.registerEvents(BlockListener(), this)
        server.pluginManager.registerEvents(MovementListener(), this)
        server.pluginManager.registerEvents(JoinListener(), this)
    }

    fun registerListener(listener: Listener) {
        server.pluginManager.registerEvents(listener, this)
    }
}