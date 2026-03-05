package dev.slne.surf.deathmessages.commands.subcommands

import dev.jorel.commandapi.arguments.UUIDArgument
import dev.jorel.commandapi.kotlindsl.argument
import dev.jorel.commandapi.kotlindsl.subcommand
import dev.slne.surf.deathmessages.appendCopyable
import dev.slne.surf.deathmessages.database.service.DeathService
import dev.slne.surf.deathmessages.gui.DeathHistoryGui
import dev.slne.surf.deathmessages.permissions.Permissions
import dev.slne.surf.surfapi.bukkit.api.command.executors.anyExecutorSuspend
import dev.slne.surf.surfapi.bukkit.api.extensions.server
import dev.slne.surf.surfapi.core.api.messages.adventure.buildText
import dev.slne.surf.surfapi.core.api.messages.adventure.sendText
import dev.slne.surf.surfapi.core.api.util.dateTimeFormatter
import net.kyori.adventure.text.event.ClickEvent
import net.kyori.adventure.text.event.HoverEvent
import org.bukkit.entity.Player
import java.util.*

fun findDeathByIdCommand() = subcommand("findById") {
    withPermission(Permissions.PLAYER_DEATH_FIND_BY_ID_COMMAND)
    argument(UUIDArgument("deathId"))

    anyExecutorSuspend { sender, args ->
        val deathId = args["deathId"] as UUID
        val death = DeathService.findDeathByUuid(deathId)

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

        sender.sendText {
            appendInfoPrefix()
            spacer("-".repeat(30))

            val playerName = server.getOfflinePlayer(death.playerUuid).name ?: "#Unbekannt"
            appendNewInfoPrefixedLine()
            appendCopyable("Spieler", playerName, "den Namen")

            val diedAtFormatted = dateTimeFormatter.format(death.diedAt)
            appendNewInfoPrefixedLine()
            appendCopyable("Zeitpunkt", diedAtFormatted, "den Zeitpunkt")

            val worldName = death.location.world?.name ?: "#Unbekannt"
            appendNewInfoPrefixedLine()
            appendCopyable("Welt", worldName, "den Weltnamen")

            val cords = "${death.location.blockX} ${death.location.blockY} ${death.location.blockZ}"
            appendNewInfoPrefixedLine()
            appendCopyable("Koordinaten", cords, "die Koordinaten")

            appendNewInfoPrefixedLine()
            appendCopyable("Todes-ID", deathId.toString(), "die Todes-ID")

            val deathReason = death.reason ?: buildText { spacer("Todesursache unbekannt") }
            appendNewInfoPrefixedLine()
            appendCopyable("Todesursache", deathReason, "die Todesursache")

            appendNewInfoPrefixedLine()
            append(
                buildText { spacer("[Zu Todesort teleportieren]") }
                    .clickEvent(ClickEvent.callback { audience ->
                        val player = audience as? Player ?: return@callback
                        player.teleportAsync(death.location)
                        player.sendText {
                            appendSuccessPrefix()
                            success("Du wurdest zum Todesort teleportiert.")
                        }
                    })
                    .hoverEvent(HoverEvent.showText(buildText { info("Klicke um dich zum Todesort zu teleportieren.") }))
            )
            appendSpace()
            append(
                buildText { spacer("[Inventar ansehen]") }
                    .clickEvent(ClickEvent.callback { audience ->
                        val player = audience as? Player ?: return@callback
                        DeathHistoryGui.itemsGui(player, death).open()
                    })
                    .hoverEvent(HoverEvent.showText(buildText { info("Klicke um dich zum Todesort zu teleportieren.") }))
            )

            appendNewInfoPrefixedLine()
            spacer("-".repeat(30))
        }
    }
}