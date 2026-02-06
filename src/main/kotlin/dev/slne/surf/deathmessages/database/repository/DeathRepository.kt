package dev.slne.surf.deathmessages.database.repository

import dev.slne.surf.database.libs.org.jetbrains.exposed.v1.core.ResultRow
import dev.slne.surf.database.libs.org.jetbrains.exposed.v1.core.SortOrder
import dev.slne.surf.database.libs.org.jetbrains.exposed.v1.core.eq
import dev.slne.surf.database.libs.org.jetbrains.exposed.v1.core.statements.api.ExposedBlob
import dev.slne.surf.database.libs.org.jetbrains.exposed.v1.r2dbc.deleteWhere
import dev.slne.surf.database.libs.org.jetbrains.exposed.v1.r2dbc.insert
import dev.slne.surf.database.libs.org.jetbrains.exposed.v1.r2dbc.selectAll
import dev.slne.surf.database.libs.org.jetbrains.exposed.v1.r2dbc.transactions.suspendTransaction
import dev.slne.surf.deathmessages.database.Death
import dev.slne.surf.deathmessages.database.table.DeathsTable
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toSet
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.inventory.ItemStack
import java.io.ByteArrayOutputStream
import java.io.DataOutputStream
import java.util.*

val deathRepository = DeathRepository()

class DeathRepository {

    suspend fun findLastDeath(uuid: UUID): Death? =
        suspendTransaction {
            DeathsTable
                .selectAll()
                .where(DeathsTable.playerUuid eq uuid)
                .orderBy(DeathsTable.id, SortOrder.ASC)
                .limit(1)
                .firstOrNull()
                ?.toDeath()
        }

    suspend fun findHistory(uuid: UUID): Set<Death> =
        suspendTransaction {
            DeathsTable
                .selectAll()
                .where { DeathsTable.playerUuid eq uuid }
                .orderBy(DeathsTable.id, SortOrder.DESC)
                .map { it.toDeath() }
                .toSet()
        }

    suspend fun findByDeathUuid(uuid: UUID): Death? =
        suspendTransaction {
            DeathsTable
                .selectAll()
                .where { DeathsTable.deathUuid eq uuid }
                .firstOrNull()
                ?.toDeath()
        }

    suspend fun save(death: Death): Death =
        suspendTransaction {
            DeathsTable.insert {
                it[deathUuid] = death.deathUuid
                it[playerUuid] = death.playerUuid
                it[worldUuid] = death.location.world.uid
                it[x] = death.location.x
                it[y] = death.location.y
                it[z] = death.location.z
                it[diedAt] = death.diedAt
                it[reason] = death.reason
                it[lostItems] = ExposedBlob(serializeItems(death.lostItems))
            }
            death
        }

    suspend fun delete(uuid: UUID) =
        suspendTransaction {
            DeathsTable.deleteWhere { DeathsTable.deathUuid eq uuid }
        }
}


fun ResultRow.toDeath(): Death {
    val worldUuid = this[DeathsTable.worldUuid]
    val world = Bukkit.getWorld(worldUuid)
        ?: error("World with Uuid $worldUuid not found!")

    return Death(
        playerUuid = this[DeathsTable.playerUuid],
        deathUuid = this[DeathsTable.deathUuid],
        location = Location(
            world,
            this[DeathsTable.x],
            this[DeathsTable.y],
            this[DeathsTable.z]
        ),
        diedAt = this[DeathsTable.diedAt],
        reason = this[DeathsTable.reason],
        lostItems = deserializeItems(this[DeathsTable.lostItems].bytes)
    )
}


fun serializeItems(items: List<ItemStack>): ByteArray = items.serializeItemsToBytes()
fun deserializeItems(bytes: ByteArray): List<ItemStack> = ItemStack.deserializeItemsFromBytes(bytes).toList()

fun List<ItemStack>.serializeItemsToBytes(): ByteArray = ByteArrayOutputStream().use { arrayOut ->
    DataOutputStream(arrayOut).use { dataOut ->
        dataOut.writeByte(1)
        dataOut.writeInt(this.size)

        for (item in this) {
            if (item.isEmpty) {
                dataOut.writeInt(0)
            } else {
                val itemBytes = item.serializeAsBytes()
                dataOut.writeInt(itemBytes.size)
                dataOut.write(itemBytes)
            }
        }

        arrayOut.toByteArray()
    }
} //TODO: Test this
