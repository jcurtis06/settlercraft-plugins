package com.settlercraft.claims.commands

import com.settlercraft.claims.claim.ClaimManager
import com.settlercraft.claims.claim.ClaimStatus
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class DelClaimCMD: CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>?): Boolean {
        if (sender is Player) {
            val claimedChunk = ClaimManager.getClaimedChunk(sender.location)

            if (claimedChunk != null) {
                when (ClaimManager.delClaim(claimedChunk, sender.uniqueId)) {
                    ClaimStatus.SUCCESS -> {
                        sender.sendMessage("§aPer your request, the claim was deleted!")
                    }
                    ClaimStatus.NOT_YOURS -> {
                        sender.sendMessage("§My liege, this isn't you're estate but someone else's")
                    }
                    else -> {
                        sender.sendMessage("§cWell, this is awkward. Something went wrong!")
                    }
                }
                return true
            } else {
                sender.sendMessage("§cSorry sir, this doesn't seem to belong to anyone at all")
            }
            return true
        }
        return false
    }
}