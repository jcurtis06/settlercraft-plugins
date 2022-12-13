package com.settlercraft.claims.commands

import com.settlercraft.claims.claim.ClaimManager
import com.settlercraft.claims.claim.Lock
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class ToggleLockCMD: CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>?): Boolean {
        if (args?.size == 1) {
           if (sender is Player) {
               val lockTitle = args[0]

               val found: Lock
               try {
                   found = Lock.valueOf(lockTitle.uppercase())
               } catch (e: IllegalArgumentException) {
                   sender.sendMessage("§cSorry sir, that is not a lock type!")
                   sender.sendMessage("§aFor your pleasure, I list the types:")
                   for (lock in Lock.values()) {
                       sender.sendMessage("- §a${lock.name}")
                   }
                   return true
               }

               ClaimManager.toggleLock(sender.uniqueId, found)
               sender.sendMessage("§aToggling $lockTitle to ${
                   if (ClaimManager.getPlayerLock(sender.uniqueId).getLock(found)) "locked" else "unlocked"
               }, my liege")
               return true
           }
        }
        return false
    }
}