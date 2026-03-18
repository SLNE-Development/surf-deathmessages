package dev.slne.surf.deathmessages.database

import net.kyori.adventure.text.Component
import org.bukkit.Location
import org.bukkit.inventory.ItemStack
import java.time.OffsetDateTime
import java.util.*

data class Death(
    val deathUuid: UUID,
    val playerUuid: UUID,
    val location: Location,
    val diedAt: OffsetDateTime,
    val reason: Component?,
    val isKeepInventory: Boolean,
    val deathInventory: Array<ItemStack?>
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Death

        if (isKeepInventory != other.isKeepInventory) return false
        if (deathUuid != other.deathUuid) return false
        if (playerUuid != other.playerUuid) return false
        if (location != other.location) return false
        if (diedAt != other.diedAt) return false
        if (reason != other.reason) return false
        if (!deathInventory.contentEquals(other.deathInventory)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = isKeepInventory.hashCode()
        result = 31 * result + deathUuid.hashCode()
        result = 31 * result + playerUuid.hashCode()
        result = 31 * result + location.hashCode()
        result = 31 * result + diedAt.hashCode()
        result = 31 * result + (reason?.hashCode() ?: 0)
        result = 31 * result + deathInventory.contentHashCode()
        return result
    }
}