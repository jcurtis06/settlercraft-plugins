package com.settlercraft.claims.commands

import com.settlercraft.claims.claim.ClaimedChunk
import com.settlercraft.claims.claim.ClaimManager
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class ClaimCMD: CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>?): Boolean {
        if (sender is Player) {
            val claim = ClaimedChunk(sender.location, sender.uniqueId)
            ClaimManager.addClaim(claim)
        }

        return true
    }
}