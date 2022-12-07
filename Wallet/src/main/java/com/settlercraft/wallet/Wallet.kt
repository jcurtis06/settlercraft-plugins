package com.settlercraft.wallet

import com.settlercraft.settlercore.settler.Settlers
import com.settlercraft.settlercore.settler.actionbar.StatusMessage
import com.settlercraft.wallet.commands.PayCMD
import com.settlercraft.wallet.listeners.JoinQuit
import org.bukkit.plugin.java.JavaPlugin

class Wallet : JavaPlugin() {
    companion object {
        const val prefix = "§7[§aWallet§7] §9"
        var instance: Wallet? = null
    }

    override fun onEnable() {
        instance = this

        if (server.pluginManager.isPluginEnabled("Settlercore")) {
            logger.info("Successfully hooked into SettlerCore!")
        } else {
            logger.severe("SettlerCore must be enabled to use this plugin!")
            server.pluginManager.disablePlugin(this)
            return
        }

        getCommand("pay")?.setExecutor(PayCMD())
        server.pluginManager.registerEvents(JoinQuit(), this)

        logger.info("Wallet has been enabled!")

        for (player in server.onlinePlayers) {
            Settlers.getSettler(player.uniqueId)?.let {
                it.addStatusMsg(StatusMessage("money-${it.name}") { "§a${it.money.toInt()}" })
            }
        }
    }

    override fun onDisable() {
        logger.info("Wallet has been disabled!")
    }
}