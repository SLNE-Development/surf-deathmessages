package dev.slne.surf.deathmessages.commands

import dev.jorel.commandapi.kotlindsl.commandAPICommand
import dev.slne.surf.deathmessages.commands.subcommands.findDeathByIdCommand
import dev.slne.surf.deathmessages.commands.subcommands.lastDeathCommand
import dev.slne.surf.deathmessages.commands.subcommands.lookupCommand
import dev.slne.surf.deathmessages.database.Death
import dev.slne.surf.deathmessages.gui.DeathHistoryView
import dev.slne.surf.deathmessages.permissions.Permissions
import dev.slne.surf.surfapi.bukkit.api.inventory.framework.viewFrame
import dev.slne.surf.surfapi.core.api.messages.Colors
import dev.slne.surf.surfapi.core.api.messages.adventure.buildText
import dev.slne.surf.surfapi.core.api.messages.adventure.sendText
import dev.slne.surf.surfapi.core.api.messages.builder.SurfComponentBuilder
import dev.slne.surf.surfapi.core.api.util.dateTimeFormatter
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.event.ClickCallback
import net.kyori.adventure.text.event.ClickEvent
import net.kyori.adventure.text.event.HoverEvent
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

fun deathCommand() = commandAPICommand("death") {
    withPermission(Permissions.PLAYER_DEATH_GENERIC_COMMAND)

    withSubcommand(lastDeathCommand())
    withSubcommand(findDeathByIdCommand())
    withSubcommand(lookupCommand())
}

private fun SurfComponentBuilder.appendCopyable(
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

fun CommandSender.sendDeathInfoMessage(death: Death, lastDeath: Boolean = false) = sendText {
    val playerName = server.getOfflinePlayer(death.playerUuid).name ?: "#Unbekannt"

    appendInfoPrefix()
    spacer("-".repeat(30))

    appendNewInfoPrefixedLine()
    if (lastDeath) {
        appendCopyable("Letzter Tod von", playerName, "den Namen")
    } else {
        appendCopyable("Tod von", playerName, "den Namen")
    }

    val diedAtFormatted = dateTimeFormatter.format(death.diedAt)
    appendNewInfoPrefixedLine()
    appendCopyable("Zeitpunkt", diedAtFormatted, "den Zeitpunkt")

    val worldName = death.location.world?.name ?: "#Unbekannt"
    appendNewInfoPrefixedLine()
    appendCopyable("Welt", worldName, "den Weltnamen")

    val cords = "${death.location.blockX} ${death.location.blockY} ${death.location.blockZ}"
    appendNewInfoPrefixedLine()
    appendCopyable("Koordinaten", cords, "die Koordinaten")

    val deathId = death.deathUuid.toString()
    appendNewInfoPrefixedLine()
    appendCopyable("Todes-ID", deathId, "die Todes-ID")

    val deathReason = death.reason ?: buildText { spacer("Todesursache unbekannt") }
    appendNewInfoPrefixedLine()
    appendCopyable("Todesursache", deathReason, "die Todesursache")

    appendNewInfoPrefixedLine()
    append {
        spacer("[Zu Todesort teleportieren]")
        clickEvent(ClickEvent.callback(ClickCallback.widen({ player ->
            player.teleportAsync(death.location)
            player.sendText {
                appendSuccessPrefix()
                success("Du wurdest zum Todesort teleportiert.")
            }
        }, Player::class.java)))
        hoverEvent(buildText { info("Klicke um dich zum Todesort zu teleportieren.") })
    }
    appendSpace()
    append {
        spacer("[Inventar ansehen]")
        clickEvent(ClickEvent.callback(ClickCallback.widen({ player ->
            viewFrame.open(
                DeathHistoryView::class.java,
                player,
                mapOf("death" to death)
            )
        }, Player::class.java)))
        hoverEvent(buildText { info("Klicke um dich zum Todesort zu teleportieren.") })
    }

    appendNewInfoPrefixedLine()
    spacer("-".repeat(30))
}