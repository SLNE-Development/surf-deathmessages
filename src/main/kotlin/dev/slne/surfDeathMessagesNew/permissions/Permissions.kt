package dev.slne.surfDeathMessagesNew.permissions

import dev.slne.surf.surfapi.bukkit.api.permission.PermissionRegistry

object Permissions : PermissionRegistry() {

    private const val PREFIX = "surf.death-messages"
    private const val COMMAND_PREFIX = "$PREFIX.command"

    val COMMAND_TOGGLE_DEATH_MESSAGES = create("$COMMAND_PREFIX.toggle-death-messages")
}