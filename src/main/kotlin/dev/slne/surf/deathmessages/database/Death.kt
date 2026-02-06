package dev.slne.surf.deathmessages.database

import net.kyori.adventure.text.Component
import org.bukkit.Location
import org.bukkit.inventory.ItemStack
import java.time.OffsetDateTime
import java.util.*

data class Death(
    val playerUuid: UUID,
    val deathUuid: UUID,
    val location: Location,
    val diedAt: OffsetDateTime,
    val reason: Component?,
    val lostItems: List<ItemStack>
)