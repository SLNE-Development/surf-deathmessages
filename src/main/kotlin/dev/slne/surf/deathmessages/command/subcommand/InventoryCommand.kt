package dev.slne.surf.deathmessages.command.subcommand

import com.github.shynixn.mccoroutine.folia.entityDispatcher
import dev.jorel.commandapi.arguments.UUIDArgument
import dev.jorel.commandapi.kotlindsl.argument
import dev.jorel.commandapi.kotlindsl.subcommand
import dev.slne.surf.api.core.messages.adventure.sendText
import dev.slne.surf.api.paper.command.executors.playerExecutorSuspend
import dev.slne.surf.api.paper.inventory.framework.viewFrame
import dev.slne.surf.deathmessages.database.repository.DeathRepository
import dev.slne.surf.deathmessages.gui.DeathHistoryView
import dev.slne.surf.deathmessages.permissions.PermissionList
import dev.slne.surf.deathmessages.plugin
import kotlinx.coroutines.withContext
import java.util.*

fun inventoryCommand() = subcommand("inventory") {
    withPermission(PermissionList.PLAYER_DEATH_FIND_BY_ID_COMMAND)
    argument(UUIDArgument("deathId"))

    playerExecutorSuspend { player, args ->
        val deathId = args["deathId"] as UUID
        val death = DeathRepository.findDeathByUuid(deathId)

        if (death == null) {
            player.sendText {
                appendErrorPrefix()
                error("Der angegebene Tod mit der ID")
                appendSpace()
                variableValue(deathId.toString())
                appendSpace()
                error("konnte nicht gefunden werden.")
            }
            return@playerExecutorSuspend
        }

        withContext(plugin.entityDispatcher(player)) {
            viewFrame.open(DeathHistoryView::class.java, player, mapOf("death" to death))
        }
    }
}
