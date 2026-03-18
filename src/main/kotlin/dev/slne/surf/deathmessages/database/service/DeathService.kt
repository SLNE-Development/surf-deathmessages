package dev.slne.surf.deathmessages.database.service

import dev.slne.surf.deathmessages.database.Death
import dev.slne.surf.surfapi.core.api.util.requiredService
import java.util.*

private val deathService = requiredService<DeathService>()

interface DeathService {

    suspend fun saveDeath(death: Death): Death

    suspend fun deleteDeath(deathUuid: UUID): Int

    suspend fun findAll(amount: Int = 500): List<Death>

    suspend fun findLastDeath(playerUuid: UUID): Death?

    suspend fun findHistory(playerUuid: UUID): List<Death>

    suspend fun findDeathByUuid(deathUuid: UUID): Death?

    suspend fun findDeathByDeathUuid(deathUuid: UUID): Death?

    suspend fun createUnusedDeathUuid(): UUID

    companion object : DeathService by deathService
}