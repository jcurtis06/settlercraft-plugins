package com.settlercraft.shops

class Shop {
    val items = mutableListOf<ShopItem>()

    fun addItem(item: ShopItem) {
        items.add(item)
    }
}