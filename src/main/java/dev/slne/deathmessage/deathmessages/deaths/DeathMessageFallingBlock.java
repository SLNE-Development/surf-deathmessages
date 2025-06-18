package dev.slne.deathmessage.deathmessages.deaths;

import dev.slne.deathmessage.deathmessages.DeathMessage;
import net.kyori.adventure.text.Component;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import java.util.List;

public class DeathMessageFallingBlock extends DeathMessage {

    public DeathMessageFallingBlock() {
        super(DamageCause.FALLING_BLOCK);
    }

    @Override
    public void registerDeathMessages(List<Component> components, Player player, Entity killer, Block killerBlock) {

        if (killer != null && killer.getType().name().contains("ANVIL")) {
            components.add(getPlayerDisplayName(player)
                    .append(Component.text(" ist jetzt Platt wie eine Briefmarke.", DEFAULT_COLOR)));
            components.add(getPlayerDisplayName(player)
                    .append(Component.text(" wurde von einem Amboss erschlagen.", DEFAULT_COLOR)));
        }

        components.add(getPlayerDisplayName(player).append(Component.text(" wurde erschlagen!", DEFAULT_COLOR)));
    }

}
