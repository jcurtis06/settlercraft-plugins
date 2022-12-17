package com.settlercraft.shops.shop

import com.settlercraft.shops.Shops
import com.settlercraft.shops.managers.ShopManager
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.inventory.InventoryMoveItemEvent
import org.bukkit.event.inventory.InventoryPickupItemEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.scheduler.BukkitRunnable

class ShopGui(val shop: Shop): Listener {
    private val inv = Bukkit.createInventory(null, 27, Component.text(shop.name))

    init {
        Bukkit.getServer().pluginManager.registerEvents(this, Shops.instance!!)
    }

    fun open(player: Player) {
        shop.items.forEach {
            inv.addItem(it.getDisplayItem(player))

            val bulkItem = ItemStack(Material.CHEST)
            val meta = bulkItem.itemMeta
            meta.displayName(Component.text("§9Bulk Sell"))
            meta.lore(listOf(Component.text("§7")))
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

            if (event.slot == 22) {
                val bulkGui = BulkGui(shop, event.whoClicked as Player)
                bulkGui.open()
                return
            }

            val item = event.currentItem ?: return
            val player = event.whoClicked as Player
            val shopItem = shop.items.firstOrNull { shopItem -> shopItem.rawItem.type == item.type } ?: return
            shop.purchaseItem(shopItem, player)
        } else {
            event.isCancelled = true

            val item = event.currentItem ?: return
            val player = event.whoClicked as Player
            val shopItem = shop.items.firstOrNull { shopItem -> shopItem.rawItem.type == item.type } ?: return

            shop.sellItem(shopItem, event.currentItem!!, player)
        }
    }
}