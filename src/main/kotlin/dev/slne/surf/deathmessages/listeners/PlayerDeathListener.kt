package dev.slne.surf.deathmessages.listeners

import com.github.shynixn.mccoroutine.folia.launch
import dev.slne.surf.deathmessages.SettingsHook
import dev.slne.surf.deathmessages.database.Death
import dev.slne.surf.deathmessages.database.service.deathService
import dev.slne.surf.deathmessages.deathmessages.DeathMessageProvider
import dev.slne.surf.deathmessages.plugin
import dev.slne.surf.surfapi.bukkit.api.extensions.server
import dev.slne.surf.surfapi.core.api.messages.Colors
import dev.slne.surf.surfapi.core.api.messages.adventure.buildText
import dev.slne.surf.surfapi.core.api.messages.adventure.sendText
import dev.slne.surf.surfapi.core.api.util.mapAsync
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.event.HoverEvent
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Projectile
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent.DamageCause
import org.bukkit.event.entity.PlayerDeathEvent
import java.time.OffsetDateTime

object PlayerDeathListener : Listener {

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
                if (!SettingsHook.hasDeathMessagesEnabled(player.uniqueId)) return@mapAsync
                player.sendText {
                    append(message)
                }
            }

            val death = Death(
                playerUuid = player.uniqueId,
                deathUuid = deathService.createUnusedDeathUuid(),
                location = event.entity.location,
                diedAt = OffsetDateTime.now(),
                reason = originalMessage,
                lostItems = event.player.inventory.contents.filterNotNull()
            )

            deathService.saveDeath(death)
        }
        event.showDeathMessages = false
    }
}