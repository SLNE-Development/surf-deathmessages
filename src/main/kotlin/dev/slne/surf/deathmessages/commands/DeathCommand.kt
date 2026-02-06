package dev.slne.surf.deathmessages.commands

import dev.jorel.commandapi.kotlindsl.commandAPICommand
import dev.slne.surf.deathmessages.commands.subcommands.findDeathByIdCommand
import dev.slne.surf.deathmessages.commands.subcommands.lastDeathCommand
import dev.slne.surf.deathmessages.commands.subcommands.playerDeathHistoryCommand
import dev.slne.surf.deathmessages.permissions.Permissions

fun deathCommand() = commandAPICommand("death") {
    withPermission(Permissions.PLAYER_DEATH_GENERIC_COMMAND)

    withSubcommand(lastDeathCommand())
    withSubcommand(playerDeathHistoryCommand())
    withSubcommand(findDeathByIdCommand())
}