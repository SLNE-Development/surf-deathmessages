package dev.slne.deathmessage.deathmessages.deaths;

import dev.slne.deathmessage.deathmessages.DeathMessage;
import net.kyori.adventure.text.Component;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import java.util.List;

public class DeathMessageSuffocation extends DeathMessage {

    public DeathMessageSuffocation() {
        super(DamageCause.SUFFOCATION);
    }

    @Override
    public void registerDeathMessages(List<Component> components, Player player, Entity killer, Block killerBlock) {
        components.add(getPlayerDisplayName(player).append(Component.text(" wurde lebendig begraben!", DEFAULT_COLOR)));
        components.add(getPlayerDisplayName(player).append(Component.text(" muss nicht mehr begraben werden.", DEFAULT_COLOR)));
        components.add(getPlayerDisplayName(player).append(Component.text(" wird irgendwann als Fossil enden.", DEFAULT_COLOR)));
    }

}
