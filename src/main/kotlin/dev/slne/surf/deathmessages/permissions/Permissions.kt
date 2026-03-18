package dev.slne.surf.deathmessages.permissions

import dev.slne.surf.surfapi.bukkit.api.permission.PermissionRegistry

object Permissions : PermissionRegistry() {
        private const val PREFIX = "surf.deathmessages."
        private const val COMMAND_PREFIX = "$PREFIX.command"
        private const val INTERACTION_PREFIX = "$PREFIX.interaction"

        val PLAYER_DEATH_GENERIC_COMMAND = create("$COMMAND_PREFIX.generic")
        val PLAYER_DEATH_LOOKUP_COMMAND = create("$COMMAND_PREFIX.lookup")

        val PLAYER_DEATH_FIND_BY_ID_COMMAND = create("$COMMAND_PREFIX.findById")

        val PLAYER_DEATH_TAKE_LOST_ITEMS = create("$INTERACTION_PREFIX.takeitems")
}