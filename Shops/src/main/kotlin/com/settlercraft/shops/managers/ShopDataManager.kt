package com.settlercraft.shops.managers

import com.settlercraft.shops.Shops
import org.bukkit.Bukkit
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File
import java.io.IOException

object ShopDataManager {
    private var file: File = File(Shops.instance?.dataFolder, "shops.yml")
    var config: FileConfiguration? = null

    init {
        if (!file.exists()) {
            try {
                file.createNewFile()
            } catch (e: IOException) {
                Bukkit.getLogger().severe("Could not create shops.yml!")
            }
        }

        config = YamlConfiguration.loadConfiguration(file)
    }

    fun save() {
        try {
            config?.save(file)
        } catch (e: Exception) {
            Bukkit.getLogger().severe("Could not save shops.yml!")
        }
    }

    fun reload() {
        config = YamlConfiguration.loadConfiguration(file)
    }
}