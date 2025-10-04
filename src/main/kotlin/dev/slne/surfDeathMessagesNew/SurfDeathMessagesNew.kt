package dev.slne.surfDeathMessagesNew

import com.github.shynixn.mccoroutine.folia.SuspendingJavaPlugin
import dev.slne.surf.surfapi.bukkit.api.event.register
import dev.slne.surfDeathMessagesNew.commands.toggleDeathMessageCommands
import dev.slne.surfDeathMessagesNew.listeners.PlayerDeathListener

class SurfDeathMessagesNew : SuspendingJavaPlugin() {

    override suspend fun onEnableAsync() {
        PlayerDeathListener().register()
        toggleDeathMessageCommands()
    }

    override fun onDisable() {
    }
}
