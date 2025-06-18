package dev.slne.deathmessage.deathmessages.listeners;

import dev.slne.deathmessage.deathmessages.DeathMessage;
import dev.slne.deathmessage.deathmessages.DeathMessageManager;
import dev.slne.deathmessage.deathmessages.events.ExtendedPlayerDeathEvent;
import dev.slne.deathmessage.message.MessageManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent.Builder;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

public class DeathListener implements Listener {
	private final DeathMessageManager deathMessageManager;

	public DeathListener(DeathMessageManager deathMessageManager) {
		this.deathMessageManager = deathMessageManager;
	}

	@EventHandler
	public void onDeath(ExtendedPlayerDeathEvent event) {
		Player player = event.getPlayer();
		Location deathLocation = player.getLocation();

		Builder prefixBuilder = Component.text();
		prefixBuilder.append(Component.text("[", NamedTextColor.DARK_GRAY));
		prefixBuilder.append(Component.text("☠", NamedTextColor.RED));
		prefixBuilder.append(Component.text("] ", NamedTextColor.DARK_GRAY));

		DamageCause damageCause = null;
		if (player.getLastDamageCause() != null) {
			damageCause = player.getLastDamageCause().getCause();
		}

		Entity killer = event.getKillerEntity();
		Block killerBlock = event.getKillerBlock();

		DeathMessage deathMessage = deathMessageManager.getDeathMessageForType(damageCause);
		Component deathMessageComponent = deathMessage.getDeathMessage(player, killer,
				killerBlock);

		if (deathMessageComponent == null) {
			deathMessage = deathMessageManager.getUnknownDeathMessage();
			deathMessageComponent = deathMessage.getDeathMessage(player, killer, killerBlock);
		}

		Builder locMessage = Component.text();
		locMessage.append(prefixBuilder);
		locMessage.append(Component.text("Spieler: ", MessageManager.SPACER));
		locMessage.append(Component.text(player.getName(), MessageManager.VARIABLE_VALUE));
		locMessage.append(Component.text(" Welt: ", MessageManager.SPACER));
		locMessage.append(Component.text(deathLocation.getWorld().getName(), MessageManager.VARIABLE_VALUE));
		locMessage.append(Component.text(" Position: ", MessageManager.SPACER));
		locMessage.append(Component.text(deathLocation.getBlockX(), MessageManager.VARIABLE_VALUE));
		locMessage.append(Component.text(", ", MessageManager.SPACER));
		locMessage.append(Component.text(deathLocation.getBlockY(), MessageManager.VARIABLE_VALUE));
		locMessage.append(Component.text(", ", MessageManager.SPACER));
		locMessage.append(Component.text(deathLocation.getBlockZ(), MessageManager.VARIABLE_VALUE));
		locMessage.append(Component.text(" Ping: ", MessageManager.SPACER));
		locMessage.append(Component.text(player.getPing(), MessageManager.VARIABLE_VALUE));
		locMessage.append(Component.text(" Levels: ", MessageManager.SPACER));
		locMessage.append(Component.text(player.getLevel(), MessageManager.VARIABLE_VALUE));

		ConsoleCommandSender sender = Bukkit.getConsoleSender();
		sender.sendMessage(locMessage.build());
		Bukkit.dispatchCommand(sender, "tps");

		event.setDeathMessage(prefixBuilder.build().append(deathMessageComponent));
		event.setCancelled(false);
	}

}
