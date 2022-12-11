package com.settlercraft.shops

import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.event.Listener

class Shop(val name: String): Listener {
    val items = mutableListOf<ShopItem>()
    val inv = Bukkit.createInventory(null, 27, Component.text(name))

    fun addItem(item: ShopItem) {
        items.add(item)

        inv.addItem(item.getDisplayItem())
    }

    fun save() {
        val config = Shops.instance!!.config
        config.createSection("shops.$name")
        config.set("shops.$name.name", name)

        for ((i, item) in items.withIndex()) {
            config.createSection("shops.$name.items.$i")
            config.set("shops.$name.items.$i.item", item.rawItem)
            config.set("shops.$name.items.$i.price", item.price)
            config.set("shops.$name.items.$i.stock", item.stock)
        }
    }
}