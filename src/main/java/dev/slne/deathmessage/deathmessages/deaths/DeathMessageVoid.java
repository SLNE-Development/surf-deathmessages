package dev.slne.deathmessage.deathmessages.deaths;

import dev.slne.deathmessage.deathmessages.DeathMessage;
import net.kyori.adventure.text.Component;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import java.util.List;

public class DeathMessageVoid extends DeathMessage {

    public DeathMessageVoid() {
        super(DamageCause.VOID);
    }

    @Override
    public void registerDeathMessages(List<Component> components, Player player, Entity killer, Block killerBlock) {
        components.add(getPlayerDisplayName(player).append(Component.text(" fällt nun für immer!", DEFAULT_COLOR)));
        components.add(getPlayerDisplayName(player).append(Component.text(" ist im Nichts verschwunden!", DEFAULT_COLOR)));
        components.add(getPlayerDisplayName(player).append(Component.text(" wird den Boden wohl nie erreichen.", DEFAULT_COLOR)));
        components.add(getPlayerDisplayName(player).append(Component.text(" verlor das Gleichgewicht und verschwand für immer.", DEFAULT_COLOR)));
    }

}
