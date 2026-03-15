package dev.slne.surf.deathmessages.gui

import com.github.stefvanschie.inventoryframework.gui.GuiItem
import com.github.stefvanschie.inventoryframework.pane.StaticPane
import dev.slne.surf.deathmessages.appendBullet
import dev.slne.surf.deathmessages.database.Death
import dev.slne.surf.deathmessages.permissions.Permissions
import dev.slne.surf.surfapi.bukkit.api.builder.buildItem
import dev.slne.surf.surfapi.bukkit.api.builder.buildLore
import dev.slne.surf.surfapi.bukkit.api.builder.displayName
import dev.slne.surf.surfapi.bukkit.api.inventory.dsl.playerMenu
import dev.slne.surf.surfapi.core.api.font.toSmallCaps
import dev.slne.surf.surfapi.core.api.messages.Colors
import dev.slne.surf.surfapi.core.api.messages.adventure.buildText
import dev.slne.surf.surfapi.core.api.util.dateTimeFormatter
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

object DeathHistoryGui { //TODO: rework

    private val PLACEHOLDER_ITEM = buildItem(Material.GRAY_STAINED_GLASS_PANE) {
        displayName(buildText { darkSpacer("Platzhalter".toSmallCaps()) })
    }

    private val CLOSE_ITEM = buildItem(Material.IRON_DOOR) {
        displayName(buildText { error("Inventar schließen".toSmallCaps()) })
    }

    fun itemsGui(player: Player, death: Death) = playerMenu(buildText {
        val deadPlayerName = Bukkit.getOfflinePlayer(death.playerUuid).name ?: death.playerUuid.toString()
        primary(deadPlayerName)
        appendSpace()
        darkSpacer("(${dateTimeFormatter.format(death.diedAt)})")
    }, player, 6) {
        val pane = StaticPane(0, 0, 9, 6)

        pane.fillWith(PLACEHOLDER_ITEM)

        val armor = listOf(
            death.deathInventory.getOrNull(39),
            death.deathInventory.getOrNull(38),
            death.deathInventory.getOrNull(37),
            death.deathInventory.getOrNull(36),
            death.deathInventory.getOrNull(40)
        )
        armor.forEachIndexed { i, item -> pane.addItem(createGuiItem(item), i, 0) }

        pane.addItem(GuiItem(CLOSE_ITEM) { event ->
            event.isCancelled = true
            event.whoClicked.closeInventory()
        }, 7, 0)

        pane.addItem(createGuiItem(death.buildInfoItem(), isSystemItem = true), 8, 0)

        val mainInv = death.deathInventory.slice(9..35)
        var mainIdx = 0
        for (row in 1..3) {
            for (col in 0..8) {
                if (mainIdx < mainInv.size) {
                    pane.addItem(createGuiItem(mainInv[mainIdx]), col, row)
                    mainIdx++
                }
            }
        }

        val hotbar = death.deathInventory.slice(0..8)
        hotbar.forEachIndexed { i, item -> pane.addItem(createGuiItem(item), i, 5) }

        addPane(pane)
    }

    private fun createGuiItem(item: ItemStack?, isSystemItem: Boolean = false): GuiItem {
        val display = item ?: PLACEHOLDER_ITEM

        return GuiItem(display) { event ->
            val clicker = event.whoClicked as? Player ?: return@GuiItem

            if (isSystemItem) {
                event.isCancelled = true
                return@GuiItem
            }

            if (clicker.hasPermission(Permissions.PLAYER_DEATH_TAKE_LOST_ITEMS)) {
                event.isCancelled = false
            } else {
                event.isCancelled = true
            }
        }
    }

    private fun Death.buildInfoItem(): ItemStack {
        val playerName = Bukkit.getOfflinePlayer(playerUuid).name ?: playerUuid.toString()
        val loc = location

        return buildItem(Material.REPEATING_COMMAND_BLOCK) {
            displayName(buildText { primary("Todes-Informationen".toSmallCaps()) })

            buildLore {
                line {
                    appendBullet()
                    info("Spieler:")
                    appendSpace()
                    variableValue(playerName)
                }

                line {
                    appendBullet()
                    info("Zeitpunkt:")
                    appendSpace()
                    variableValue(dateTimeFormatter.format(diedAt))
                }

                line {
                    appendBullet()
                    info("Ort:")
                    appendSpace()
                    variableValue("${loc.blockX}, ${loc.blockY}, ${loc.blockZ}")
                    appendSpace()
                    spacer("(${loc.world?.name ?: "Unbekannt"})")
                }

                line {
                    appendBullet()
                    info("Grund:")
                    appendSpace()
                    if (reason != null) {
                        append(reason).color(Colors.VARIABLE_VALUE)
                    } else {
                        error("Kein Grund angegeben")
                    }
                }

                line {
                    appendBullet()
                    info("KeepInventory:")
                    appendSpace()
                    variableValue(if (isKeepInventory) "Ja" else "Nein")
                }

                line {
                    appendBullet()
                    info("UUID:")
                    appendSpace()
                    variableValue(deathUuid.toString())
                }
            }
        }
    }
}