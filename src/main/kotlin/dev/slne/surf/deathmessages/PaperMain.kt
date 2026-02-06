package dev.slne.surf.deathmessages

import com.github.shynixn.mccoroutine.folia.SuspendingJavaPlugin
import dev.slne.surf.deathmessages.commands.deathCommand
import dev.slne.surf.deathmessages.database.databaseLoader
import dev.slne.surf.surfapi.bukkit.api.event.register
import dev.slne.surf.deathmessages.listeners.PlayerDeathListener
import org.bukkit.plugin.java.JavaPlugin

class PaperMain : SuspendingJavaPlugin() {

    override suspend fun onEnableAsync() {
        PlayerDeathListener.register()
        deathCommand()
        databaseLoader.connect(plugin.dataPath)
        databaseLoader.createTables()
    }

    override suspend fun onDisableAsync() {
        databaseLoader.disconnect()
    }
}

val plugin get() = JavaPlugin.getPlugin(PaperMain::class.java)