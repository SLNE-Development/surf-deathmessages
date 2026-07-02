package dev.slne.surf.deathmessages.command.subcommand

import dev.jorel.commandapi.arguments.UUIDArgument
import dev.jorel.commandapi.kotlindsl.argument
import dev.jorel.commandapi.kotlindsl.subcommand
import dev.slne.surf.api.core.messages.adventure.sendText
import dev.slne.surf.api.paper.command.executors.anyExecutorSuspend
import dev.slne.surf.deathmessages.command.sendDeathInfoMessage
import dev.slne.surf.deathmessages.database.repository.DeathRepository
import dev.slne.surf.deathmessages.permissions.PermissionList
import java.util.*

fun findDeathByIdCommand() = subcommand("findById") {
    withPermission(PermissionList.PLAYER_DEATH_FIND_BY_ID_COMMAND)
    argument(UUIDArgument("deathId"))

    anyExecutorSuspend { sender, args ->
        val deathId = args["deathId"] as UUID
        val death = DeathRepository.findDeathByUuid(deathId)

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