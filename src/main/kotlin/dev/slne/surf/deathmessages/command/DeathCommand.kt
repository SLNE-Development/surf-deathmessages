package dev.slne.surf.deathmessages.command

import dev.jorel.commandapi.kotlindsl.commandAPICommand
import dev.slne.surf.api.core.messages.Colors
import dev.slne.surf.api.core.messages.adventure.buildText
import dev.slne.surf.api.core.messages.adventure.plain
import dev.slne.surf.api.core.messages.adventure.sendText
import dev.slne.surf.api.core.messages.builder.SurfComponentBuilder
import dev.slne.surf.api.core.util.dateTimeFormatter
import dev.slne.surf.deathmessages.command.subcommand.findDeathByIdCommand
import dev.slne.surf.deathmessages.command.subcommand.inventoryCommand
import dev.slne.surf.deathmessages.command.subcommand.lastDeathCommand
import dev.slne.surf.deathmessages.command.subcommand.lookupCommand
import dev.slne.surf.deathmessages.database.Death
import dev.slne.surf.deathmessages.permissions.PermissionList
import net.kyori.adventure.text.Component
import org.bukkit.Location
import org.bukkit.command.CommandSender

private const val UNKNOWN = "#Unbekannt"

fun deathCommand() = commandAPICommand("death") {
    withPermission(PermissionList.PLAYER_DEATH_GENERIC_COMMAND)

    withSubcommand(lastDeathCommand())
    withSubcommand(findDeathByIdCommand())
    withSubcommand(lookupCommand())
    withSubcommand(inventoryCommand())
}

fun CommandSender.sendDeathInfoMessage(death: Death, lastDeath: Boolean = false) = sendText {
    val location = death.location
    val playerName = server.getOfflinePlayer(death.playerUuid).name ?: UNKNOWN

    appendInfoPrefix()
    spacer("-".repeat(30))

    appendNewInfoPrefixedLine()
    appendCopyable(if (lastDeath) "Letzter Tod von" else "Tod von", playerName, "den Namen")

    appendNewInfoPrefixedLine()
    appendCopyable("Zeitpunkt", dateTimeFormatter.format(death.diedAt), "den Zeitpunkt")

    appendNewInfoPrefixedLine()
    appendCopyable("Welt", location.world?.name ?: UNKNOWN, "den Weltnamen")

    appendNewInfoPrefixedLine()
    appendCopyable("Koordinaten", "${location.blockX} ${location.blockY} ${location.blockZ}", "die Koordinaten")

    appendNewInfoPrefixedLine()
    appendCopyable("Todes-ID", death.deathUuid.toString(), "die Todes-ID")

    appendNewInfoPrefixedLine()
    appendCopyable(
        "Todesursache",
        death.reason ?: buildText { spacer("Todesursache unbekannt") },
        "die Todesursache"
    )

    appendNewInfoPrefixedLine()
    appendCommandButton(
        "[Zu Todesort teleportieren]",
        location.toTeleportCommand(),
        "Klicke um dich zum Todesort zu teleportieren."
    )
    appendSpace()
    appendCommandButton(
        "[Inventar ansehen]",
        "/death inventory ${death.deathUuid}",
        "Klicke um das Inventar anzusehen."
    )

    appendNewInfoPrefixedLine()
    spacer("-".repeat(30))
}

private fun SurfComponentBuilder.appendCopyable(label: String, value: String, description: String) =
    appendCopyable(label, buildText { variableValue(value) }, description)

private fun SurfComponentBuilder.appendCopyable(label: String, value: Component, description: String) {
    val rawText = value.plain()

    info("$label: ")
    append(buildText {
        append(value.colorIfAbsent(Colors.VARIABLE_VALUE))

        clickCopiesToClipboard(rawText)
        hoverEvent(buildText {
            info("Klicke, um $description")
            appendSpace()
            variableValue(rawText)
            appendSpace()
            info("zu kopieren.")
        })
    })
}

private fun SurfComponentBuilder.appendCommandButton(label: String, command: String, hint: String) {
    append(buildText {
        spacer(label)

        clickRunsCommand(command)
        hoverEvent(buildText { info(hint) })
    })
}

private fun Location.toTeleportCommand(): String {
    val tp = "/tp $x $y $z"
    val world = world ?: return tp
    return "$tp ${world.key.asString()}"
}
