package com.settlercraft.settlercore.settler

import com.settlercraft.settlercore.settler.actionbar.StatusMessage
import net.kyori.adventure.text.Component
import java.util.UUID

@Suppress("unused")
data class Settler(val uuid: UUID, var name: String, var money: Double = 0.0, var chunks: Int = 0) {
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
        // example output: "§cClaim: §aWilderness §c| §cBalance: §a$0.00"
        return if (statusMsg.size == 1) {
            Component.text(statusMsg[0].get())
        } else {
            // loop
            var msg = ""
            for (i in statusMsg.indices) {
                msg += statusMsg[i].get()
                if (i != statusMsg.size - 1) {
                    msg += " §7| §r"
                }
            }
            Component.text(msg)
        }
    }
}