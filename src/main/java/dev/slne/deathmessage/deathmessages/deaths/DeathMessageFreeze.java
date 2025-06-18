package dev.slne.deathmessage.deathmessages.deaths;

import dev.slne.deathmessage.deathmessages.DeathMessage;
import net.kyori.adventure.text.Component;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import java.util.List;

public class DeathMessageFreeze extends DeathMessage {

    public DeathMessageFreeze() {
        super(DamageCause.FREEZE);
    }

    @Override
    public void registerDeathMessages(List<Component> components, Player player, Entity killer, Block killerBlock) {
        components.add(getPlayerDisplayName(player).append(Component.text(" hat seinen Mantel vergessen!", DEFAULT_COLOR)));
        components.add(getPlayerDisplayName(player).append(Component.text(" hat kalte Füße bekommen!", DEFAULT_COLOR)));
        components.add(getPlayerDisplayName(player).append(Component.text(" ist an Unterkühlung gestorben.", DEFAULT_COLOR)));
    }

}
