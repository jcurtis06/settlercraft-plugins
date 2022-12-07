package com.settlercraft.settlercore.settler

import com.settlercraft.settlercore.settler.actionbar.StatusMessage
import net.kyori.adventure.text.Component
import java.util.UUID

@Suppress("unused")
data class Settler(val uuid: UUID, var name: String, var money: Double = 0.0) {
    private var statusMsg = mutableListOf<StatusMessage>()

    /**
     * Adds to the status message
     * This appears above the hot bar.
     * @param msg The message to add
     * @see StatusMessage
     */
    fun addStatusMsg(msg: StatusMessage) {
        if (statusMsg.find { it.id == msg.id } == null) {
            statusMsg.add(msg)
        }
    }

    /**
     * Removes from the status message
     * This appears above the hot bar.
     * @param id The message to remove
     * @see StatusMessage
     */
    fun removeStatusMsg(id: String) {
        statusMsg.remove(statusMsg.find { it.id == id })
    }

    /**
     * Formats the status message to a Component
     * @return The formatted status message
     * @see StatusMessage
     */
    fun formatStatusMsg(): Component {
        var msg = Component.text("")
        for (i in statusMsg) {
            msg = msg.append(Component.text(i.get()))
        }
        return msg
    }
}