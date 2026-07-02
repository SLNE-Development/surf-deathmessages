package dev.slne.surf.deathmessages.command.subcommand

import dev.jorel.commandapi.kotlindsl.subcommand
import dev.slne.surf.api.core.command.args.awaiting
import dev.slne.surf.api.core.messages.adventure.sendText
import dev.slne.surf.api.paper.command.executors.anyExecutorSuspend
import dev.slne.surf.core.api.common.player.SurfPlayer
import dev.slne.surf.core.api.paper.command.argument.surfOfflinePlayerArgument
import dev.slne.surf.deathmessages.command.sendDeathInfoMessage
import dev.slne.surf.deathmessages.database.repository.DeathRepository
import dev.slne.surf.deathmessages.permissions.PermissionList


fun lastDeathCommand() = subcommand("last") {
    withPermission(PermissionList.PLAYER_DEATH_LOOKUP_COMMAND)
    surfOfflinePlayerArgument("player")

    anyExecutorSuspend { sender, args ->
        val surfPlayer = args.awaiting<SurfPlayer?>("player")

        if (surfPlayer == null) {
            sender.sendText {
                appendErrorPrefix()
                error("Der angegebene Spieler konnte nicht gefunden werden.")
            }
            return@anyExecutorSuspend
        }

        val lastDeath = DeathRepository.findLastDeath(surfPlayer.uuid)

        if (lastDeath == null) {
            sender.sendText {
                appendErrorPrefix()
                error("Es wurde kein Tod des Spielers gefunden.")
            }
            return@anyExecutorSuspend
        }

        sender.sendDeathInfoMessage(lastDeath, true)
    }
}