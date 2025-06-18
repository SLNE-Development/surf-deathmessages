package dev.slne.deathmessage.deathmessages.deaths;

import dev.slne.deathmessage.deathmessages.DeathMessage;
import net.kyori.adventure.text.Component;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.generator.WorldInfo;

import java.util.List;

public class DeathMessageBlockExplosion extends DeathMessage {

    public DeathMessageBlockExplosion() {
        super(DamageCause.BLOCK_EXPLOSION);
    }

    @Override
    public void registerDeathMessages(List<Component> components, Player player, Entity killer, Block killerBlock) {
        components.add(getPlayerDisplayName(player).append(Component.text(" kam durch TNT zu Tode!", DEFAULT_COLOR)));

        WorldInfo worldInfo = player.getWorld();
        worldInfo.getMaxHeight();
    }
}
