package dev.slne.surfDeathMessagesNew

import dev.slne.surf.settings.api.surfSettingsApi
import org.bukkit.Bukkit
import java.util.*

object SettingsHook {
    fun isEnabled() = Bukkit.getPluginManager().isPluginEnabled("surf-settings-paper")

    fun hasDeathMessagesEnabled(playerUuid: UUID): Boolean {
        return if (isEnabled()) {
            surfSettingsApi.getPlayerSetting(playerUuid, "chat_deathmessages")?.getBoolean()
                ?: true
        } else {
            true
        }
    }
}