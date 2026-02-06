package dev.slne.surf.deathmessages.commands.subcommands

import dev.jorel.commandapi.arguments.AsyncPlayerProfileArgument
import dev.jorel.commandapi.kotlindsl.argument
import dev.jorel.commandapi.kotlindsl.subcommand
import dev.slne.surf.deathmessages.database.service.deathService
import dev.slne.surf.deathmessages.permissions.Permissions
import dev.slne.surf.surfapi.bukkit.api.command.executors.anyExecutorSuspend
import dev.slne.surf.surfapi.bukkit.api.command.util.awaitAsyncPlayerProfile
import dev.slne.surf.surfapi.core.api.messages.adventure.buildText
import dev.slne.surf.surfapi.core.api.messages.adventure.sendText
import dev.slne.surf.surfapi.core.api.util.dateTimeFormatter
import net.kyori.adventure.text.event.ClickEvent
import net.kyori.adventure.text.event.HoverEvent

fun lastDeathCommand() = subcommand("last") {
    withPermission(Permissions.PLAYER_DEATH_LOOKUP_COMMAND)
    argument(AsyncPlayerProfileArgument("player"))

    anyExecutorSuspend { sender, args ->
        val profile = args.awaitAsyncPlayerProfile("player")
        val uuid = profile.id

        if (uuid == null) {
            sender.sendText {
                appendErrorPrefix()
                error("Der angegebene Spieler")
                appendSpace()
                variableValue(profile.name ?: "#Unbekannt")
                appendSpace()
                error("Konnte nicht gefunden werden.")
            }
            return@anyExecutorSuspend
        }

        val lastDeath = deathService.findLastDeath(uuid)

        if (lastDeath == null) {
            sender.sendText {
                appendErrorPrefix()
                error("Es wurden keine Tod für den Spieler")
                appendSpace()
                variableValue(profile.name ?: uuid.toString())
                appendSpace()
                error("gefunden.")
            }
            return@anyExecutorSuspend
        }

        sender.sendText {
            appendInfoPrefix()
            info("Letzter Tod von")
            appendSpace()
            variableValue(profile.name ?: "#Unbekannt")

            appendNewInfoPrefixedLine()
            info("Zeitpunkt:")
            appendSpace()
            variableValue(dateTimeFormatter.format(lastDeath.diedAt))

            appendNewInfoPrefixedLine()
            info("Welt:")
            appendSpace()
            variableValue(lastDeath.location.world?.name ?: "#Unbekannt")

            appendNewInfoPrefixedLine()
            info("Koordinaten:")
            appendSpace()
            variableValue(
                "X:${lastDeath.location.blockX} " +
                        "Y:${lastDeath.location.blockY} " +
                        "Z:${lastDeath.location.blockZ}"
            )

            appendNewInfoPrefixedLine()
            info("Todes-ID:")
            appendSpace()
            variableValue(lastDeath.deathUuid.toString())

            appendNewInfoPrefixedLine()
            spacer("Zum Todesort teleportieren")
                .clickEvent(
                    ClickEvent.callback { audience ->
                        val player = audience as? org.bukkit.entity.Player ?: return@callback
                        player.teleportAsync(lastDeath.location)
                        player.sendText {
                            appendSuccessPrefix()
                            success("Du wurdest zum Todesort von")
                            appendSpace()
                            variableValue(profile.name ?: "#Unbekannt")
                            appendSpace()
                            success("teleportiert.")
                        }
                    }
                )
                .hoverEvent(
                    HoverEvent.showText(
                        lastDeath.reason ?: buildText { error("Kein Todesgrund vorhanden") }
                    )
                )
        }
    }
}