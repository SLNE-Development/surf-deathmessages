/**
 *
 */
package dev.slne.deathmessage;

import dev.slne.commands.OpenEndCommand;
import dev.slne.deathmessage.deathmessages.DeathMessageManager;
import dev.slne.listeners.BukkitPortalListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

	private boolean endEnabled = false;

	@Override
	public void onEnable() {
		final PluginManager pluginManager = Bukkit.getPluginManager();
		DeathMessageManager deathMessageManager = new DeathMessageManager();
		deathMessageManager.bukkitListeners().forEach(listener -> pluginManager.registerEvents(listener, this));
		pluginManager.registerEvents(new BukkitPortalListener(), this);
		new OpenEndCommand();
	}

	@Override
	public void onDisable() {
	}

	public static Main getInstance() {
		return getPlugin(Main.class);
	}

	public boolean isEndEnabled() {
		return endEnabled;
	}

	public void setEndEnabled(boolean endEnabled) {
		this.endEnabled = endEnabled;
	}
}
