package dev.slne.surfDeathMessagesNew

import dev.slne.surf.cloud.api.common.player.toCloudPlayer
import dev.slne.surf.surfapi.core.api.messages.adventure.key
import net.kyori.adventure.util.TriState
import org.bukkit.entity.Player

object DeathMessageState {
    val disabled = key("death-messages", "disabled")

    suspend fun hasDeathMessagesEnabled(player: Player): Boolean {
        val cloudPlayer = player.toCloudPlayer() ?: return true
        return cloudPlayer.withPersistentData {
            getBoolean(disabled)?.not() != false
        }
    }

    suspend fun setDeathMessagesEnabled(player: Player, enabled: Boolean): TriState {
        val cloudPlayer = player.toCloudPlayer() ?: return TriState.NOT_SET
        cloudPlayer.withPersistentData {
            if (enabled) {
                remove(disabled)
            } else {
                setBoolean(disabled, true)
            }
        }

        return TriState.byBoolean(enabled)
    }
}