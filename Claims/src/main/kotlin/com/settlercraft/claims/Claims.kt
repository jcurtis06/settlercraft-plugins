package com.settlercraft.claims

import com.settlercraft.claims.claim.ClaimManager
import com.settlercraft.claims.commands.*
import com.settlercraft.claims.listeners.ClaimListeners
import com.settlercraft.claims.listeners.JoinListener
import org.bukkit.event.Listener
import org.bukkit.plugin.java.JavaPlugin

class Claims: JavaPlugin() {
    companion object {
        var instance: Claims? = null
    }

    override fun onEnable() {
        saveDefaultConfig()
        instance = this

        if (server.pluginManager.getPlugin("Settlercore") == null) {
            logger.severe("SettlerCore not found! Disabling...")
            server.pluginManager.disablePlugin(this)
            return
        }

        logger.info("Enabled!")

        server.pluginManager.registerEvents(ClaimListeners(), this)
        server.pluginManager.registerEvents(JoinListener(), this)

        getCommand("claim")!!.setExecutor(ClaimCMD())
        getCommand("delallcontainers")!!.setExecutor(DelAllContainersCMD())
        getCommand("togglelock")!!.setExecutor(ToggleLockCMD())
        getCommand("delclaim")!!.setExecutor(DelClaimCMD())
        getCommand("convertlegacyclaims")!!.setExecutor(ConvertLegacyClaimsCMD())

        ClaimManager.loadClaims()

        for (p in server.onlinePlayers) {
            ClaimManager.setStatusMsg(p)
        }
    }

    fun registerListener(listener: Listener) {
        server.pluginManager.registerEvents(listener, this)
    }
}