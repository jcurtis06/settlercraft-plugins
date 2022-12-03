package com.settlercraft.settlercore.settler

import com.settlercraft.settlercore.data.Database
import net.kyori.adventure.text.Component
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

class SettlerListeners: Listener {
    @EventHandler
    fun onJoin(e: PlayerJoinEvent) {
        val player: Player = e.player
        if (Settlers.getSettler(e.player.uniqueId) == null) {
            if (Database.doesExist("settlers", "name", player.name)) {
                Database.setColWhere("settlers", "uuid", "name", player.name, player.uniqueId)
                Settlers.loadSettlers()
            } else {
                player.kick(Component.text("Please register at the SettlerCraft Website! If you have already registered, please contact an administrator"))
            }
        }
    }
}