package dev.slne.surf.deathmessages.commands.subcommands

import dev.jorel.commandapi.kotlindsl.getValue
import dev.jorel.commandapi.kotlindsl.subcommand
import dev.jorel.commandapi.kotlindsl.uuidArgument
import dev.slne.surf.deathmessages.commands.sendDeathInfoMessage
import dev.slne.surf.deathmessages.database.service.DeathService
import dev.slne.surf.deathmessages.permissions.Permissions
import dev.slne.surf.surfapi.bukkit.api.command.executors.anyExecutorSuspend
import dev.slne.surf.surfapi.core.api.messages.adventure.sendText
import java.util.*

fun findDeathByIdCommand() = subcommand("findById") {
    withPermission(Permissions.PLAYER_DEATH_FIND_BY_ID_COMMAND)
    uuidArgument("deathId")

    anyExecutorSuspend { sender, args ->
        val deathId: UUID by args
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