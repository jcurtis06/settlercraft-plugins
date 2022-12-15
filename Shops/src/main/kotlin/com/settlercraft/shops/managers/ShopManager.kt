package com.settlercraft.shops.managers

import com.settlercraft.shops.shop.Shop
import com.settlercraft.shops.shop.ShopItem
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

object ShopManager {
    val shops = mutableListOf<Shop>()

    fun createShop(name: String) {
        ShopDataManager.config?.createSection(name)
        ShopDataManager.save()
    }

    fun deleteShop(name: String) {
        ShopDataManager.config?.set(name, null)
        ShopDataManager.save()
    }

    fun openShop(player: Player, name: String) {
        val shop = shops.find { it.name == name }
        shop?.open(player)
    }

    fun addShopItem(shop: String, item: ItemStack, buy: Double, sell: Double) {
        val section = ShopDataManager.config?.getConfigurationSection(shop)
        val size = section?.getKeys(false)?.size ?: 0
        section?.createSection(size.toString())
        section?.getConfigurationSection("$size")?.set("item", item)
        section?.getConfigurationSection("$size")?.set("buy", buy)
        section?.getConfigurationSection("$size")?.set("sell", sell)
        ShopDataManager.save()
    }

    fun loadShops() {
        val loadedShops = ShopDataManager.config?.getKeys(false)

        loadedShops?.forEach { s ->
            val shop = Shop(s)
            val items = ShopDataManager.config?.getConfigurationSection(s)?.getKeys(false)
            items?.forEach {
                println("Found item $it in shop $s")
                val item = ShopDataManager.config?.getItemStack("$s.$it.item")
                val buy = ShopDataManager.config?.getDouble("$s.$it.buy")
                val sell = ShopDataManager.config?.getDouble("$s.$it.sell")
                println("Loading $item with buy price $buy and sell price $sell")
                if (item != null && buy != null && sell != null) {
                    shop.items.add(ShopItem(item, buy, sell))
                }
            }
            shops.add(shop)
        }

        Bukkit.getLogger().info("Loaded ${shops.size} shops")

        shops.forEach {
            it.printItems()
        }
    }
}