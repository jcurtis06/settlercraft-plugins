package com.settlercraft.settlercore.utils

import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

object ItemStackUtils {
    fun createItemStack(name: String, lore: List<String>, material: Material, amount: Int): ItemStack {
        val item = ItemStack(material, amount)
        val meta = item.itemMeta
        meta.displayName(Component.text(name))
        meta.lore(lore.map { Component.text(it) })
        item.itemMeta = meta
        return item
    }
}