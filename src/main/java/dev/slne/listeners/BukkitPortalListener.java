package dev.slne.listeners;

import com.destroystokyo.paper.event.player.PlayerTeleportEndGatewayEvent;
import dev.slne.deathmessage.Main;
import dev.slne.deathmessage.message.MessageManager;
import it.unimi.dsi.fastutil.objects.Object2LongMap;
import it.unimi.dsi.fastutil.objects.Object2LongMaps;
import it.unimi.dsi.fastutil.objects.Object2LongOpenHashMap;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.World.Environment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPortalEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.world.PortalCreateEvent;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class BukkitPortalListener implements Listener {

	private final Object2LongMap<UUID> COOL_DOWNS = Object2LongMaps.synchronize(new Object2LongOpenHashMap<>());

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPortalEnter(@NotNull PlayerPortalEvent event) {
		event.setCancelled(checkIfPortalIsDisabled(event.getPlayer(), event.getTo().getWorld().getEnvironment()));
	}

	@EventHandler
	public void onDisconnect(@NotNull PlayerQuitEvent event) {
		COOL_DOWNS.removeLong(event.getPlayer().getUniqueId());
	}

	/**
	 * Returns if the portal is disabled and sends a message to the player if it is.
	 *
	 * @param player    The player involved in this check
	 * @param worldType The world type to check.
	 */
	public boolean checkIfPortalIsDisabled(Player player, @NotNull Environment worldType) {
		if (worldType.equals(Environment.NORMAL)) {
			return false;
		}

		if (!Main.getInstance().isEndEnabled() && (worldType.equals(Environment.THE_END))) {
			sendMessageWithCooldown(player,
					Component.text(
							"Das End ist aktuell deaktiviert! Versuche es zu einem späteren Zeitpunkt erneut.",
							MessageManager.ERROR));
			return true;
		}

		return false;
	}

	/**
	 * Sends a message to the player with a cooldown of 2 seconds.
	 *
	 * @param player    The player involved in this check
	 * @param component The message to send
	 */

	public void sendMessageWithCooldown(Player player, Component component) {
		if (COOL_DOWNS.containsKey(player.getUniqueId())) {
			long secondsLeft = ((COOL_DOWNS.getLong(player.getUniqueId()) / 1000) + 2)
					- (System.currentTimeMillis() / 1000);
			if (secondsLeft > 0) {
				return;
			}
		}

		COOL_DOWNS.put(player.getUniqueId(), System.currentTimeMillis());
		player.sendMessage(MessageManager.prefix().append(component));

		Vector knockbackDirection = player.getLocation().getDirection().multiply(-1).setY(1);
		player.setVelocity(knockbackDirection);
	}

}
