package dev.slne.deathmessage.deathmessages.deaths;

import dev.slne.deathmessage.deathmessages.DeathMessage;
import net.kyori.adventure.text.Component;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import java.util.List;

public class DeathMessageThorns extends DeathMessage {

    public DeathMessageThorns() {
        super(DamageCause.THORNS);
    }

    @Override
    public void registerDeathMessages(List<Component> components, Player player, Entity killer, Block killerBlock) {
        if (killer instanceof Player playerKiller) {
            components.add(getPlayerDisplayName(player).append(Component.text(" hat sich an ", DEFAULT_COLOR)).append(getPlayerDisplayName(playerKiller)).append(Component.text("s zu Tode gepieckst!", DEFAULT_COLOR)));
        } else {
            components.add(getPlayerDisplayName(player).append(Component.text(" hat sich an einer Rüstung zu Tode gepieckst!", DEFAULT_COLOR)));
        }
    }

}
