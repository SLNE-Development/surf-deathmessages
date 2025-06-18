package dev.slne.deathmessage.deathmessages.deaths;

import dev.slne.deathmessage.deathmessages.DeathMessage;
import net.kyori.adventure.text.Component;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import java.util.List;

public class DeathMessagePoison extends DeathMessage {

    public DeathMessagePoison() {
        super(DamageCause.POISON);
    }

    @Override
    public void registerDeathMessages(List<Component> components, Player player, Entity killer, Block killerBlock) {
        components.add(getPlayerDisplayName(player).append(Component.text(" starb an einer Vergiftung!", DEFAULT_COLOR)));
        components.add(getPlayerDisplayName(player).append(Component.text(" hat wohl nicht auf das Verfallsdatum geachtet.", DEFAULT_COLOR)));
        components.add(getPlayerDisplayName(player).append(Component.text(" wurde dahingerafft.", DEFAULT_COLOR)));
    }

}
