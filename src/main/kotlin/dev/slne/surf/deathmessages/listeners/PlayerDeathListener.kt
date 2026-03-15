package dev.slne.surf.deathmessages.listeners

import com.github.benmanes.caffeine.cache.Caffeine
import com.github.shynixn.mccoroutine.folia.launch
import dev.slne.surf.deathmessages.SettingsHook
import dev.slne.surf.deathmessages.database.Death
import dev.slne.surf.deathmessages.database.service.DeathService
import dev.slne.surf.deathmessages.deathmessages.DeathMessageProvider
import dev.slne.surf.deathmessages.plugin
import dev.slne.surf.surfapi.bukkit.api.extensions.server
import dev.slne.surf.surfapi.core.api.messages.Colors
import dev.slne.surf.surfapi.core.api.messages.adventure.buildText
import dev.slne.surf.surfapi.core.api.util.mapAsync
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.event.HoverEvent
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

    private val inventorySnapshots = Caffeine.newBuilder()
        .expireAfterWrite(3.minutes.toJavaDuration())
        .maximumSize(10_000)
        .build<UUID, Array<ItemStack?>>()

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    fun onDamage(event: EntityDamageEvent) {
        val player = event.entity as? Player ?: return

        val snapshot = player.inventory.contents.clone()
        inventorySnapshots.put(player.uniqueId, snapshot)
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    fun onPlayerDeath(event: PlayerDeathEvent) {
        val player = event.player

        val lastDamageCause: DamageCause? = event.entity.lastDamageCause?.cause
        assert(lastDamageCause != null) { error("${player.name} (${player.uniqueId}) died without a last damage cause. This should not happen!") }

        val damageEntity = (player.lastDamageCause as? EntityDamageByEntityEvent)?.damager

        val killerEntity: LivingEntity? = when (damageEntity) {
            is Projectile -> (damageEntity.shooter as? LivingEntity)
            is LivingEntity -> damageEntity
            else -> null
        }

        val originalMessage: Component? = event.deathMessage()

        val message = DeathMessageProvider.getDeathMessageComponent(player, lastDamageCause, killerEntity).hoverEvent(
            HoverEvent.showText {
                buildText {
                    append(originalMessage ?: buildText { text("") }).color(Colors.GRAY)
                }
            })

        plugin.launch {
            server.onlinePlayers.mapAsync { player ->
                if (SettingsHook.hasDeathMessagesEnabled(player.uniqueId)) {
                    player.sendMessage(message)
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
        event.showDeathMessages = false
    }
}