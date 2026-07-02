package dev.slne.surf.deathmessages.hook

import dev.slne.surf.settings.api.SurfSettingsApi
import dev.slne.surf.settings.api.setting.SettingKeys
import java.util.*

object SettingsHook {
    fun hasDeathMessagesEnabled(playerUuid: UUID) =
        SurfSettingsApi.getSettingValue(playerUuid, SettingKeys.CHAT_DEATH_MESSAGES)
}