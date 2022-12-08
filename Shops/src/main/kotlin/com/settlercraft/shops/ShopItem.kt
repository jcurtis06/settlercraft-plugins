package com.settlercraft.shops

import net.kyori.adventure.text.Component
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class ShopItem(val rawItem: ItemStack, val price: Int, val stock: Int = 0) {
    fun getDisplayItem(): ItemStack {
        val item = rawItem.clone()
        val meta = item.itemMeta

        meta.lore()?.add(Component.text("Price: $price"))
        meta.lore()?.add(Component.text("Stock: $stock"))

        item.itemMeta = meta
        return item
    }
}