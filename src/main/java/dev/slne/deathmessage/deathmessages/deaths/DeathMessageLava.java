package dev.slne.deathmessage.deathmessages.deaths;

import dev.slne.deathmessage.deathmessages.DeathMessage;
import net.kyori.adventure.text.Component;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import java.util.List;

public class DeathMessageLava extends DeathMessage {

    public DeathMessageLava() {
        super(DamageCause.LAVA);
    }

    @Override
    public void registerDeathMessages(List<Component> components, Player player, Entity killer, Block killerBlock) {
        components.add(getPlayerDisplayName(player)
                .append(Component.text(" verwechselte Lava mit dem kühlen Nass!", DEFAULT_COLOR)));
        components.add(getPlayerDisplayName(player)
                .append(Component.text(" tat so, als wäre er Obsidian!", DEFAULT_COLOR)));
        components.add(getPlayerDisplayName(player)
                .append(Component.text(" wollte einen Vulkan von innen erkunden.", DEFAULT_COLOR)));
        components.add(getPlayerDisplayName(player)
                .append(Component.text(" hat einen heißen Tauchgang gemacht.", DEFAULT_COLOR)));
    }

}
