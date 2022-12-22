package com.settlercraft.shops.shop

import com.settlercraft.settlercore.econ.Economy
import com.settlercraft.settlercore.econ.EconomyError
import com.settlercraft.shops.managers.ShopDataManager
import com.settlercraft.shops.managers.ShopManager
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import java.util.UUID

class Shop(val name: String, val sellable: Boolean = true) {
    val items = mutableListOf<ShopItem>()
    var owner: UUID? = null

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
                player.inventory.addItem(item.rawItem.asOne())
                if (owner != null) {
                    Economy.deposit(owner!!, item.buy)
                }
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

    fun removeItem(item: ShopItem) {
        val id = item.id
        println("Removing item w/ id $id")
        items.remove(item)
        println("Removed item, new size: ${items.size}")
        for (i in items) {
            println("Item ${i.id} is ${i.rawItem.type.name}")
        }

        val section = ShopDataManager.config?.getConfigurationSection(name)
        section?.set(id.toString(), null)
        ShopDataManager.save()
    }

    fun updateItem(item: ShopItem) {
        ShopDataManager.config?.set("${name}.${item.id}.item", item.rawItem)
        ShopDataManager.save()
    }

    fun getTotalSellPrice(inv: Inventory): Double {
        var total = 0.0
        for (i in 0 until inv.size) {
            val item = inv.getItem(i) ?: continue
            val shopItem = getShopItem(item) ?: continue
            total += shopItem.sell * item.amount
        }
        return total
    }

    private fun getShopItem(item: ItemStack): ShopItem? {
        return items.find { it.rawItem.isSimilar(item) }
    }
}