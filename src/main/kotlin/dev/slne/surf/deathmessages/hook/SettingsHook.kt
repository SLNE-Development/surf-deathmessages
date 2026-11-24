package dev.slne.surf.deathmessages.hook

import dev.slne.surf.player.api.settings.SurfSettingsApi
import dev.slne.surf.player.api.settings.setting.SettingKeys
import java.util.*

object SettingsHook {
    fun hasDeathMessagesEnabled(playerUuid: UUID) =
        SurfSettingsApi.getSettingValue(playerUuid, SettingKeys.CHAT_DEATH_MESSAGES)
}