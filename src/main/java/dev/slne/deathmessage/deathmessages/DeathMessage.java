package dev.slne.deathmessage.deathmessages;

import dev.slne.deathmessage.message.MessageManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public abstract class DeathMessage {

    private final DamageCause damageCause;
    private final Random random;
    protected TextColor DEFAULT_COLOR = MessageManager.SPACER;

    /**
     * Construct a new DeathMessage
     *
     * @param damageCause The DamageCause of the DeathMessage
     */
    public DeathMessage(DamageCause damageCause) {
        this.damageCause = damageCause;
        this.random = new Random();
    }

    /**
     * Get the DamageCause of the DeathMessage
     *
     * @return The DamageCause of the DeathMessage
     */
    public DamageCause getDamageCause() {
        return damageCause;
    }

    /**
     * Register the DeathMessages for the DeathMessage
     *
     * @param player      The Player who died
     * @param killer      The Entity who killed the Player
     * @param killerBlock The Block which killed the Player
     */
    public abstract void registerDeathMessages(List<Component> components, Player player, Entity killer, Block killerBlock);

    /**
     * Get a random DeathMessage for the DeathMessage
     *
     * @param player      The Player who died
     * @param killer      The Entity who killed the Player
     * @param killerBlock The Block which killed the Player
     *
     * @return A random DeathMessage for the DeathMessage
     */
    public Component getDeathMessage(Player player, Entity killer, Block killerBlock) {
        List<Component> components = new ArrayList<>();
        registerDeathMessages(components, player, killer, killerBlock);

        final Component randomComponent = getRandomComponent(components);

        components = null;

        return randomComponent;
    }

    /**
     * Get a random Component from the DeathMessage
     *
     * @return A random Component from the DeathMessage
     */
    public Component getRandomComponent(List<Component> components) {
        if (components.isEmpty()) {
            return null;
        }

        int randomInt = random.nextInt(components.size());
        return components.get(randomInt);
    }

    /**
     * Get the display name of the Player
     *
     * @return The display name of the Player
     */
    protected Component getPlayerDisplayName(Player player) {
        return player.displayName().colorIfAbsent(MessageManager.VARIABLE_VALUE);
    }
}
