package dev.slne.surf.deathmessages

import com.github.shynixn.mccoroutine.folia.SuspendingJavaPlugin
import dev.slne.surf.surfapi.bukkit.api.event.register
import dev.slne.surf.deathmessages.listeners.PlayerDeathListener
import org.bukkit.plugin.java.JavaPlugin

class SurfDeathMessagesNew : SuspendingJavaPlugin() {

    override suspend fun onEnableAsync() {
        PlayerDeathListener.register()
    }

    override fun onDisable() {
    }
}

val plugin get() = JavaPlugin.getPlugin(SurfDeathMessagesNew::class.java)