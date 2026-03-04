package dev.slne.surf.deathmessages

import com.github.shynixn.mccoroutine.folia.SuspendingJavaPlugin
import dev.slne.surf.deathmessages.commands.deathCommand
import dev.slne.surf.deathmessages.database.databaseLoader
import dev.slne.surf.deathmessages.listeners.PlayerDeathListener
import dev.slne.surf.surfapi.bukkit.api.event.register
import dev.slne.surf.surfapi.core.api.messages.Colors
import dev.slne.surf.surfapi.core.api.messages.adventure.buildText
import dev.slne.surf.surfapi.core.api.messages.builder.SurfComponentBuilder
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.event.ClickEvent
import net.kyori.adventure.text.event.HoverEvent
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer
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

fun SurfComponentBuilder.appendCopyable(
    label: String,
    value: Any?,
    description: String
) {
    val displayValue = value?.toString() ?: "#Unbekannt"

    val rawText = if (value is Component) {
        PlainTextComponentSerializer.plainText().serialize(value)
    } else {
        displayValue
    }

    info("$label: ")
    append(
        buildText {
            if (value is Component) append(value.colorIfAbsent(Colors.VARIABLE_VALUE)) else variableValue(displayValue)
        }
            .clickEvent(ClickEvent.copyToClipboard(rawText))
            .hoverEvent(HoverEvent.showText(buildText {
                info("Klicke, um $description")
                appendSpace()
                variableValue(rawText)
                appendSpace()
                info("zu kopieren.")
            }))
    )
}

val plugin get() = JavaPlugin.getPlugin(PaperMain::class.java)