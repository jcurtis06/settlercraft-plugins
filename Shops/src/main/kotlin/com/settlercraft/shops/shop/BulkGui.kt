package com.settlercraft.shops.shop

import com.settlercraft.settlercore.econ.Economy
import com.settlercraft.shops.Shops
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryDragEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.scheduler.BukkitRunnable

class BulkGui(private val shop: Shop, private val player: Player): Listener {
    private val inv = Bukkit.createInventory(null, 27, Component.text(shop.name))
    private val sellItem = ItemStack(Material.EMERALD_BLOCK)

    init {
        Bukkit.getServer().pluginManager.registerEvents(this, Shops.instance!!)
    }

    fun open() {
        val sellMeta = sellItem.itemMeta
        sellMeta.displayName(Component.text("§9Sell"))
        sellItem.itemMeta = sellMeta
        inv.setItem(21, sellItem)

        val backItem = ItemStack(Material.ARROW)
        val backMeta = backItem.itemMeta
        backMeta.displayName(Component.text("§9Back"))
        backItem.itemMeta = backMeta
        inv.setItem(23, backItem)

        player.openInventory(inv)
    }

    @EventHandler
    fun onClick(event: InventoryClickEvent) {
        if (event.inventory != inv) return
        if (event.clickedInventory == inv) {
            updateSell()

            if (event.slot == 21) {
                event.isCancelled = true
                Economy.deposit(player.uniqueId, shop.getTotalSellPrice(inv))
                inv.contents.forEachIndexed { index, _ ->
                    if (index != 21 && index != 23) {
                        inv.setItem(index, null)
                    }
                }
            } else if (event.slot == 23) {
                event.isCancelled = true
                shop.open(player)
            }
        } else {
            if (shop.items.none { shopItem -> shopItem.rawItem.type == event.currentItem?.type }) {
                if (event.currentItem?.type == Material.AIR || event.currentItem == null) return
                event.isCancelled = true
            }
        }
    }

    @EventHandler
    fun onDrag(event: InventoryDragEvent) {
        updateSell()
    }

    private fun updateSell() {
        object: BukkitRunnable() {
            override fun run() {
                val sellMeta = sellItem.itemMeta

                sellMeta.lore(listOf(Component.text("§7Total Value: §a${shop.getTotalSellPrice(inv)}")))
                sellItem.itemMeta = sellMeta
                inv.setItem(21, sellItem)
                updateLater(player)
            }
        }.runTaskLater(Shops.instance!!, 1)
    }

    fun updateLater(player: Player) {
        object: BukkitRunnable() {
            override fun run() {
                player.updateInventory()
            }
        }.runTaskLater(Shops.instance!!, 1)
    }
}