package dev.slne.deathmessage.deathmessages.deaths;

import dev.slne.deathmessage.deathmessages.DeathMessage;
import net.kyori.adventure.text.Component;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import java.util.List;

public class DeathMessageFireTick extends DeathMessage {

    public DeathMessageFireTick() {
        super(DamageCause.FIRE_TICK);
    }

    @Override
    public void registerDeathMessages(List<Component> components, Player player, Entity killer, Block killerBlock) {
        components.add(getPlayerDisplayName(player)
                .append(Component.text(" ist nur noch ein Häufchen Asche!", DEFAULT_COLOR)));
        components.add(getPlayerDisplayName(player)
                .append(Component.text(" spielte mit dem Feuer.", DEFAULT_COLOR)));
        components.add(getPlayerDisplayName(player)
                .append(Component.text(" ging in Flammen auf!", DEFAULT_COLOR)));
        components.add(getPlayerDisplayName(player)
                .append(Component.text(" musste lernen, dass Feuer kein Spielzeug ist.", DEFAULT_COLOR)));
        components.add(getPlayerDisplayName(player)
                .append(Component.text(" hatte ein heißes Date mit dem Feuer.", DEFAULT_COLOR)));
        components.add(getPlayerDisplayName(player)
                .append(Component.text(" verbrannte sich die Finger!", DEFAULT_COLOR)));
    }

}
