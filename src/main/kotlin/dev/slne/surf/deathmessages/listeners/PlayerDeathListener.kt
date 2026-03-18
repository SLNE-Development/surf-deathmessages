package dev.slne.surf.deathmessages.listeners

import com.github.benmanes.caffeine.cache.Caffeine
import com.github.shynixn.mccoroutine.folia.globalRegionDispatcher
import com.github.shynixn.mccoroutine.folia.launch
import com.google.common.flogger.StackSize
import dev.slne.surf.deathmessages.SettingsHook
import dev.slne.surf.deathmessages.database.Death
import dev.slne.surf.deathmessages.database.service.DeathService
import dev.slne.surf.deathmessages.deathmessages.DeathMessageProvider
import dev.slne.surf.deathmessages.plugin
import dev.slne.surf.surfapi.bukkit.api.extensions.server
import dev.slne.surf.surfapi.core.api.messages.Colors
import dev.slne.surf.surfapi.core.api.messages.adventure.buildText
import dev.slne.surf.surfapi.core.api.util.logger
import kotlinx.coroutines.launch
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.event.HoverEvent
import org.bukkit.GameRules
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.entity.Projectile
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.EntityDamageEvent.DamageCause
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.inventory.ItemStack
import java.time.OffsetDateTime
import java.util.*
import kotlin.time.Duration.Companion.minutes
import kotlin.time.toJavaDuration

object PlayerDeathListener : Listener {
    private val log = logger()

    private val inventorySnapshots = Caffeine.newBuilder()
        .expireAfterWrite(3.minutes.toJavaDuration())
        .maximumSize(10_000)
        .build<UUID, Array<ItemStack?>>()

    @EventHandler(priority = EventPriority.MONITOR)
    fun onDamage(event: EntityDamageEvent) {
        if (event.isCancelled) return
        val player = event.entity as? Player ?: return

        val snapshot = player.inventory.contents.clone()
        inventorySnapshots.put(player.uniqueId, snapshot)
    }

    @EventHandler(priority = EventPriority.HIGH)
    fun onPlayerDeath(event: PlayerDeathEvent) {
        if (event.isCancelled) return
        val player = event.player

        val lastDamageCause: DamageCause? = event.entity.lastDamageCause?.cause
        if (lastDamageCause == null) {
            log.atWarning()
                .withStackTrace(StackSize.MEDIUM)
                .log("${player.name} (${player.uniqueId}) died without a last damage cause. This should not happen!")
            return
        }

        val damageEntity = (player.lastDamageCause as? EntityDamageByEntityEvent)?.damager

        val killerEntity: LivingEntity? = when (damageEntity) {
            is Projectile -> (damageEntity.shooter as? LivingEntity)
            is LivingEntity -> damageEntity
            else -> null
        }

        val originalMessage: Component? = event.deathMessage()
        val deathMessagesEnabled =
            player.world.getGameRuleValue(GameRules.SHOW_DEATH_MESSAGES)!! // Never returns null — api annotation is trash
        event.showDeathMessages = false

        val message = DeathMessageProvider.getDeathMessageComponent(player, lastDamageCause, killerEntity).hoverEvent(
            HoverEvent.showText {
                buildText {
                    append(originalMessage ?: buildText { text("") }).color(Colors.GRAY)
                }
            })

        plugin.launch {
            if (deathMessagesEnabled) {
                launch(plugin.globalRegionDispatcher) {
                    server.onlinePlayers.forEach { onlinePlayer ->
                        if (SettingsHook.hasDeathMessagesEnabled(onlinePlayer.uniqueId)) {
                            onlinePlayer.sendMessage(message)
                        }
                    }
                }
            }

            val death = Death(
                playerUuid = player.uniqueId,
                deathUuid = DeathService.createUnusedDeathUuid(),
                location = event.entity.location,
                diedAt = OffsetDateTime.now(),
                reason = originalMessage,
                isKeepInventory = event.keepInventory,
                deathInventory = inventorySnapshots.getIfPresent(player.uniqueId) ?: emptyArray()
            )

            DeathService.saveDeath(death)
        }
    }
}