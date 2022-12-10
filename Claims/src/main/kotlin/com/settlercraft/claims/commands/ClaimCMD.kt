package com.settlercraft.claims.commands

import com.settlercraft.claims.claim.ClaimManager
import com.settlercraft.claims.claim.ClaimedChunk
import com.settlercraft.settlercore.econ.Economy
import com.settlercraft.settlercore.settler.Settlers
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class ClaimCMD: CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>?): Boolean {
        if (sender is Player) {
            val price = ClaimManager.priceOfLandFor(sender.uniqueId)

            if (Settlers.getSettler(sender.uniqueId)!!.money < price) {
                sender.sendMessage("You too broke smh")
                return true
            }

            if (ClaimManager.isInClaim(sender.location)) {
                sender.sendMessage("Dat do be owned by sumbody so you cants has it")
                return true
            }

            Economy.withdraw(sender.uniqueId, price)
            val claim = ClaimedChunk(sender.location, sender.uniqueId)
            ClaimManager.addClaim(claim)
            return true
        }
        sender.sendMessage("u're consule or sumthin else >:( playzerz onli!")
        return false
    }
}