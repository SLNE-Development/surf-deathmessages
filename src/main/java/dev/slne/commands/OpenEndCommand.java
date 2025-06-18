package dev.slne.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.slne.deathmessage.Main;
import dev.slne.deathmessage.message.MessageManager;
import net.kyori.adventure.text.Component;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class OpenEndCommand extends CommandAPICommand {
	public OpenEndCommand() {
		super("openend");

		withSubcommands(new CommandAPICommand("enable")
						.executes((sender, args) -> {
							Main.getInstance().setEndEnabled(true);
							if (sender instanceof Player player) {
								player.sendMessage(MessageManager.prefix().append(Component.text("Du hast das End geöffnet!", MessageManager.SUCCESS)));
							} else {
								sender.sendMessage("§7Du hast das End §ageöffnet§7!");
							}
						}),
				new CommandAPICommand("disable")
						.executes((sender, args) -> {
							Main.getInstance().setEndEnabled(false);
							if (sender instanceof Player player) {
								player.sendMessage(MessageManager.prefix().append(Component.text("Du hast das End geschlossen!", MessageManager.SUCCESS)));
							} else {
								sender.sendMessage("§7Du hast das End §ageschlossen§7!");
							}
						}));

		register();
	}
}
