package com.settlercraft.shops.shop

import com.settlercraft.settlercore.econ.Economy
import com.settlercraft.settlercore.econ.EconomyError
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack

class Shop(val name: String) {
    val items = mutableListOf<ShopItem>()

    fun printItems() {
        items.forEach {
            println(it.rawItem.type.name)
        }
    }

    fun open(player: Player) {
        val gui = ShopGui(this)
        gui.open(player)
    }

    fun purchaseItem(item: ShopItem, player: Player) {
        when (Economy.withdraw(player.uniqueId, item.buy)) {
            EconomyError.SUCCESS -> {
                player.inventory.addItem(item.rawItem)
                player.playSound(player.location, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f)
            }
            EconomyError.INSUFFICIENT_FUNDS -> {
                player.playSound(player.location, Sound.ENTITY_VILLAGER_NO, 1f, 1f)
            }
            else -> {
                player.playSound(player.location, Sound.ENTITY_GOAT_SCREAMING_DEATH, 1f, 1f)
            }
        }
    }

    fun sellItem(item: ShopItem, clickedItem: ItemStack, player: Player) {
        when (Economy.deposit(player.uniqueId, item.sell)) {
            EconomyError.SUCCESS -> {
                clickedItem.amount--
                player.playSound(player.location, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f)
            }
            else -> {
                player.playSound(player.location, Sound.ENTITY_GOAT_SCREAMING_DEATH, 1f, 1f)
            }
        }
    }

    fun getTotalSellPrice(inv: Inventory): Double {
        var total = 0.0
        // Loop through all items in the inventory
        for (i in 0 until inv.size) {
            // Get the item in the slot
            val item = inv.getItem(i) ?: continue
            // If the item is null, continue to the next item
            // Get the shop item from the item
            val shopItem = getShopItem(item) ?: continue
            // If the shop item is null, continue to the next item
            // Add the sell price of the item to the total
            total += shopItem.sell * item.amount
        }
        return total
    }

    fun getShopItem(item: ItemStack): ShopItem? {
        return items.find { it.rawItem.isSimilar(item) }
    }
}