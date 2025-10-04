package dev.slne.surfDeathMessagesNew.commands

import dev.jorel.commandapi.kotlindsl.commandAPICommand
import dev.jorel.commandapi.kotlindsl.playerExecutor
import dev.slne.surf.surfapi.core.api.messages.adventure.sendText
import dev.slne.surfDeathMessagesNew.permissions.Permissions

fun toggleDeathMessageCommands() = commandAPICommand("death-messages"){
    withPermission(Permissions.COMMAND_TOGGLE_DEATH_MESSAGES)

    playerExecutor { player, _ ->

        player.sendText {

            //TODO: Implement command and save state in db
            appendPrefix()
            success("Du hast die Todesnachrichten erfolgreich umgeschaltet.")
        }
    }
}