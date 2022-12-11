package com.settlercraft.claims.commands

import com.settlercraft.claims.claim.ClaimManager
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.ConsoleCommandSender

class DelAllContainersCMD: CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>?): Boolean {
        if (sender is ConsoleCommandSender) {
            println("Deleting all containers...")
            for (chunk in ClaimManager.claims) {
                println("deleted container!")
                val container = chunk.location.chunk.persistentDataContainer
                container.remove(ClaimManager.claimedKey)
            }
        }

        return true
    }

}