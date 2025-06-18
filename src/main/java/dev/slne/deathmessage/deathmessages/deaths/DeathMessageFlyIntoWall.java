package dev.slne.deathmessage.deathmessages.deaths;

import dev.slne.deathmessage.deathmessages.DeathMessage;
import net.kyori.adventure.text.Component;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import java.util.List;

public class DeathMessageFlyIntoWall extends DeathMessage {

    public DeathMessageFlyIntoWall() {
        super(DamageCause.FLY_INTO_WALL);
    }

    @Override
    public void registerDeathMessages(List<Component> components, Player player, Entity killer, Block killerBlock) {
        components.add(getPlayerDisplayName(player)
                .append(Component.text(" ist gegen eine Wand geflogen.", DEFAULT_COLOR)));
        components.add(getPlayerDisplayName(player)
                .append(Component.text(" hat die Wand nicht kommen sehen.", DEFAULT_COLOR)));
        components.add(getPlayerDisplayName(player)
                .append(Component.text(" sollte über seine Flsugtauglichkeit nachdenken.", DEFAULT_COLOR)));
        components.add(getPlayerDisplayName(player)
                .append(Component.text(" lernte auf die harte Tour, dass Wände nicht nachgeben.", DEFAULT_COLOR)));
    }

}
