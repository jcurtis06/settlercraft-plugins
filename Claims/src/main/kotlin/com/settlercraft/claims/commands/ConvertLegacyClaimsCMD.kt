package com.settlercraft.claims.commands

import com.settlercraft.claims.Claims
import com.settlercraft.claims.claim.ClaimManager
import com.settlercraft.claims.claim.ClaimedChunk
import com.settlercraft.settlercore.data.Database
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import java.util.*

class ConvertLegacyClaimsCMD: CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>?): Boolean {
        if (sender.isOp) {
            println("Converting legacy claims...")
            val config = Claims.instance!!.config

            val chunkKeys = config.getConfigurationSection("regions")?.getKeys(false)!!

            println("Found ${chunkKeys.size} chunks to convert")

            for (c in chunkKeys) {
                println("Converting chunk $c")
                val parent = config.getString("regions.$c.parent")

                if (parent != null) {
                    val uuid = Database.getColWhere("USER_DATA", "MINECRAFT_ID", "PARENT_REGION_ID", parent) as String

                    println("Found Chunk: $c | Owner: $uuid")

                    val claim = ClaimedChunk(Bukkit.getWorld("world")!!.getChunkAt(c.toLong()), UUID.fromString(uuid))
                    ClaimManager.addClaim(claim)
                }
            }
            return true
        }

        return false
    }
}