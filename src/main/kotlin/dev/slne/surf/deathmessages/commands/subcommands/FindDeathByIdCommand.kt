package dev.slne.surf.deathmessages.commands.subcommands

import dev.jorel.commandapi.arguments.UUIDArgument
import dev.jorel.commandapi.kotlindsl.argument
import dev.jorel.commandapi.kotlindsl.subcommand
import dev.slne.surf.deathmessages.database.service.deathService
import dev.slne.surf.deathmessages.permissions.Permissions
import dev.slne.surf.surfapi.bukkit.api.command.executors.anyExecutorSuspend
import dev.slne.surf.surfapi.bukkit.api.extensions.server
import dev.slne.surf.surfapi.core.api.messages.adventure.buildText
import dev.slne.surf.surfapi.core.api.messages.adventure.sendText
import dev.slne.surf.surfapi.core.api.util.dateTimeFormatter
import java.util.*

fun findDeathByIdCommand() = subcommand("findById") {
    withPermission(Permissions.PLAYER_DEATH_FIND_BY_ID_COMMAND)
    argument(UUIDArgument("deathId"))

    anyExecutorSuspend { sender, args ->
        val deathId = args["deathId"] as UUID
        val death = deathService.findByDeathUuid(deathId)

        if (death == null) {
            sender.sendText {
                appendErrorPrefix()
                error("Der angegebene Tod mit der ID")
                appendSpace()
                variableValue(deathId.toString())
                appendSpace()
                error("konnte nicht gefunden werden.")
            }
            return@anyExecutorSuspend
        }

        val playerName = server.getPlayer(death.playerUuid)?.name
        val reason = if (death.reason != null) {
            buildText { append(death.reason) }
        } else {
            buildText { error("Todesursache unbekannt") }
        }


        sender.sendText {
            appendInfoPrefix()
            info("Tod mit der ID")
            appendSpace()
            variableValue(deathId.toString())
            appendSpace()
            info("gefunden:")

            appendNewInfoPrefixedLine()
            info("Spieler:")
            appendSpace()
            variableValue(playerName ?: "#Unbekannt")

            appendNewInfoPrefixedLine()
            info("Zeitpunkt:")
            appendSpace()
            variableValue(dateTimeFormatter.format(death.diedAt))

            appendNewInfoPrefixedLine()
            info("Todesursache:")
            appendSpace()
            append(reason)
        }
    }
}