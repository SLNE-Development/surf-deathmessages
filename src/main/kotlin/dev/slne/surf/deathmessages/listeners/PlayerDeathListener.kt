package dev.slne.surf.deathmessages.listeners

import com.github.benmanes.caffeine.cache.Caffeine
import com.github.shynixn.mccoroutine.folia.launch
import com.google.common.flogger.StackSize
import com.sksamuel.aedile.core.expireAfterWrite
import dev.slne.surf.api.core.messages.Colors
import dev.slne.surf.api.core.messages.adventure.buildText
import dev.slne.surf.api.core.util.logger
import dev.slne.surf.api.paper.event.common.death.PlayerDeathMessageEvent
import dev.slne.surf.api.paper.util.forEachPlayer
import dev.slne.surf.deathmessages.database.Death
import dev.slne.surf.deathmessages.database.repository.DeathRepository
import dev.slne.surf.deathmessages.deathmessages.DeathMessageProvider
import dev.slne.surf.deathmessages.hook.SettingsHook
import dev.slne.surf.deathmessages.plugin
import net.kyori.adventure.text.Component
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.entity.Projectile
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.inventory.ItemStack
import java.time.OffsetDateTime
import java.util.*
import kotlin.time.Duration.Companion.minutes

object PlayerDeathListener : Listener {

    private val log = logger()

    private val inventorySnapshots = Caffeine.newBuilder()
        .expireAfterWrite(3.minutes)
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
        saveDeath(
            event.player,
            event.deathMessage(),
            event.keepInventory
        )

        broadcastDeathMessage(event)
    }

    private fun broadcastDeathMessage(event: PlayerDeathEvent) {
        if (!event.showDeathMessages) return

        val player = event.player
        val damageCause = player.lastDamageCause?.cause

        if (damageCause == null) {
            log.atWarning()
                .withStackTrace(StackSize.SMALL)
                .log("Player ${player.name} (${player.uniqueId}) died without a last damage cause. This should not happen!")
            return
        }

        val killerEntity: LivingEntity? =
            when (val damageEntity = event.damageSource.directEntity) {
                is Projectile -> (damageEntity.shooter as? LivingEntity)
                is LivingEntity -> damageEntity
                else -> null
            }

        var message =
            DeathMessageProvider.getDeathMessageComponent(player, damageCause, killerEntity)
                .hoverEvent(
                    buildText {
                        append(event.deathMessage() ?: buildText { text("") }).color(Colors.GRAY)
                    }
                )

        val messageEvent = PlayerDeathMessageEvent(player, message, damageCause)
        if (!messageEvent.call()) {
            event.showDeathMessages = false
            return
        }

        message = messageEvent.message

        event.showDeathMessages = false
        event.deathMessage(message)

        forEachPlayer { player ->
            if (plugin.hasSettingsHook) {
                if (SettingsHook.hasDeathMessagesEnabled(player.uniqueId)) {
                    player.sendMessage(message)
                }
            } else {
                player.sendMessage(message)
            }
        }
    }

    private fun saveDeath(player: Player, originalMessage: Component?, isKeepInventory: Boolean) {
        val uuid = player.uniqueId
        val location = player.location
        val now = OffsetDateTime.now()

        plugin.launch {
            val death = Death(
                playerUuid = uuid,
                deathUuid = DeathRepository.createUnusedDeathUuid(),
                location = location,
                diedAt = now,
                reason = originalMessage,
                isKeepInventory = isKeepInventory,
                deathInventory = inventorySnapshots.getIfPresent(uuid) ?: emptyArray()
            )

            DeathRepository.saveDeath(death)
        }
    }
}