package com.settlercraft.claims.ui

import com.settlercraft.claims.Claims
import com.settlercraft.claims.claim.Claim
import com.settlercraft.claims.claim.ClaimError
import com.settlercraft.claims.claim.ClaimHandler
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.Inventory

class ClaimMain(): Listener {
    private val inv: Inventory = Bukkit.createInventory(null, 27, Component.text("Claim Menu"))

    init {
        Claims.instance!!.registerListener(this)
    }

    fun open(player: Player) {
        inv.setItem(12, GuiUtils.createGuiItem(
            Material.GRASS_BLOCK,
            "Claim",
            "Claim a plot of land",
            "for yourself."
        ))
        inv.setItem(13, GuiUtils.createGuiItem(
            Material.PAPER,
            "Claim Info",
            "View information about",
            "this claim."
        ))
        inv.setItem(14, GuiUtils.createGuiItem(
            Material.BARRIER,
            "Manage",
            "Manage this plot of land."
        ))

        player.openInventory(inv)
    }

    @EventHandler
    fun onInventoryClick(event: InventoryClickEvent) {
        if (event.inventory == inv) {
            event.isCancelled = true

            val p = event.whoClicked as Player

            println("Clicked Slot #${event.slot}")

            when (event.slot) {
                12 -> {
                    val claim = Claim(p.location.x.toInt(), p.location.z.toInt(), p.uniqueId)

                    when (ClaimHandler.addClaim(claim)) {
                        ClaimError.SUCCESS -> {
                            p.sendMessage(Component.text("Claimed!"))
                        }
                        ClaimError.FAILED_TO_CLAIM -> {
                            p.sendMessage(Component.text("Failed to claim!"))
                        }
                        else -> {
                            p.sendMessage(Component.text("An error has occurred!"))
                        }
                    }
                }
                13 -> {
                    // info
                    p.sendMessage("Info...")
                }
                14 -> {
                    // manage
                    p.sendMessage("Manage...")
                }
            }
        }
    }
}