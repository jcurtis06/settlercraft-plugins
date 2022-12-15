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
    private val bulkInv = Bukkit.createInventory(null, 27, Component.text("Bulk Sell"))
    private var changedItems = mutableMapOf<Int, ItemStack>()

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
            bulkInv.addItem(bulkItem)
        }

        player.openInventory(inv)

        for ((i, item: ItemStack?) in player.inventory.contents.withIndex()) {
            if (item == null) continue
            if (shop.items.any { it.rawItem.type == item.type }) {
                val shopItem: ShopItem = shop.items.find { it.rawItem.type == item.type } as ShopItem
                val displayItem = shopItem.getSellItem()
                displayItem.amount = item.amount

                player.inventory.setItem(player.inventory.first(item), displayItem)
                changedItems[i] = item
            }
        }
        updateLater(player)
    }

    private fun updateLater(player: Player) {
        object : BukkitRunnable() {
            override fun run() {
                player.updateInventory()
            }
        }.runTaskLater(Shops.instance!!, 1)
    }

    @EventHandler
    fun onClick(event: InventoryClickEvent) {
        if (event.inventory == inv) {
            event.isCancelled = true

            val item = event.currentItem ?: return
            val player = event.whoClicked as Player
            val shopItem = shop.items.firstOrNull { shopItem -> shopItem.rawItem.type == item.type } ?: return

            if (event.clickedInventory == inv) {
                shop.purchaseItem(shopItem, player)

                for ((i, it: ItemStack?) in player.inventory.contents.withIndex()) {
                    if (it == null) continue
                    if (shopItem.rawItem.type == it.type) {
                        println("Found item")

                        changedItems[i] = ItemStack(it.type, changedItems[i]!!.amount+1)
                    }
                }
            } else {
                shop.sellItem(shopItem, player)
                changedItems[event.slot] = ItemStack(item.type, changedItems[event.slot]!!.amount-1)
            }
        }
    }

    @EventHandler
    fun onClose(event: InventoryCloseEvent) {
        if (event.inventory == inv) {
            val player: Player = event.player as Player

            changedItems.forEach {
                player.inventory.setItem(it.key, it.value)
            }
        }
    }
}