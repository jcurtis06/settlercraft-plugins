package com.settlercraft.settlercore.settler

import net.kyori.adventure.text.Component
import java.util.UUID

@Suppress("unused")
data class Settler(val uuid: UUID, var name: String, var money: Double = 0.0) {
    private var statusMsg = mutableListOf<String>()

    /**
     * Adds to the status message
     * This appears above the hot bar.
     * @param msg The message to add
     */
    fun addStatusMsg(msg: String) {
        statusMsg.add(msg)
    }

    /**
     * Removes from the status message
     * This appears above the hot bar.
     * @param msg The message to remove
     */
    fun removeStatusMsg(msg: String) {
        statusMsg.remove(msg)
    }

    /**
     * Formats the status message to a Component
     * @return The formatted status message
     */
    fun formatStatusMsg(): Component {
        return Component.text(statusMsg.joinToString(" | ") { "§a$it" })
    }
}