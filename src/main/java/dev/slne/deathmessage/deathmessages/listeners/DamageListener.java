package dev.slne.deathmessage.deathmessages.listeners;

import dev.slne.deathmessage.deathmessages.events.ExtendedPlayerDeathEvent;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

public class DamageListener implements Listener {

    /**
     * Handles the death of a player
     *
     * @param event the event
     */
    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        event.getEntity();
        Player player = event.getPlayer();

        EntityDamageEvent lastEntityDamageEvent = player.getLastDamageCause();

        assert lastEntityDamageEvent != null : "Player %s died without a last damage cause. This should not happen!".formatted(player.getName());

        Entity killerEntity = event.getPlayer().getKiller();
        Block killerBlock = null;

        if (killerEntity == null) {
            if (lastEntityDamageEvent instanceof EntityDamageByEntityEvent entityDamageByEntityEvent) {
                killerEntity = entityDamageByEntityEvent.getDamager();
            } else if (lastEntityDamageEvent instanceof EntityDamageByBlockEvent entityDamageByBlockEvent) {
                killerBlock = entityDamageByBlockEvent.getDamager();
            }
        }

        ExtendedPlayerDeathEvent userDeathEvent = new ExtendedPlayerDeathEvent(player, lastEntityDamageEvent,
                killerEntity, killerBlock, event.deathMessage());
        Bukkit.getPluginManager().callEvent(userDeathEvent);
        if (userDeathEvent.isCancelled()) {
            event.setCancelled(true);
        }

        event.deathMessage(userDeathEvent.getDeathMessage());
    }
}