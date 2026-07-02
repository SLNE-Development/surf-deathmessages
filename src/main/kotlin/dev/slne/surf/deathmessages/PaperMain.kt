package dev.slne.surf.deathmessages

import com.github.shynixn.mccoroutine.folia.SuspendingJavaPlugin
import dev.slne.surf.api.core.messages.builder.SurfComponentBuilder
import dev.slne.surf.api.paper.event.register
import dev.slne.surf.api.paper.extensions.pluginManager
import dev.slne.surf.api.paper.inventory.framework.viewFrame
import dev.slne.surf.deathmessages.command.deathCommand
import dev.slne.surf.deathmessages.database.databaseLoader
import dev.slne.surf.deathmessages.gui.DeathHistoryView
import dev.slne.surf.deathmessages.listeners.PlayerDeathListener
import org.bukkit.plugin.java.JavaPlugin

class PaperMain : SuspendingJavaPlugin() {

    override suspend fun onLoadAsync() {
        viewFrame.with(DeathHistoryView)
    }

    override suspend fun onEnableAsync() {
        deathCommand()

        PlayerDeathListener.register()

        databaseLoader.connect(plugin.dataPath)
        databaseLoader.createTables()
    }

    override suspend fun onDisableAsync() {
        databaseLoader.disconnect()
    }

    val hasSettingsHook get() = pluginManager.isPluginEnabled("surf-settings-paper")
}

fun SurfComponentBuilder.appendBullet() {
    spacer("-")
    appendSpace()
}

val plugin get() = JavaPlugin.getPlugin(PaperMain::class.java)