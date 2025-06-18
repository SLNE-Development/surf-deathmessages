package dev.slne.deathmessage.deathmessages.events;

import net.kyori.adventure.text.Component;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityDamageEvent;
import org.jetbrains.annotations.NotNull;

public class ExtendedPlayerDeathEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private final EntityDamageEvent lastEntityDamageEvent;
    private final Entity killerEntity;
    private final Block killerBlock;
    private final Player player;
    private boolean cancel = false;
    private Component deathMessage;

    /**
     * Constructor
     *
     * @param player                the player
     * @param lastEntityDamageEvent the last entity damage event
     * @param killerEntity          the killer entity
     * @param killerBlock           the killer block
     * @param deathMessage          the death message
     */
    public ExtendedPlayerDeathEvent(Player player, EntityDamageEvent lastEntityDamageEvent, Entity killerEntity, Block killerBlock, Component deathMessage) {
        this.player = player;
        this.lastEntityDamageEvent = lastEntityDamageEvent;
        this.killerBlock = killerBlock;
        this.killerEntity = killerEntity;
        this.deathMessage = deathMessage;
    }

    /**
     * Returns the handler list
     *
     * @return the handler list
     */
    public static HandlerList getHandlerList() {
        return handlers;
    }

    /**
     * Returns the cancelled state
     *
     * @return the cancelled state
     */
    @Override
    public boolean isCancelled() {
        return this.cancel;
    }

    /**
     * Sets the cancelled state
     *
     * @param cancel the cancelled state
     */
    @Override
    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }

    /**
     * Returns the death message
     *
     * @return the death message
     */
    public Component getDeathMessage() {
        return deathMessage;
    }

    /**
     * Sets the death message
     *
     * @param deathMessage the death message
     */
    public void setDeathMessage(Component deathMessage) {
        this.deathMessage = deathMessage;
    }

    /**
     * Returns the last entity damage event
     *
     * @return the last entity damage event
     */
    public EntityDamageEvent getLastEntityDamageEvent() {
        return lastEntityDamageEvent;
    }

    /**
     * Returns the killer entity
     *
     * @return the killer entity
     */
    public Entity getKillerEntity() {
        return killerEntity;
    }

    /**
     * Returns the killer block
     *
     * @return the killer block
     */
    public Block getKillerBlock() {
        return killerBlock;
    }

    /**
     * Returns the player
     *
     * @return the player
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Returns the handlers
     *
     * @return the handlers
     */
    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }

}