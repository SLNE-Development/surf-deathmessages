package dev.slne.deathmessage.deathmessages.deaths;

import dev.slne.deathmessage.deathmessages.DeathMessage;
import net.kyori.adventure.text.Component;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import java.util.List;

public class DeathMessageHotFloor extends DeathMessage {

    public DeathMessageHotFloor() {
        super(DamageCause.HOT_FLOOR);
    }

    @Override
    public void registerDeathMessages(List<Component> components, Player player, Entity killer, Block killerBlock) {
        components.add(getPlayerDisplayName(player).append(Component.text(" verschmolz mit einem Magmablock!", DEFAULT_COLOR)));
        components.add(getPlayerDisplayName(player).append(Component.text(" hat sich die Füße verbrannt.", DEFAULT_COLOR)));
    }

}
