package dev.slne.deathmessage.deathmessages.deaths;

import dev.slne.deathmessage.deathmessages.DeathMessage;
import net.kyori.adventure.text.Component;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import java.util.List;

public class DeathMessageDragonBreath extends DeathMessage {

    public DeathMessageDragonBreath() {
        super(DamageCause.DRAGON_BREATH);
    }

    @Override
    public void registerDeathMessages(List<Component> components, Player player, Entity killer, Block killerBlock) {
        components.add(Component.text("Ein Drache hat ", DEFAULT_COLOR).append(getPlayerDisplayName(player)).append(Component.text(" geröstet.", DEFAULT_COLOR)));
    }

}
