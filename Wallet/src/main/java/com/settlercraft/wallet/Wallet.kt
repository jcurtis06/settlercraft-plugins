package com.settlercraft.wallet

import com.mysql.cj.jdbc.MysqlConnectionPoolDataSource
import com.mysql.cj.jdbc.MysqlDataSource
import com.settlercraft.wallet.commands.Pay
import com.settlercraft.wallet.listeners.JoinLeave
import com.settlercraft.wallet.managers.DataManager
import com.settlercraft.wallet.managers.PlayerManager
import org.bukkit.plugin.java.JavaPlugin


class Wallet : JavaPlugin() {
    companion object {
        var instance: Wallet? = null
    }

    override fun onEnable() {
        instance = this

        getCommand("pay")!!.setExecutor(Pay())

        server.pluginManager.registerEvents(JoinLeave(), this)

        saveDefaultConfig()

        DataManager.connect()

        for (player in server.onlinePlayers) {
            PlayerManager.registerPlayer(player.uniqueId)
        }

        logger.info("Wallet has been enabled!")
    }

    override fun onDisable() {
        DataManager.disconnect()
        logger.info("Wallet has been disabled!")
    }
}