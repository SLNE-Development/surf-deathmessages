package dev.slne.surf.deathmessages.database.service

import com.google.auto.service.AutoService
import dev.slne.surf.database.libs.org.jetbrains.exposed.v1.core.ResultRow
import dev.slne.surf.database.libs.org.jetbrains.exposed.v1.core.SortOrder
import dev.slne.surf.database.libs.org.jetbrains.exposed.v1.core.eq
import dev.slne.surf.database.libs.org.jetbrains.exposed.v1.core.statements.api.ExposedBlob
import dev.slne.surf.database.libs.org.jetbrains.exposed.v1.r2dbc.deleteWhere
import dev.slne.surf.database.libs.org.jetbrains.exposed.v1.r2dbc.insert
import dev.slne.surf.database.libs.org.jetbrains.exposed.v1.r2dbc.selectAll
import dev.slne.surf.database.libs.org.jetbrains.exposed.v1.r2dbc.transactions.suspendTransaction
import dev.slne.surf.deathmessages.database.Death
import dev.slne.surf.deathmessages.database.tables.DeathsTable
import dev.slne.surf.surfapi.bukkit.api.extensions.server
import dev.slne.surf.surfapi.core.api.util.logger
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import net.kyori.adventure.util.Services
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.inventory.ItemStack
import java.util.*

@AutoService(DeathService::class)
class DeathServiceImpl : DeathService, Services.Fallback {

    override suspend fun findLastDeath(playerUuid: UUID): Death? =
        suspendTransaction {
            DeathsTable
                .selectAll()
                .where(DeathsTable.playerId eq playerUuid)
                .orderBy(DeathsTable.id, SortOrder.DESC)
                .limit(1)
                .firstOrNull()
                ?.toDeath()
        }

    override suspend fun findHistory(playerUuid: UUID): List<Death> =
        suspendTransaction {
            DeathsTable
                .selectAll()
                .where { DeathsTable.playerId eq playerUuid }
                .orderBy(DeathsTable.id, SortOrder.DESC)
                .map { it.toDeath() }
                .toList()
        }

    override suspend fun findDeathByUuid(deathUuid: UUID): Death? =
        suspendTransaction {
            DeathsTable
                .selectAll()
                .where { DeathsTable.deathId eq deathUuid }
                .firstOrNull()
                ?.toDeath()
        }

    override suspend fun findDeathByDeathUuid(deathUuid: UUID): Death? =
        findDeathByUuid(deathUuid)

    override suspend fun deleteDeath(deathUuid: UUID): Int =
        suspendTransaction {
            DeathsTable.deleteWhere { DeathsTable.deathId eq deathUuid }
        }

    override suspend fun findAll(amount: Int): List<Death> =
        suspendTransaction {
            DeathsTable.selectAll()
                .orderBy(DeathsTable.diedAt to SortOrder.DESC)
                .limit(amount)
                .map { it.toDeath() }
                .toList()
        }

    override suspend fun saveDeath(death: Death): Death =
        suspendTransaction {
            DeathsTable.insert {
                it[deathId] = death.deathUuid
                it[playerId] = death.playerUuid
                it[worldId] = death.location.world.uid
                it[x] = death.location.x
                it[y] = death.location.y
                it[z] = death.location.z
                it[diedAt] = death.diedAt
                it[reason] = death.reason
                it[isKeepInventory] = death.isKeepInventory
                it[lostItems] = ExposedBlob(serializeItems(death.deathInventory))
            }
            death
        }

    override suspend fun createUnusedDeathUuid(): UUID {
        var uuid: UUID
        do {
            uuid = UUID.randomUUID()
        } while (findDeathByUuid(uuid) != null)
        return uuid
    }

    private fun ResultRow.toDeath(): Death {
        val worldUuid = this[DeathsTable.worldId]
        val world = Bukkit.getWorld(worldUuid)

        if (world == null) {
            logger().atWarning().log(
                "Death record ${this[DeathsTable.deathId]} references unknown world $worldUuid"
            )
        }

        return Death(
            playerUuid = this[DeathsTable.playerId],
            deathUuid = this[DeathsTable.deathId],
            location = Location(
                world ?: server.worlds.first(),
                this[DeathsTable.x],
                this[DeathsTable.y],
                this[DeathsTable.z]
            ),
            diedAt = this[DeathsTable.diedAt],
            reason = this[DeathsTable.reason],
            isKeepInventory = this[DeathsTable.isKeepInventory],
            deathInventory = deserializeItems(this[DeathsTable.lostItems].bytes)
        )
    }

    private fun serializeItems(items: Array<ItemStack?>): ByteArray {
        val array = Array(items.size) { i -> items[i] ?: ItemStack.empty() }
        return ItemStack.serializeItemsAsBytes(array)
    }

    private fun deserializeItems(bytes: ByteArray): Array<ItemStack?> {
        return ItemStack.deserializeItemsFromBytes(bytes)
            .map { it as ItemStack? }
            .toTypedArray()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        return true
    }

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }
}