package dev.slne.surf.deathmessages.commands.subcommands

import dev.jorel.commandapi.arguments.AsyncPlayerProfileArgument
import dev.jorel.commandapi.kotlindsl.argument
import dev.jorel.commandapi.kotlindsl.subcommand
import dev.slne.surf.deathmessages.appendCopyable
import dev.slne.surf.deathmessages.database.service.DeathService
import dev.slne.surf.deathmessages.gui.DeathHistoryGui
import dev.slne.surf.deathmessages.permissions.Permissions
import dev.slne.surf.surfapi.bukkit.api.command.executors.anyExecutorSuspend
import dev.slne.surf.surfapi.bukkit.api.command.util.awaitAsyncPlayerProfile
import dev.slne.surf.surfapi.core.api.messages.adventure.buildText
import dev.slne.surf.surfapi.core.api.messages.adventure.sendText
import dev.slne.surf.surfapi.core.api.util.dateTimeFormatter
import net.kyori.adventure.text.event.ClickEvent
import net.kyori.adventure.text.event.HoverEvent
import org.bukkit.entity.Player


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

        val lastDeath = DeathService.findLastDeath(uuid)

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
            spacer("-".repeat(30))

            appendNewInfoPrefixedLine()
            appendCopyable("Letzter Tod von", profile.name, "den Namen")

            val diedAtFormatted = dateTimeFormatter.format(lastDeath.diedAt)
            appendNewInfoPrefixedLine()
            appendCopyable("Zeitpunkt", diedAtFormatted, "den Zeitpunkt")

            val worldName = lastDeath.location.world?.name ?: "#Unbekannt"
            appendNewInfoPrefixedLine()
            appendCopyable("Welt", worldName, "den Weltnamen")

            val cords = "${lastDeath.location.blockX} ${lastDeath.location.blockY} ${lastDeath.location.blockZ}"
            appendNewInfoPrefixedLine()
            appendCopyable("Koordinaten", cords, "die Koordinaten")

            val deathId = lastDeath.deathUuid.toString()
            appendNewInfoPrefixedLine()
            appendCopyable("Todes-ID", deathId, "die Todes-ID")

            val deathReason = lastDeath.reason ?: buildText { spacer("Todesursache unbekannt") }
            appendNewInfoPrefixedLine()
            appendCopyable("Todesursache", deathReason, "die Todesursache")

            appendNewInfoPrefixedLine()
            append(
                buildText { spacer("[Zu Todesort teleportieren]") }
                    .clickEvent(ClickEvent.callback { audience ->
                        val player = audience as? Player ?: return@callback
                        player.teleportAsync(lastDeath.location)
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
                        DeathHistoryGui.itemsGui(player, lastDeath).open()
                    })
                    .hoverEvent(HoverEvent.showText(buildText { info("Klicke um dich zum Todesort zu teleportieren.") }))
            )

            appendNewInfoPrefixedLine()
            spacer("-".repeat(30))
        }
    }
}