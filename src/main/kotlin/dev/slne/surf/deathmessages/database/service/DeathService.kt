package dev.slne.surf.deathmessages.database.service

import dev.slne.surf.deathmessages.database.Death
import dev.slne.surf.deathmessages.database.repository.deathRepository
import dev.slne.surf.surfapi.core.api.util.requiredService
import java.util.UUID

val deathService = DeathService()

class DeathService {
    suspend fun findLastDeath(playerUuid: UUID): Death? =
        deathRepository.findLastDeath(playerUuid)

    suspend fun findHistory(playerUuid: UUID): Set<Death> =
        deathRepository.findHistory(playerUuid)

    suspend fun findByDeathUuid(deathUuid: UUID): Death? =
        deathRepository.findByDeathUuid(deathUuid)

    suspend fun saveDeath(death: Death): Death =
        deathRepository.save(death)

    suspend fun deleteDeath(deathUuid: UUID) {
        deathRepository.delete(deathUuid)
    }

    suspend fun createUnusedDeathUuid(): UUID {
        var uuid: UUID

        do {
            uuid = UUID.randomUUID()
        } while (deathRepository.findByDeathUuid(uuid) != null)

        return uuid
    }
}