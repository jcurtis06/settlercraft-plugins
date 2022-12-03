package com.settlercraft.settlercore

import com.settlercraft.settlercore.commands.Pay
import com.settlercraft.settlercore.data.Database
import com.settlercraft.settlercore.econ.Economy
import com.settlercraft.settlercore.econ.shops.Shop
import com.settlercraft.settlercore.econ.shops.ShopItem
import com.settlercraft.settlercore.settler.SettlerListeners
import com.settlercraft.settlercore.settler.Settlers
import com.settlercraft.settlercore.utils.ItemStackUtils
import org.bukkit.Material
import org.bukkit.plugin.java.JavaPlugin
import java.util.*

class SettlerCore : JavaPlugin() {
    companion object {
        var instance: SettlerCore? = null
    }

    override fun onEnable() {
        instance = this
        saveDefaultConfig()
        server.pluginManager.registerEvents(SettlerListeners(), this)

        getCommand("pay")!!.setExecutor(Pay())

        Settlers.loadSettlers()

        for (p in server.onlinePlayers) {
            Economy.deposit(p.uniqueId, 100.0)

            val testShop = Shop()

            Database.connect()
            val con = Database.connection

            val stmt = con.createStatement()
            val rs = stmt.executeQuery("SELECT * FROM SHOP_MINERALS")
            while (rs.next()) {
                val item = rs.getString("ITEM")
                val buy = rs.getInt("BUY")

                testShop.items.add(ShopItem(item, buy + 0.0, 10, Material.valueOf(item)))
            }

            p.openInventory(testShop.gui.inventory)
        }
    }
}