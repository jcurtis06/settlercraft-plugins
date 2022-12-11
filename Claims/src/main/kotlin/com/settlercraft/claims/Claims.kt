package com.settlercraft.claims

import com.settlercraft.claims.claim.ClaimManager
import com.settlercraft.claims.commands.ClaimCMD
import com.settlercraft.claims.commands.DelAllContainersCMD
import com.settlercraft.claims.listeners.BlockListener
import com.settlercraft.claims.listeners.DangerListener
import com.settlercraft.claims.listeners.JoinListener
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

        server.pluginManager.registerEvents(BlockListener(), this)
        server.pluginManager.registerEvents(JoinListener(), this)
        server.pluginManager.registerEvents(DangerListener(), this)

        getCommand("claim")!!.setExecutor(ClaimCMD())
        getCommand("delallcontainers")!!.setExecutor(DelAllContainersCMD())

        ClaimManager.loadClaims()

        for (p in server.onlinePlayers) {
            ClaimManager.setStatusMsg(p)
        }
    }

    fun registerListener(listener: Listener) {
        server.pluginManager.registerEvents(listener, this)
    }
}