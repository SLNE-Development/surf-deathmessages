package dev.slne.surf.deathmessages.commands.subcommands

import dev.jorel.commandapi.arguments.UUIDArgument
import dev.jorel.commandapi.kotlindsl.argument
import dev.jorel.commandapi.kotlindsl.subcommand
import dev.slne.surf.deathmessages.commands.sendDeathInfoMessage
import dev.slne.surf.deathmessages.database.service.DeathService
import dev.slne.surf.deathmessages.permissions.Permissions
import dev.slne.surf.surfapi.bukkit.api.command.executors.anyExecutorSuspend
import dev.slne.surf.surfapi.core.api.messages.adventure.sendText
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

        sender.sendDeathInfoMessage(death)
    }
}