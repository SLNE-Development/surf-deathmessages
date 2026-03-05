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
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.flow.toSet
import net.kyori.adventure.util.Services
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.inventory.ItemStack
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.DataInputStream
import java.io.DataOutputStream
import java.util.*

@AutoService(DeathService::class)
class DeathServiceImpl : DeathService, Services.Fallback {

    override suspend fun findLastDeath(playerUuid: UUID): Death? =
        suspendTransaction {
            DeathsTable
                .selectAll()
                .where(DeathsTable.playerUuid eq playerUuid)
                .orderBy(DeathsTable.id, SortOrder.DESC)
                .limit(1)
                .firstOrNull()
                ?.toDeath()
        }

    override suspend fun findHistory(playerUuid: UUID): Set<Death> =
        suspendTransaction {
            DeathsTable
                .selectAll()
                .where { DeathsTable.playerUuid eq playerUuid }
                .orderBy(DeathsTable.id, SortOrder.DESC)
                .map { it.toDeath() }
                .toSet()
        }

    override suspend fun findDeathByUuid(deathUuid: UUID): Death? =
        suspendTransaction {
            DeathsTable
                .selectAll()
                .where { DeathsTable.deathUuid eq deathUuid }
                .firstOrNull()
                ?.toDeath()
        }

    override suspend fun findDeathByDeathUuid(deathUuid: UUID): Death? =
        findDeathByUuid(deathUuid)

    override suspend fun deleteDeath(deathUuid: UUID): Int =
        suspendTransaction {
            DeathsTable.deleteWhere { DeathsTable.deathUuid eq deathUuid }
        }

    override suspend fun findAll(amount: Int): List<Death> = suspendTransaction {
        DeathsTable.selectAll()
            .orderBy(DeathsTable.diedAt to SortOrder.DESC)
            .limit(amount)
            .map { it.toDeath() }
            .toList()
    }

    override suspend fun saveDeath(death: Death): Death =
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

    override suspend fun createUnusedDeathUuid(): UUID {
        var uuid: UUID
        do {
            uuid = UUID.randomUUID()
        } while (findDeathByUuid(uuid) != null)
        return uuid
    }


    private fun ResultRow.toDeath(): Death {
        val worldUuid = this[DeathsTable.worldUuid]
        val world = Bukkit.getWorld(worldUuid)
            ?: error("World with UUID $worldUuid not found!")

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

    private fun serializeItems(items: List<ItemStack>): ByteArray {
        val out = ByteArrayOutputStream()
        val data = DataOutputStream(out)

        data.writeInt(items.size)

        for (item in items) {
            val bytes = item.serializeAsBytes()
            data.writeInt(bytes.size)
            data.write(bytes)
        }

        return out.toByteArray()
    }

    private fun deserializeItems(bytes: ByteArray): List<ItemStack> {
        val input = DataInputStream(ByteArrayInputStream(bytes))

        val size = input.readInt()
        val items = mutableListOf<ItemStack>()

        repeat(size) {
            val length = input.readInt()
            val itemBytes = ByteArray(length)
            input.readFully(itemBytes)

            items.add(ItemStack.deserializeBytes(itemBytes))
        }

        return items
    }

}
