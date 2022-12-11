package com.settlercraft.claims.commands

import com.settlercraft.claims.claim.ClaimManager
import com.settlercraft.claims.claim.ClaimedChunk
import com.settlercraft.claims.ui.ClaimMain
import com.settlercraft.settlercore.econ.Economy
import com.settlercraft.settlercore.settler.Settlers
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class ClaimCMD: CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>?): Boolean {
        if (sender is Player) {
            ClaimMain().open(sender)
            return true
        }
        sender.sendMessage("§cYou must be a player to use this command!")
        return false
    }
}