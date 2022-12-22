package com.settlercraft.shops.shop

import com.settlercraft.shops.Shops
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack

class ShopGui(val shop: Shop): Listener {
    private val inv = Bukkit.createInventory(null, 27, Component.text(shop.name))

    init {
        Bukkit.getServer().pluginManager.registerEvents(this, Shops.instance!!)
    }

    fun open(player: Player) {
        println("Shop ${shop.name}.sellable = ${shop.sellable}")
        shop.items.forEach {
            inv.addItem(it.getDisplayItem(player))
        }

        if (shop.sellable) {
            val bulkItem = ItemStack(Material.CHEST)
            val meta = bulkItem.itemMeta
            meta.displayName(Component.text("§9Bulk Sell"))
            bulkItem.itemMeta = meta

            inv.setItem(22, bulkItem)
        }

        player.openInventory(inv)
    }

    @EventHandler
    fun onClick(event: InventoryClickEvent) {
        if (event.inventory != inv) return
        if (event.clickedInventory == inv) {
            event.isCancelled = true

            if (event.slot == 22 && shop.sellable) {
                val bulkGui = BulkGui(shop, event.whoClicked as Player)
                bulkGui.open()
                return
            }

            val item = event.currentItem ?: return
            val player = event.whoClicked as Player
            val shopItem = shop.items.firstOrNull { shopItem -> shopItem.rawItem.type == item.type } ?: return
            shop.purchaseItem(shopItem, player)
            if (shop.owner != null) {
                item.amount--
                if (shopItem.rawItem.amount - 1 == 0) {
                    shop.removeItem(shopItem)
                }
                shopItem.rawItem.amount--
                shop.updateItem(shopItem)
            }
        } else {
            event.isCancelled = true
            if (!shop.sellable) return

            val item = event.currentItem ?: return
            val player = event.whoClicked as Player
            val shopItem = shop.items.firstOrNull { shopItem -> shopItem.rawItem.type == item.type } ?: return

            shop.sellItem(shopItem, event.currentItem!!, player)
        }
    }
}