package dev.slne.surfDeathMessagesNew.listeners

import dev.slne.surf.surfapi.core.api.messages.adventure.buildText
import dev.slne.surf.surfapi.core.api.messages.adventure.sendText
import dev.slne.surfDeathMessagesNew.deathmessages.DeathMessageProvider
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.event.HoverEvent
import org.bukkit.Bukkit
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Projectile
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent.DamageCause
import org.bukkit.event.entity.PlayerDeathEvent

class PlayerDeathListener : Listener {

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

        val originalMessage: Component = event.deathMessage() ?: buildText { text("") }

        val message = DeathMessageProvider.getDeathMessageComponent(player, lastDamageCause, killerEntity).hoverEvent(
            HoverEvent.showText {
                buildText {
                    append(originalMessage)
                }
            })

        Bukkit.getOnlinePlayers().forEach { onlinePlayer ->
            onlinePlayer.sendText {
                //TODO: check if player has death messages enabled
                append(message)
            }
        }
    }
}