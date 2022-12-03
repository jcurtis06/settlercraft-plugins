package com.settlercraft.settlercore.econ.shops

import com.settlercraft.settlercore.utils.ItemStackUtils
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

data class ShopItem(val name: String, val price: Double, val stock: Int, val material: Material) {
    val item = ItemStackUtils.createItemStack(name, listOf("Price: $price", "Stock: $stock"), material, 1)
}