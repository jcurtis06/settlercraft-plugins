package com.settlercraft.shops.shop

import com.settlercraft.settlercore.econ.Economy
import com.settlercraft.settlercore.econ.EconomyError
import org.bukkit.Sound
import org.bukkit.entity.Player

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
                player.inventory.addItem(item.getSellItem())
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

    fun sellItem(item: ShopItem, player: Player) {
        when (Economy.deposit(player.uniqueId, item.sell)) {
            EconomyError.SUCCESS -> {
                player.inventory.removeItem(item.getSellItem())
                player.playSound(player.location, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f)
            }
            else -> {
                player.playSound(player.location, Sound.ENTITY_GOAT_SCREAMING_DEATH, 1f, 1f)
            }
        }
    }
}