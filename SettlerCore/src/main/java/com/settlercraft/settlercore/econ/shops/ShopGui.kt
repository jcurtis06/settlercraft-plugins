package com.settlercraft.settlercore.econ.shops

import com.settlercraft.settlercore.SettlerCore
import com.settlercraft.settlercore.econ.Economy
import com.settlercraft.settlercore.econ.EconomyError
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryOpenEvent
import org.bukkit.event.inventory.InventoryType

class ShopGui(val shop: Shop): Listener {
    val inventory = Bukkit.createInventory(null, InventoryType.CHEST, Component.text("Shop"))

    init {
        Bukkit.getPluginManager().registerEvents(this, SettlerCore.instance!!)
    }

    @EventHandler
    fun onInventoryClick(event: InventoryClickEvent) {
        if (event.inventory == inventory) {
            event.isCancelled = true

            if (event.slot < 0 || event.slot >= shop.items.size) return

            val item = shop.items[event.slot]
            val player = event.whoClicked

            Economy.withdraw(player.uniqueId, item.price)
            player.inventory.addItem(item.item)
        }
    }

    @EventHandler
    fun onInventoryOpen(event: InventoryOpenEvent) {
        if (event.inventory == inventory) {
            shop.items.forEach { item ->
                println("Adding item ${item.name} to shop gui")
                inventory.addItem(item.item)
            }
        }
    }
}