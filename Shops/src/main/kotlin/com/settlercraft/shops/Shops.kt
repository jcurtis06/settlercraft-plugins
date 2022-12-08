package com.settlercraft.shops

import org.bukkit.plugin.java.JavaPlugin

class Shops: JavaPlugin() {
    val shops = mutableListOf<Shop>()

    override fun onEnable() {
        // Plugin startup logic
        logger.info("Shops has been enabled!")
    }
}