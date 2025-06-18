package dev.slne.deathmessage.deathmessages;

import dev.slne.deathmessage.deathmessages.deaths.*;
import dev.slne.deathmessage.deathmessages.listeners.DamageListener;
import dev.slne.deathmessage.deathmessages.listeners.DeathListener;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import java.util.ArrayList;
import java.util.List;

public class DeathMessageManager {

    private final List<DeathMessage> deathMessages;
    private DeathMessage unknownDeathMessage;

    /**
     * Creates a new death message manager.
     */
    public DeathMessageManager() {
        this.deathMessages = new ArrayList<>();

        // If nothing matches
        this.unknownDeathMessage = new DeathMessageUnknown();

        // Register death messages
        this.deathMessages.add(new DeathMessageBlockExplosion());
        this.deathMessages.add(new DeathMessageContact());
        this.deathMessages.add(new DeathMessageCramming());
        this.deathMessages.add(new DeathMessageDragonBreath());
        this.deathMessages.add(new DeathMessageDrowning());
        this.deathMessages.add(new DeathMessageEntityAttack());
        this.deathMessages.add(new DeathMessageEntityExplosion());
        this.deathMessages.add(new DeathMessageEntitySweepAttack());
        this.deathMessages.add(new DeathMessageFall());
        this.deathMessages.add(new DeathMessageFallingBlock());
        this.deathMessages.add(new DeathMessageFire());
        this.deathMessages.add(new DeathMessageFireTick());
        this.deathMessages.add(new DeathMessageFlyIntoWall());
        this.deathMessages.add(new DeathMessageFreeze());
        this.deathMessages.add(new DeathMessageHotFloor());
        this.deathMessages.add(new DeathMessageLava());
        this.deathMessages.add(new DeathMessageLightning());
        this.deathMessages.add(new DeathMessageMagic());
        this.deathMessages.add(new DeathMessagePoison());
        this.deathMessages.add(new DeathMessageProjectile());
        this.deathMessages.add(new DeathMessageStarvation());
        this.deathMessages.add(new DeathMessageSuffocation());
        this.deathMessages.add(new DeathMessageSuicide());
        this.deathMessages.add(new DeathMessageThorns());
        this.deathMessages.add(new DeathMessageVoid());
        this.deathMessages.add(new DeathMessageWither());

    }

    /**
     * Returns the death message for the given damage cause.
     *
     * @param damageCause The damage cause
     *
     * @return The death message
     */
    public DeathMessage getDeathMessageForType(DamageCause damageCause) {
        if (damageCause == null) {
            return this.unknownDeathMessage;
        }

        return this.deathMessages.stream().filter(deathMessage -> deathMessage.getDamageCause().equals(damageCause)).findFirst().orElse(this.unknownDeathMessage);
    }

    /**
     * Returns the unknown death message.
     *
     * @return The unknown death message
     */
    public DeathMessage getUnknownDeathMessage() {
        return unknownDeathMessage;
    }

    public List<Listener> bukkitListeners() {
        return List.of(new DeathListener(this), new DamageListener());
    }

}
