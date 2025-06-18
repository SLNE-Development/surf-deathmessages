package dev.slne.deathmessage.deathmessages.deaths;

import dev.slne.deathmessage.deathmessages.DeathMessage;
import net.kyori.adventure.text.Component;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import java.util.List;

public class DeathMessageEntitySweepAttack extends DeathMessage {

    public DeathMessageEntitySweepAttack() {
        super(DamageCause.ENTITY_SWEEP_ATTACK);
    }

    @Override
    public void registerDeathMessages(List<Component> components, Player player, Entity killer, Block killerBlock) {
        if (killer instanceof Player playerKiller) {
			components.add(getPlayerDisplayName(player).append(Component.text(" hat die Schwungkraft von ", DEFAULT_COLOR)).append(playerKiller.displayName()).append(Component.text(" falsch berechnet!", DEFAULT_COLOR)));
        } else {
            components.add(getPlayerDisplayName(player).append(Component.text(" wurde Opfer eines großen Schwungs!", DEFAULT_COLOR)));
        }

    }

}
