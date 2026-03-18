package dev.slne.surf.deathmessages.gui

import dev.slne.surf.deathmessages.appendBullet
import dev.slne.surf.deathmessages.database.Death
import dev.slne.surf.deathmessages.permissions.Permissions
import dev.slne.surf.surfapi.bukkit.api.builder.buildItem
import dev.slne.surf.surfapi.bukkit.api.builder.buildLore
import dev.slne.surf.surfapi.bukkit.api.builder.displayName
import dev.slne.surf.surfapi.bukkit.api.extensions.server
import dev.slne.surf.surfapi.core.api.font.toSmallCaps
import dev.slne.surf.surfapi.core.api.messages.Colors
import dev.slne.surf.surfapi.core.api.messages.adventure.buildText
import dev.slne.surf.surfapi.core.api.util.dateTimeFormatter
import me.devnatan.inventoryframework.View
import me.devnatan.inventoryframework.ViewConfigBuilder
import me.devnatan.inventoryframework.context.OpenContext
import me.devnatan.inventoryframework.context.RenderContext
import me.devnatan.inventoryframework.state.State
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

object DeathHistoryView : View() {
    private val deathState: State<Death> = initialState("death")

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

    /**
     * Layout Overview:
     *
     * A = Armor & Offhand slots Order: [39, 38, 37, 36, 40]
     * M = Main inventory (slots 9–35)
     * H = Hotbar (slots 0–8)
     * P = Placeholder (non-interactive)
     * C = Close button (closes the inventory)
     * I = Info item (death information)
     * Total size: 6 rows (6 × 9 = 54 slots)
     **/
    override fun onInit(config: ViewConfigBuilder) {
        config
            .size(6)
            .layout(
                "AAAAAPPCI",
                "MMMMMMMMM",
                "MMMMMMMMM",
                "MMMMMMMMM",
                "PPPPPPPPP",
                "HHHHHHHHH"
            )
    }

    override fun onOpen(open: OpenContext) {
        val death = deathState.get(open)
        val deadPlayerName = server.getOfflinePlayer(death.playerUuid).name ?: death.playerUuid.toString()
        val dateStr = dateTimeFormatter.format(death.diedAt)

        open.modifyConfig().title(buildText {
            primary(deadPlayerName)
            appendSpace()
            darkSpacer("($dateStr)")
        })
    }

    override fun onFirstRender(render: RenderContext) {
        val death = deathState.get(render)
        val inv = death.deathInventory

        val armorItems = listOf(39, 38, 37, 36, 40)
        var armorIdx = 0
        render.layoutSlot('A').onRender { slot ->
            val invIndex = armorItems.getOrNull(armorIdx++) ?: return@onRender
            slot.item = inv.getOrNull(invIndex)
        }.onClick { click ->
           click.isCancelled = !click.player.hasPermission(Permissions.PLAYER_DEATH_TAKE_LOST_ITEMS)
        }

        var mainIdx = 9
        render.layoutSlot('M').onRender { slot ->
            slot.item = inv.getOrNull(mainIdx++)
        }.onClick { click ->
            click.isCancelled = !click.player.hasPermission(Permissions.PLAYER_DEATH_TAKE_LOST_ITEMS)
        }

        var hotbarIdx = 0
        render.layoutSlot('H').onRender { slot ->
            slot.item = inv.getOrNull(hotbarIdx++)
        }.onClick { click ->
            click.isCancelled = !click.player.hasPermission(Permissions.PLAYER_DEATH_TAKE_LOST_ITEMS)
        }

        render.layoutSlot('P', PLACEHOLDER_ITEM).cancelOnClick()

        render.layoutSlot('C', CLOSE_ITEM).onClick { click ->
            click.isCancelled = true
            click.closeForPlayer()
        }

        render.layoutSlot('I', death.buildInfoItem()).cancelOnClick()
    }

    private fun Death.buildInfoItem(): ItemStack {
        val playerName = Bukkit.getOfflinePlayer(playerUuid).name ?: playerUuid.toString()
        val loc = location

        return buildItem(Material.REPEATING_COMMAND_BLOCK) {
            displayName(
                buildText {
                    primary("Todes-Informationen".toSmallCaps())
                }
            )

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
                    info("Koordinaten:")
                    appendSpace()
                    variableValue("${loc.blockX}, ${loc.blockY}, ${loc.blockZ}")
                }

                line {
                    appendBullet()
                    info("Grund:")
                    appendSpace()
                    if (reason != null)
                        append(reason).color(Colors.VARIABLE_VALUE)
                    else
                        error("Kein Grund angegeben")
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