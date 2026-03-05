package dev.slne.surf.deathmessages

import com.github.shynixn.mccoroutine.folia.SuspendingJavaPlugin
import dev.slne.surf.deathmessages.commands.deathCommand
import dev.slne.surf.deathmessages.database.Death
import dev.slne.surf.deathmessages.database.databaseLoader
import dev.slne.surf.deathmessages.gui.DeathHistoryGui
import dev.slne.surf.deathmessages.listeners.PlayerDeathListener
import dev.slne.surf.surfapi.bukkit.api.event.register
import dev.slne.surf.surfapi.bukkit.api.extensions.server
import dev.slne.surf.surfapi.core.api.messages.Colors
import dev.slne.surf.surfapi.core.api.messages.adventure.buildText
import dev.slne.surf.surfapi.core.api.messages.adventure.sendText
import dev.slne.surf.surfapi.core.api.messages.builder.SurfComponentBuilder
import dev.slne.surf.surfapi.core.api.util.dateTimeFormatter
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.event.ClickEvent
import net.kyori.adventure.text.event.HoverEvent
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
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