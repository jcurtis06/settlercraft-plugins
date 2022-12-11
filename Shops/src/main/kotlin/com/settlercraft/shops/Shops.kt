package com.settlercraft.shops

import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.java.JavaPlugin

class Shops: JavaPlugin() {
    private var shops = listOf<Shop>()

    companion object {
        var instance: Shops? = null
    }

    override fun onEnable() {
        instance = this
        saveDefaultConfig()

        shops = ShopDataUtils.getShopData()

        for (p in server.onlinePlayers) {
            p.openInventory(shops[0].inv)
        }

        logger.info("Shops has been enabled!")
    }
}