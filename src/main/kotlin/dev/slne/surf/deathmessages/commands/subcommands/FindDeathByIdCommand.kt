package dev.slne.surf.deathmessages.commands.subcommands

import dev.jorel.commandapi.arguments.AsyncPlayerProfileArgument
import dev.jorel.commandapi.kotlindsl.argument
import dev.jorel.commandapi.kotlindsl.subcommand
import dev.slne.surf.deathmessages.permissions.Permissions
import dev.slne.surf.surfapi.bukkit.api.command.executors.anyExecutorSuspend
import dev.slne.surf.surfapi.bukkit.api.command.util.awaitAsyncPlayerProfile
import dev.slne.surf.surfapi.core.api.messages.adventure.sendText

fun findDeathByIdCommand() = subcommand("findById") {
    withPermission(Permissions.PLAYER_DEATH_FIND_BY_ID_COMMAND)
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
    }

}