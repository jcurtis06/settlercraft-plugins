package com.settlercraft.shops.commands

import com.settlercraft.shops.managers.ShopManager
import org.bukkit.Material
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class ShopCommand: CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>?): Boolean {
        if (sender is Player) {
            when (args?.get(0)) {
                "create" -> {
                    if (args.size == 2) {
                        sender.sendMessage("Creating shop ${args[1]}")
                        ShopManager.createShop(args[1])
                        return true
                    }
                    if (args.size == 1) {
                        sender.sendMessage("Creating shop ${sender.name}")
                        ShopManager.createPlayerShop(sender)
                        return true
                    }
                }
                "delete" -> {
                    if (args.size == 2) {
                        // todo: delete shop
                        sender.sendMessage("Deleting shop ${args[1]}")
                    }
                }
                "add" -> {
                    when (args.size) {
                        5 -> {
                            val shop = args[1]
                            val item = args[2]
                            val buy = args[3].toDouble()
                            val sell = args[4].toDouble()

                            ShopManager.addShopItem(shop, ItemStack(Material.valueOf(item)), buy, sell)

                            sender.sendMessage("Adding $item to $shop with buy price $buy and sell price $sell")
                        }
                        4 -> {
                            val shop = args[1]
                            val buy = args[2].toDouble()
                            val sell = args[3].toDouble()
                            val item = sender.inventory.itemInMainHand

                            ShopManager.addShopItem(shop, item, buy, sell)

                            sender.sendMessage("Adding ${item.type.name} to $shop with buy price $buy and sell price $sell")
                        }
                        else -> {
                            sender.sendMessage("Usage: /shop add <shop> [mat_name] <buy> <sell>")
                        }
                    }
                }
                "remove" -> {
                    if (args.size == 3) {
                        val shop = args[1]
                        val item = args[2]
                        sender.sendMessage("Removing $item from $shop")
                    }
                }
                "list" -> {
                    sender.sendMessage("Shops:")
                    ShopManager.shops.forEach {
                        sender.sendMessage(it.name)
                    }
                }
                "open" -> {
                    if (args.size == 2) {
                        val shop = args[1]
                        ShopManager.openShop(sender, shop)
                    }
                }
            }
        }

        return true
    }

}