package com.settlercraft.settlercore.econ.shops

class Shop {
    val items = mutableListOf<ShopItem>()
    val gui = ShopGui(this)
}