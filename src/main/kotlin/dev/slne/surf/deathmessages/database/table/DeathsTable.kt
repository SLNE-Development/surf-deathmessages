package dev.slne.surf.deathmessages.database.table

import dev.slne.surf.database.columns.component
import dev.slne.surf.database.columns.nativeUuid
import dev.slne.surf.database.columns.time.offsetDateTime
import dev.slne.surf.database.libs.org.jetbrains.exposed.v1.core.dao.id.LongIdTable

object DeathsTable : LongIdTable("death_deaths") {
    val deathUuid = nativeUuid("death_uuid").uniqueIndex()
    val playerUuid = nativeUuid("player_uuid")

    val worldUuid = nativeUuid("world_uuid")
    val x = double("x")
    val y = double("y")
    val z = double("z")

    val diedAt = offsetDateTime("died_at")
    val reason = component("reason").nullable()
    val lostItems = blob("lost_items") // Save as Bytes may be better
}