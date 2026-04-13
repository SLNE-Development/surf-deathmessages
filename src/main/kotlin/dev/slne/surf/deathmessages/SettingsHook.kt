package dev.slne.surf.deathmessages

import dev.slne.surf.settings.api.SurfSettingsApi
import java.util.*

object SettingsHook {
    fun hasDeathMessagesEnabled(playerUuid: UUID): Boolean {
        return SurfSettingsApi.getPlayerSetting(playerUuid, "chat_deathmessages")?.getBoolean()
            ?: true
    }
}