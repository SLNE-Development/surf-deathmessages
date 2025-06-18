package dev.slne.deathmessage.deathmessages.deaths;

import dev.slne.deathmessage.deathmessages.DeathMessage;
import net.kyori.adventure.text.Component;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.List;

public class DeathMessageUnknown extends DeathMessage {

    public DeathMessageUnknown() {
        super(null);
    }

    @Override
    public void registerDeathMessages(List<Component> components, Player player, Entity killer, Block killerBlock) {
        components.add(getPlayerDisplayName(player).append(Component.text(" ist gestorben!", DEFAULT_COLOR)));
        components.add(getPlayerDisplayName(player).append(Component.text(" ist von uns gegangen!", DEFAULT_COLOR)));
    }

}
