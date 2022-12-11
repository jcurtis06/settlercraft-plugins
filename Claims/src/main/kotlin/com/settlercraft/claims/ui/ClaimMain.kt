package com.settlercraft.claims.ui

import com.settlercraft.claims.Claims
import com.settlercraft.claims.claim.ClaimedChunk
import com.settlercraft.claims.claim.ClaimManager
import com.settlercraft.settlercore.econ.Economy
import com.settlercraft.settlercore.settler.Settlers
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.Inventory

class ClaimMain : Listener {
    private val inv: Inventory = Bukkit.createInventory(null, 27, Component.text("Claim Menu"))

    private var price: Double = 20.0

    init {
        Claims.instance!!.registerListener(this)
    }

    fun open(player: Player) {
        price = ClaimManager.priceOfLandFor(player.uniqueId)

        if (ClaimManager.isInClaim(player.location)) {
            inv.setItem(12, GuiUtils.createGuiItem(
                Material.REDSTONE_BLOCK,
                "§cCannot Claim Chunk",
                "§7This chunk is §9already claimed!"
            ))
        } else {
            if (Settlers.getSettler(player.uniqueId)!!.money < price) {
                inv.setItem(12, GuiUtils.createGuiItem(
                    Material.REDSTONE_BLOCK,
                    "§cCannot Claim Chunk",
                    "§7You do not have §9$$price!",
                ))
            } else {
                inv.setItem(12, GuiUtils.createGuiItem(
                    Material.EMERALD_BLOCK,
                    "§aClaim Chunk",
                    "§7Cost: §9$$price",
                    "§8Price of chunks increase",
                    "§8based on §9number of chunks owned"
                ))
            }
        }

        inv.setItem(13, GuiUtils.createGuiItem(
            Material.PAPER,
            "§aInformation",
            "§7You own §9${Settlers.getSettler(player.uniqueId)!!.chunks} §7chunks",
        ))

        inv.setItem(14, GuiUtils.createGuiItem(
            Material.REPEATER,
            "§aManage Chunks",
            "§7Manage §9all §7your chunks"
        ))

        for (i in inv.contents) {
            if (i == null) {
                inv.setItem(inv.firstEmpty(), GuiUtils.createGuiItem(Material.GRAY_STAINED_GLASS_PANE, " "))
            }
        }

        player.openInventory(inv)
    }

    @EventHandler
    fun onInventoryClick(event: InventoryClickEvent) {
        if (event.inventory == inv) {
            event.isCancelled = true

            val p = event.whoClicked as Player

            when (event.slot) {
                12 -> {
                    if (Settlers.getSettler(p.uniqueId)!!.money < price || ClaimManager.isInClaim(p.location)) {
                        p.playSound(p.location, Sound.ENTITY_VILLAGER_NO, 1f, 1f)
                        return
                    }

                    Economy.withdraw(p.uniqueId, price)
                    val claim = ClaimedChunk(p.location, p.uniqueId)
                    ClaimManager.addClaim(claim)
                    p.playSound(p.location, Sound.ENTITY_PLAYER_LEVELUP, 1f, 1f)
                    p.closeInventory()
                }
                14 -> {
                    // manage
                    p.sendMessage("Manage...")
                }
            }
        }
    }
}