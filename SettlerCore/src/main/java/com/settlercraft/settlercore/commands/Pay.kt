package com.settlercraft.settlercore.commands

import com.settlercraft.settlercore.econ.Economy
import com.settlercraft.settlercore.econ.EconomyError
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class Pay: CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>?): Boolean {
        if (sender is Player) {
            if (args != null && args.size == 2) {
                val amount = args[1].toDouble()
                val target = Bukkit.getOfflinePlayer(args[0]).uniqueId
                when (Economy.transfer(sender.uniqueId, target, amount)) {
                    EconomyError.SUCCESS -> sender.sendMessage("Successfully transferred $amount to ${args[0]}")
                    EconomyError.INSUFFICIENT_FUNDS -> sender.sendMessage("You do not have enough money to do that!")
                    EconomyError.SETTLER_NOT_FOUND -> sender.sendMessage("That player does not exist!")
                }
            }
        }
        return false
    }
}