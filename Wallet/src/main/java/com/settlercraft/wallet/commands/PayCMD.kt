package com.settlercraft.wallet.commands

import com.settlercraft.settlercore.econ.Economy
import com.settlercraft.settlercore.econ.EconomyError
import com.settlercraft.settlercore.settler.Settlers
import com.settlercraft.wallet.Wallet
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.util.UUID

class PayCMD: CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>?): Boolean {
        if (sender is Player) {
            if (args != null && args.size == 2) {
                val target: UUID? = Settlers.settlerUUID(args[0])
                if (target == null) {
                    sender.sendMessage("${Wallet.prefix}Could not find settler with name §7${args[0]}")
                    return true
                }

                val amount: Int? = args[1].toIntOrNull()

                if (amount == null || amount <= 0) {
                    sender.sendMessage("${Wallet.prefix}§7${args[1]} §cis not a valid number!")
                    return false
                }

                when (Economy.transfer(sender.uniqueId, target, amount.toDouble())) {
                    EconomyError.SETTLER_NOT_FOUND -> sender.sendMessage("${Wallet.prefix}Could not find settler with name §7${args[0]}")
                    EconomyError.INSUFFICIENT_FUNDS -> sender.sendMessage("${Wallet.prefix}You do not have enough money to pay §7${args[0]}")
                    EconomyError.SUCCESS -> {
                        sender.sendMessage("${Wallet.prefix}Successfully transferred §7$$amount §9to §7${args[0]}")
                        sender.server.getPlayer(target)?.sendMessage("${Wallet.prefix}You have received §7$$amount §9from §7${sender.name}")
                    }
                }
                return true
            }
        }
        return false
    }
}