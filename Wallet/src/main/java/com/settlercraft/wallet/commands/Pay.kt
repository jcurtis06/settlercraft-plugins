package com.settlercraft.wallet.commands

import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class Pay: CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>?): Boolean {
        if (sender !is Player) {
            sender.sendMessage("You must be a player to use this command!")
            return true
        }

        if (args?.size == 2) {
            val target = sender.server.getOfflinePlayer(args[0])
            val amount = args[1].toDoubleOrNull()
        } else {
            sender.sendMessage("Invalid arguments! (/pay <user> <amount>)")
            return false
        }
        return false
    }
}