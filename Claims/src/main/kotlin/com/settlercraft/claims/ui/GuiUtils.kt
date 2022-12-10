package com.settlercraft.claims.ui

import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

object GuiUtils {
    fun createGuiItem(material: Material, name: String, vararg lore: String): ItemStack {
        val item = org.bukkit.inventory.ItemStack(material, 1)
        val meta = item.itemMeta
        meta.displayName(Component.text(name))
        meta.lore(lore.map { Component.text(it) })
        item.itemMeta = meta
        return item
    }
}