package dev.slne.surf.deathmessages.gui

import com.github.stefvanschie.inventoryframework.gui.GuiItem
import com.github.stefvanschie.inventoryframework.pane.StaticPane
import dev.slne.surf.deathmessages.database.Death
import dev.slne.surf.deathmessages.permissions.Permissions
import dev.slne.surf.surfapi.bukkit.api.builder.buildItem
import dev.slne.surf.surfapi.bukkit.api.builder.displayName
import dev.slne.surf.surfapi.bukkit.api.inventory.dsl.playerMenu
import dev.slne.surf.surfapi.core.api.messages.adventure.buildText
import dev.slne.surf.surfapi.core.api.util.dateTimeFormatter
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

object DeathHistoryGui {

    private val PLACEHOLDER_ITEM: ItemStack = buildItem(Material.RED_STAINED_GLASS_PANE) {
        displayName(buildText {
            spacer("")
        })
    }

    fun itemsGui(
        player: Player,
        death: Death
    ) = playerMenu(buildText {
        val deadPlayerName = Bukkit.getOfflinePlayer(death.playerUuid).name ?: death.playerUuid.toString()
        primary(deadPlayerName)
        appendSpace()
        darkSpacer("(${dateTimeFormatter.format(death.diedAt)})")
    }, player, 6) {

        val pane = StaticPane(0, 0, 9, 6)
        val items = death.lostItems

        var index = 0

        for (row in 0 until 6) {
            for (col in 0 until 9) {
                val item = items.getOrNull(index)
                pane.addItem(guiItem(item), col, row)
                index++
            }
        }

        addPane(pane)

        //TODO: Sort items like player inventory (armor, offhand, main hand, then rest)
    }

    private fun guiItem(item: ItemStack?): GuiItem {
        val display = item ?: PLACEHOLDER_ITEM

        return GuiItem(display) { clickEvent ->
            val player = clickEvent.whoClicked as? Player ?: return@GuiItem

            if (item == null || item == PLACEHOLDER_ITEM) {
                clickEvent.isCancelled = true
                return@GuiItem
            }

            if (!player.hasPermission(Permissions.PLAYER_DEATH_TAKE_LOST_ITEMS)) {
                clickEvent.isCancelled = true
                return@GuiItem
            }

            clickEvent.isCancelled = false
        }
    }
}