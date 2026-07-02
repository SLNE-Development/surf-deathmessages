package dev.slne.surf.deathmessages.database.service

import dev.slne.surf.deathmessages.command.subcommand.DeathLookupFilter
import dev.slne.surf.deathmessages.database.Death
import dev.slne.surf.deathmessages.database.repository.DeathRepository
import kotlin.math.pow

object DeathLookupService {
    suspend fun lookup(filter: DeathLookupFilter): List<Death> {
        val source = if (filter.playerUuid != null) {
            DeathRepository.findHistory(filter.playerUuid)
        } else {
            DeathRepository.findAll()
        }

        return source
            .asSequence()
            .filter { it.location.world.name == filter.worldName }
            .filter { filter.after == null || it.diedAt.isAfter(filter.after) }
            .filter {
                if (filter.radius == null) true
                else {
                    val loc = it.location
                    val distanceSq = (loc.x - filter.centerX).pow(2) +
                            (loc.y - filter.centerY).pow(2) +
                            (loc.z - filter.centerZ).pow(2)
                    distanceSq <= filter.radius.pow(2)
                }
            }
            .sortedByDescending { it.diedAt }
            .take(filter.limit)
            .toList()
    }
}