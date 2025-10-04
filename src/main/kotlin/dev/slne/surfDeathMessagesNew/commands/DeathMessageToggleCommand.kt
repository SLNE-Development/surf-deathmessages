package dev.slne.surfDeathMessagesNew.commands

import com.github.shynixn.mccoroutine.folia.launch
import dev.jorel.commandapi.kotlindsl.commandAPICommand
import dev.jorel.commandapi.kotlindsl.playerExecutor
import dev.slne.surf.surfapi.core.api.messages.adventure.sendText
import dev.slne.surfDeathMessagesNew.DeathMessageState
import dev.slne.surfDeathMessagesNew.permissions.Permissions
import dev.slne.surfDeathMessagesNew.plugin
import net.kyori.adventure.util.TriState


fun toggleDeathMessageCommands() = commandAPICommand("death-messages") {
    withPermission(Permissions.COMMAND_TOGGLE_DEATH_MESSAGES)

    playerExecutor { player, _ ->

        plugin.launch {
            val state =
                DeathMessageState.setDeathMessagesEnabled(player, !DeathMessageState.hasDeathMessagesEnabled(player))

            player.sendText {
                appendPrefix()

                when (state) {
                    TriState.NOT_SET -> error("Es ist ein Fehler aufgetreten.")
                    TriState.FALSE -> {
                        success("Du hast die Todesnachrichten erfolgreich ")
                        error("deaktiviert")
                        success(".")
                    }

                    TriState.TRUE -> success("Du hast die Todesnachrichten erfolgreich aktiviert.")
                }
            }
        }
    }
}