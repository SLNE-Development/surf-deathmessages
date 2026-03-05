package dev.slne.surf.deathmessages.commands.subcommands

import dev.jorel.commandapi.CommandAPI
import dev.jorel.commandapi.arguments.MapArgumentBuilder
import dev.jorel.commandapi.kotlindsl.getValue
import dev.jorel.commandapi.kotlindsl.optionalArgument
import dev.jorel.commandapi.kotlindsl.subcommand
import dev.slne.surf.deathmessages.commands.sendDeathInfoMessage
import dev.slne.surf.deathmessages.database.Death
import dev.slne.surf.deathmessages.database.service.DeathService
import dev.slne.surf.deathmessages.permissions.Permissions
import dev.slne.surf.surfapi.bukkit.api.command.executors.playerExecutorSuspend
import dev.slne.surf.surfapi.core.api.font.toSmallCaps
import dev.slne.surf.surfapi.core.api.messages.adventure.buildText
import dev.slne.surf.surfapi.core.api.messages.adventure.clickOpensUrl
import dev.slne.surf.surfapi.core.api.messages.adventure.sendText
import dev.slne.surf.surfapi.core.api.messages.builder.SurfComponentBuilder
import dev.slne.surf.surfapi.core.api.messages.pagination.Pagination
import dev.slne.surf.surfapi.core.api.service.PlayerLookupService
import dev.slne.surf.surfapi.core.api.util.dateTimeFormatter
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import net.kyori.adventure.text.event.ClickEvent
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.entity.Player
import java.time.Instant
import java.time.OffsetDateTime
import java.time.temporal.ChronoUnit
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import kotlin.math.pow

private data class DeathRenderData(
    val names: Map<UUID, String>,
    val record: Death
)

private val deathPagination = Pagination<DeathRenderData> {
    title {
        info("Suchergebnisse".toSmallCaps(), TextDecoration.BOLD)
    }
    resultsPerPage = 10

    rowRenderer { (names, record), _ ->
        listOf(
            buildText {
                appendLinePrefix()

                append {
                    spacer(record.diedAt.formatAgo())
                        .hoverEvent(buildText {
                            info("Datum: ")
                            spacer(dateTimeFormatter.format(record.diedAt))
                        })
                }

                appendSpace()
                spacer("-")
                appendSpace()

                append {
                    val name = names[record.playerUuid] ?: "#Unbekannt"
                    variableValue(name)

                    hoverEvent(buildText {
                        info("UUID: ")
                        variableValue(record.playerUuid.toString())
                        appendNewline()
                        spacer("Klicke, um das Profil zu öffnen.")
                    })
                    if (name != "#Unbekannt") {
                        clickOpensUrl("https://laby.net/$name")
                    }
                }


                append {
                    appendSpace()
                    info("starb bei")
                    appendSpace()

                    val loc = record.location
                    variableValue("${loc.blockX} ${loc.blockY} ${loc.blockZ}")
                    appendSpace()
                    spacer("(${record.location.world.name})")

                    hoverEvent(buildText {
                        spacer("Klicke, um den Eintrag im Detail zu sehen!", TextDecoration.UNDERLINED)
                    })
                    clickEvent(
                        ClickEvent.callback { audience ->
                            val player = audience as? Player ?: return@callback
                            player.sendDeathInfoMessage(record)
                        }
                    )
                }
            }
        )
    }
}

fun lookupCommand() = subcommand("lookup") {
    withPermission(Permissions.PLAYER_DEATH_LOOKUP_COMMAND)

    optionalArgument(
        MapArgumentBuilder<String, String>("query", ':')
            .withKeyMapper { it.lowercase() }
            .withValueMapper { it }
            .withKeyList(listOf("--player", "--time", "--radius", "--page", "--limit"))
            .withoutValueList()
            .build()
    )

    playerExecutorSuspend { player, args ->
        val query: Map<String, String>? by args

        player.sendText {
            info("Es wird nach Todes-Einträgen gesucht…")
        }

        val filter = query?.parseDeathFilters(player) ?: DeathLookupFilter.empty(player)
        val page = query?.get("--page")?.toIntOrNull() ?: 1

        val deaths = DeathLookupService.lookup(filter)

        if (deaths.isEmpty()) {
            throw CommandAPI.failWithString("Es wurden keine Todes-Einträge gefunden.")
        }

        val names = ConcurrentHashMap<UUID, String>()
        coroutineScope {
            deaths.map { it.playerUuid }.distinct().forEach { uuid ->
                launch {
                    val name = PlayerLookupService.getUsername(uuid)
                    names[uuid] = name ?: uuid.toString()
                }
            }
        }

        val renderData = deaths.map { DeathRenderData(names, it) }

        player.sendText {
            append(deathPagination.renderComponent(renderData, page))
        }
    }
}

private val rangeRegex by lazy { Regex("""(\d+)([smhdw])""", RegexOption.IGNORE_CASE) }

private suspend fun Map<String, String>.parseDeathFilters(player: Player): DeathLookupFilter {
    val playerUuid = this["--player"]?.let { PlayerLookupService.getUuid(it) }

    val radius = (this["--radius"] ?: this["--range"])?.toDoubleOrNull()

    val after = this["--time"]?.let { rangeStr ->
        val match = rangeRegex.find(rangeStr.trim()) ?: return@let null
        val (valueStr, unit) = match.destructured
        val value = valueStr.toLongOrNull() ?: return@let null

        val seconds = when (unit.lowercase()) {
            "s" -> value
            "m" -> value * 60
            "h" -> value * 3600
            "d" -> value * 86400
            "w" -> value * 604800
            else -> 0L
        }
        OffsetDateTime.now().minusSeconds(seconds)
    }

    return DeathLookupFilter(
        playerUuid = playerUuid,
        after = after,
        radius = radius,
        centerX = player.location.x,
        centerY = player.location.y,
        centerZ = player.location.z,
        worldName = player.world.name,
        limit = this["--limit"]?.toIntOrNull() ?: 50
    )
}

data class DeathLookupFilter(
    val playerUuid: UUID?,
    val after: OffsetDateTime?,
    val radius: Double?,
    val centerX: Double,
    val centerY: Double,
    val centerZ: Double,
    val worldName: String,
    val limit: Int
) {
    companion object {
        fun empty(player: Player) = DeathLookupFilter(
            null, null, null,
            player.location.x, player.location.y, player.location.z,
            player.world.name, 50
        )
    }
}


object DeathLookupService {
    suspend fun lookup(filter: DeathLookupFilter): List<Death> {
        val source = if (filter.playerUuid != null) {
            DeathService.findHistory(filter.playerUuid)
        } else {
            DeathService.findAll()
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

private fun OffsetDateTime.formatAgo(): String {
    val then = toInstant()
    val now = Instant.now()

    val totalSeconds = ChronoUnit.SECONDS.between(then, now)
    if (totalSeconds < 1) return "now"

    val seconds = totalSeconds % 60
    val totalMinutes = totalSeconds / 60
    val minutes = totalMinutes % 60
    val totalHours = totalMinutes / 60
    val hours = totalHours % 24
    val totalDays = totalHours / 24
    val days = totalDays % 30
    val totalMonths = totalDays / 30
    val months = totalMonths % 12
    val years = totalMonths / 12

    return when {
        years > 0 -> "%d.%02dy ago".format(years, months)
        totalMonths > 0 -> "%d.%02dmo ago".format(totalMonths, days)
        totalDays > 0 -> "%d.%02dd ago".format(totalDays, hours)
        totalHours > 0 -> "%d.%02dh ago".format(totalHours, minutes)
        totalMinutes > 0 -> "%d.%02dm ago".format(totalMinutes, seconds)
        else -> "%d.%02ds ago".format(seconds, 0)
    }
}

private fun SurfComponentBuilder.appendLinePrefix() = darkSpacer("»").appendSpace()