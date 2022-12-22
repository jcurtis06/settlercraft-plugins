package com.settlercraft.shops.shop

import com.settlercraft.settlercore.econ.Economy
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class ShopItem(val rawItem: ItemStack, val buy: Double, val sell: Double, private val shop: Shop, val id: Int) {
    fun getDisplayItem(player: Player): ItemStack {
        val item = rawItem.clone()
        val meta = item.itemMeta
        val lore = mutableListOf<Component>()

        var msg = Component.text("§9Click to purchase")
        val balance = Economy.getBalance(player.uniqueId)

        if (balance.first!! < buy) {
            meta.displayName()?.color(NamedTextColor.RED)
            msg = Component.text("§9Insufficient funds")
        }

        lore.add(Component.text("§7Buy: §9$$buy"))

        if (shop.sellable) lore.add(Component.text("§7Sell: §9$$sell"))

        lore.add(Component.empty())
        lore.add(msg)

        meta.lore(lore)
        item.itemMeta = meta
        return item
    }
}