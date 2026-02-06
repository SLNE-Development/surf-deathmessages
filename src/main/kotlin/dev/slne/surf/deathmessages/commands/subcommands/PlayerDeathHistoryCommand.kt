package dev.slne.surf.deathmessages.commands.subcommands

import dev.jorel.commandapi.arguments.AsyncPlayerProfileArgument
import dev.jorel.commandapi.kotlindsl.argument
import dev.jorel.commandapi.kotlindsl.subcommand
import dev.slne.surf.deathmessages.database.Death
import dev.slne.surf.deathmessages.database.service.deathService
import dev.slne.surf.deathmessages.permissions.Permissions
import dev.slne.surf.surfapi.bukkit.api.command.executors.anyExecutorSuspend
import dev.slne.surf.surfapi.bukkit.api.command.util.awaitAsyncPlayerProfile
import dev.slne.surf.surfapi.core.api.font.toSmallCaps
import dev.slne.surf.surfapi.core.api.messages.adventure.buildText
import dev.slne.surf.surfapi.core.api.messages.adventure.sendText
import dev.slne.surf.surfapi.core.api.messages.pagination.Pagination
import dev.slne.surf.surfapi.core.api.util.dateTimeFormatter
import net.kyori.adventure.text.format.TextDecoration

fun playerDeathHistoryCommand() = subcommand("history") {
    withPermission(Permissions.PLAYER_DEATH_LIST_COMMAND)
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

        val deathHistory = deathService.findHistory(uuid)

        if (deathHistory.isEmpty()) {
            sender.sendText {
                appendErrorPrefix()
                error("Es wurden keine Tode für den Spieler")
                appendSpace()
                variableValue(profile.name ?: uuid.toString())
                appendSpace()
                error("gefunden.")
            }
            return@anyExecutorSuspend
        }

        val pagination = Pagination<Death> {
            title {
                primary("Tode von".toSmallCaps(), TextDecoration.BOLD)
                appendSpace()
                variableValue(profile.name ?: uuid.toString())
            }
            rowRenderer { death, _ ->
                listOf(
                    buildText {
                        variableValue(death.deathUuid.toString())
                        spacer(",")
                        appendSpace()
                        variableValue(dateTimeFormatter.format(death.diedAt))
                        spacer(",")
                        appendSpace()
                        variableValue(death.location.world.name)
                        appendSpace()
                        variableValue(death.location.blockX)
                        appendSpace()
                        variableValue(death.location.blockY)
                        appendSpace()
                        appendSpace()
                        variableValue(death.location.blockZ)
                    }//,
//                    buildText {
//                        spacer(">>")
//                        appendSpace()
//                        spacer()
//                    }
                )
            }
        }

        sender.sendText {
            appendNewline()
            append(pagination.renderComponent(deathHistory))
        }
    }
}
