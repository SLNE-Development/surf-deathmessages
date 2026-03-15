package dev.slne.surf.deathmessages.gui

import dev.slne.surf.deathmessages.appendBullet
import dev.slne.surf.deathmessages.database.Death
import dev.slne.surf.deathmessages.permissions.Permissions
import dev.slne.surf.surfapi.bukkit.api.builder.buildItem
import dev.slne.surf.surfapi.bukkit.api.builder.buildLore
import dev.slne.surf.surfapi.bukkit.api.builder.displayName
import dev.slne.surf.surfapi.bukkit.api.extensions.server
import dev.slne.surf.surfapi.bukkit.api.inventory.framework.titleBuilder
import dev.slne.surf.surfapi.core.api.font.toSmallCaps
import dev.slne.surf.surfapi.core.api.messages.Colors
import dev.slne.surf.surfapi.core.api.messages.adventure.buildText
import dev.slne.surf.surfapi.core.api.util.dateTimeFormatter
import me.devnatan.inventoryframework.View
import me.devnatan.inventoryframework.ViewConfigBuilder
import me.devnatan.inventoryframework.context.RenderContext
import me.devnatan.inventoryframework.context.SlotClickContext
import me.devnatan.inventoryframework.state.State
import net.kyori.adventure.text.format.TextDecoration
import net.kyori.adventure.translation.GlobalTranslator.render
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

@Suppress("UnstableApiUsage")
object DeathHistoryView : View() {

    private val deathState: State<Death> = initialState("death")

    override fun onInit(config: ViewConfigBuilder) {
        config
            .size(6)
            .titleBuilder {
                primary("Todes-Inventar".toSmallCaps())
            }
            .layout(
                "AAAAAPPCI",
                "MMMMMMMMM",
                "MMMMMMMMM",
                "MMMMMMMMM",
                "PPPPPPPPP",
                "HHHHHHHHH"
            )
            .cancelInteractions()
    }

    override fun onFirstRender(render: RenderContext) {
        val death = deathState.get(render)
        val deadPlayerName = server.getOfflinePlayer(death.playerUuid).name ?: death.playerUuid.toString()

        render.updateTitleForPlayer(buildText {
            primary(deadPlayerName)
            appendSpace()
            darkSpacer("(${dateTimeFormatter.format(death.diedAt)})")
        })

        val inv = death.deathInventory

        val armorItems = listOf(39, 38, 37, 36, 40)
        var armorIdx = 0
        render.layoutSlot('A').onRender { slot ->
            val invIndex = armorItems.getOrNull(armorIdx++) ?: return@onRender
            slot.item = inv.getOrNull(invIndex) ?: PLACEHOLDER_ITEM
        }.onClick { click -> handleItemInteraction(click) }

        var mainIdx = 9
        render.layoutSlot('M').onRender { slot ->
            slot.item = inv.getOrNull(mainIdx++) ?: PLACEHOLDER_ITEM
        }.onClick { click -> handleItemInteraction(click) }

        var hotbarIdx = 0
        render.layoutSlot('H').onRender { slot ->
            slot.item = inv.getOrNull(hotbarIdx++) ?: PLACEHOLDER_ITEM
        }.onClick { click -> handleItemInteraction(click) }

        render.layoutSlot('P', PLACEHOLDER_ITEM)

        render.layoutSlot('C', CLOSE_ITEM).onClick { click ->
            click.closeForPlayer()
        }

        render.layoutSlot('I', death.buildInfoItem())
    }

    private fun handleItemInteraction(click: SlotClickContext) {
        val item = click.item ?: return
        if (item.type == Material.GRAY_STAINED_GLASS_PANE) {
            click.isCancelled = true
            return
        }

        if (!click.player.hasPermission(Permissions.PLAYER_DEATH_TAKE_LOST_ITEMS)) {
            click.isCancelled = true
        }
    }

    private val PLACEHOLDER_ITEM = buildItem(Material.GRAY_STAINED_GLASS_PANE) {
        displayName {
            darkSpacer("Platzhalter".toSmallCaps())
        }
    }

    private val CLOSE_ITEM = buildItem(Material.IRON_DOOR) {
        displayName {
            error("Inventar schließen".toSmallCaps())
        }
    }

    private fun Death.buildInfoItem(): ItemStack {
        val playerName = Bukkit.getOfflinePlayer(playerUuid).name ?: playerUuid.toString()
        val loc = location

        return buildItem(Material.REPEATING_COMMAND_BLOCK) {
            displayName(buildText { primary("Todes-Informationen".toSmallCaps()) })
            buildLore {
                line { appendBullet(); info("Spieler:"); appendSpace(); variableValue(playerName) }
                line { appendBullet(); info("Zeitpunkt:"); appendSpace(); variableValue(dateTimeFormatter.format(diedAt)) }
                line {
                    appendBullet(); info("Koordinaten:"); appendSpace()
                    variableValue("${loc.blockX}, ${loc.blockY}, ${loc.blockZ}")
                }
                line {
                    appendBullet(); info("Grund:"); appendSpace()
                    if (reason != null) append(reason).color(Colors.VARIABLE_VALUE) else error("Kein Grund")
                }
                line { appendBullet(); info("KeepInventory:"); appendSpace(); variableValue(if (isKeepInventory) "Ja" else "Nein") }
                line { appendBullet(); info("UUID:"); appendSpace(); variableValue(deathUuid.toString()) }
            }
        }
    }
}