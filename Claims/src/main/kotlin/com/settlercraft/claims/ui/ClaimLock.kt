package com.settlercraft.claims.ui

import com.settlercraft.claims.Claims
import com.settlercraft.claims.claim.ClaimManager
import com.settlercraft.claims.claim.Lock
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent

class ClaimLock: Listener {
    val inv = Bukkit.createInventory(null, 27, Component.text("Claim Management"))

    fun open(player: Player) {
        Bukkit.getServer().pluginManager.registerEvents(this, Claims.instance!!)
        player.openInventory(inv)

        loadItems(player)
    }

    private fun loadItems(player: Player) {
        if (ClaimManager.getPlayerLock(player.uniqueId).getLock(Lock.BUILD)) {
            inv.setItem(12, GuiUtils.createGuiItem(
                Material.LIME_DYE,
                "§cBuilding",
                "§7Click to §cunlock §7building"
            ))
        } else {
            inv.setItem(12, GuiUtils.createGuiItem(
                Material.GRAY_DYE,
                "§aBuilding",
                "§7Click to §alock §7building"
            ))
        }
        if (ClaimManager.getPlayerLock(player.uniqueId).getLock(Lock.INTERACT)) {
            inv.setItem(13, GuiUtils.createGuiItem(
                Material.LIME_DYE,
                "§cInteractions",
                "§7Click to §cunlock §7interactions"
            ))
        } else {
            inv.setItem(13, GuiUtils.createGuiItem(
                Material.GRAY_DYE,
                "§aInteractions",
                "§7Click to §alock §7interactions",
            ))
        }
        if (ClaimManager.getPlayerLock(player.uniqueId).getLock(Lock.PVP)) {
            inv.setItem(14, GuiUtils.createGuiItem(
                Material.LIME_DYE,
                "§cPVE",
                "§7Click to §cunlock §7PVE",
                "§9Includes §lall §9entities"
            ))
        } else {
            inv.setItem(14, GuiUtils.createGuiItem(
                Material.GRAY_DYE,
                "§aPVP",
                "§7Click to §alock §7PVP",
                "",
                "§9Includes §lall §9entities"
            ))
        }
    }

    @EventHandler
    fun onClick(e: InventoryClickEvent) {
        if (e.clickedInventory == inv) {
            e.isCancelled = true

            val player = e.whoClicked as Player

            when (e.slot) {
                12 -> {
                    ClaimManager.toggleLock(player.uniqueId, Lock.BUILD)
                    loadItems(player)
                }
                13 -> {
                    ClaimManager.toggleLock(player.uniqueId, Lock.INTERACT)
                    loadItems(player)
                }
                14 -> {
                    ClaimManager.toggleLock(player.uniqueId, Lock.PVP)
                    loadItems(player)
                }
            }
        }
    }
}