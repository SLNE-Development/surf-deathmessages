package dev.slne.deathmessage.deathmessages.deaths;

import dev.slne.deathmessage.deathmessages.DeathMessage;
import net.kyori.adventure.text.Component;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import java.util.List;

public class DeathMessageFire extends DeathMessage {

    public DeathMessageFire() {
        super(DamageCause.FIRE);
    }

    @Override
    public void registerDeathMessages(List<Component> components, Player player, Entity killer, Block killerBlock) {
        components.add(getPlayerDisplayName(player)
                .append(Component.text(" brannte wie Zunder!", DEFAULT_COLOR)));
        components.add(getPlayerDisplayName(player)
                .append(Component.text(" blieb im Kamin stecken!", DEFAULT_COLOR)));
        components.add(getPlayerDisplayName(player)
                .append(Component.text(" wurde gebraten!", DEFAULT_COLOR)));
        components.add(getPlayerDisplayName(player)
                .append(Component.text(" wurde bei lebendigem Leibe verbrannt!", DEFAULT_COLOR)));
        components.add(getPlayerDisplayName(player)
                .append(Component.text(" spielte mit dem Feuer und verbrannte sich.", DEFAULT_COLOR)));
        components.add(getPlayerDisplayName(player)
                .append(Component.text(" ist im Feuer aufgegangen!", DEFAULT_COLOR)));
    }

}
