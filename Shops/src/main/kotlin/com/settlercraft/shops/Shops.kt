package com.settlercraft.shops

import com.settlercraft.shops.commands.ShopCommand
import com.settlercraft.shops.managers.ShopManager
import org.bukkit.plugin.java.JavaPlugin

class Shops: JavaPlugin() {
    companion object {
        var instance: Shops? = null
    }

    override fun onEnable() {
        instance = this

        getCommand("shop")?.setExecutor(ShopCommand())
        ShopManager.loadShops()

        logger.info("Shops has been enabled!")
    }
}