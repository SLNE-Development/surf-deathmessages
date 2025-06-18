package dev.slne.deathmessage.deathmessages.deaths;

import dev.slne.deathmessage.deathmessages.DeathMessage;
import net.kyori.adventure.text.Component;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import java.util.List;

public class DeathMessageLightning extends DeathMessage {

    public DeathMessageLightning() {
        super(DamageCause.LIGHTNING);
    }

    @Override
    public void registerDeathMessages(List<Component> components, Player player, Entity killer, Block killerBlock) {
        components.add(getPlayerDisplayName(player).append(Component.text(" hat Bekanntschaft mit dem ohmschen Gesetz gemacht!", DEFAULT_COLOR)));
        components.add(getPlayerDisplayName(player).append(Component.text(" wurde vom Blitz erschlagen!", DEFAULT_COLOR)));
        components.add(getPlayerDisplayName(player).append(Component.text(" wurde elektrisiert!", DEFAULT_COLOR)));
        components.add(Component.text("Zeus konnte ", DEFAULT_COLOR).append(getPlayerDisplayName(player)).append(Component.text(" wohl nicht leiden.", DEFAULT_COLOR)));
    }

}
