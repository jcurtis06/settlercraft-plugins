package com.settlercraft.shops

object ShopDataUtils {
    val config = Shops.instance!!.config

    fun getShopData(): List<Shop> {
        // return list of shops
        val shops = mutableListOf<Shop>()
        
        /*
        shops:
          Test Shop:
            name: Test Shop
            items:
              '0':
                item:
                  ==: org.bukkit.inventory.ItemStack
                  v: 3120
                  type: DIAMOND
                price: 10
                stock: 10
         */
        
        // get shops section
        val shopsSection = config.getConfigurationSection("shops") ?: return shops
        
        // loop through shops
        for (shopName in shopsSection.getKeys(false)) {
            // get shop section
            val shopSection = shopsSection.getConfigurationSection(shopName) ?: continue
            
            // get shop name
            val name = shopSection.getString("name") ?: continue
            
            // create shop
            val shop = Shop(name)
            
            // get items section
            val itemsSection = shopSection.getConfigurationSection("items") ?: continue
            
            // loop through items
            for (itemIndex in itemsSection.getKeys(false)) {
                // get item section
                val itemSection = itemsSection.getConfigurationSection(itemIndex) ?: continue
                
                // get item
                val item = itemSection.getItemStack("item") ?: continue
                
                // get price
                val price = itemSection.getInt("price")
                
                // get stock
                val stock = itemSection.getInt("stock")
                
                // add item to shop
                println("Adding item to shop: ${item.displayName()}")
                println("Price: $price")
                println("Stock: $stock")
                shop.addItem(ShopItem(item, price, stock))
            }
            
            // add shop to list
            shops.add(shop)
        }

        println("Loaded ${shops.size} shops")

        return shops
    }
}