package dev.slne.surf.deathmessages

import com.github.shynixn.mccoroutine.folia.SuspendingJavaPlugin
import dev.slne.surf.deathmessages.commands.deathCommand
import dev.slne.surf.deathmessages.database.databaseLoader
import dev.slne.surf.deathmessages.gui.DeathHistoryView
import dev.slne.surf.deathmessages.listeners.PlayerDeathListener
import dev.slne.surf.surfapi.bukkit.api.event.register
import dev.slne.surf.surfapi.bukkit.api.inventory.framework.viewFrame
import dev.slne.surf.surfapi.core.api.messages.builder.SurfComponentBuilder
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
}

fun SurfComponentBuilder.appendBullet() {
    spacer("-")
    appendSpace()
}

val plugin get() = JavaPlugin.getPlugin(PaperMain::class.java)