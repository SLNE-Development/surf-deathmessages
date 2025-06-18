package dev.slne.deathmessage.deathmessages.deaths;

import dev.slne.deathmessage.deathmessages.DeathMessage;
import net.kyori.adventure.text.Component;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import java.util.List;

public class DeathMessageStarvation extends DeathMessage {

    public DeathMessageStarvation() {
        super(DamageCause.STARVATION);
    }

    @Override
    public void registerDeathMessages(List<Component> components, Player player, Entity killer, Block killerBlock) {
        components.add(getPlayerDisplayName(player).append(Component.text(" ist verhungert!", DEFAULT_COLOR)));
        components.add(getPlayerDisplayName(player).append(Component.text(" hatte nichts mehr zu Essen!", DEFAULT_COLOR)));
        components.add(getPlayerDisplayName(player).append(Component.text(" übertrieb es mit der Diät.", DEFAULT_COLOR)));
    }

}
