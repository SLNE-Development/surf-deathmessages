package dev.slne.deathmessage.deathmessages.deaths;

import dev.slne.deathmessage.deathmessages.DeathMessage;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import java.util.List;

public class DeathMessageContact extends DeathMessage {

    public DeathMessageContact() {
        super(DamageCause.CONTACT);
    }

    @Override
    public void registerDeathMessages(List<Component> components, Player player, Entity killer, Block killerBlock) {
        if (killerBlock == null) {
            components.add(getPlayerDisplayName(player)
                    .append(Component.text(" wurde zu Tode gestochen!", DEFAULT_COLOR)));
            return;
        }

        if (killerBlock.getType().equals(Material.CACTUS)) {
            components.add(getPlayerDisplayName(player)
                    .append(Component.text(" hat sich an einem Kaktus gestochen!", DEFAULT_COLOR)));
            components.add(getPlayerDisplayName(player)
                    .append(Component.text(" wird nächstes mal Abstand zu Stacheln halten!", DEFAULT_COLOR)));
            components.add(Component.text("Was ist grün und stachelig?  ", DEFAULT_COLOR)
                    .append(getPlayerDisplayName(player))
                    .append(Component.text(" hat es herausgefunden.", DEFAULT_COLOR)));
            components.add(getPlayerDisplayName(player)
                    .append(Component.text(" steckt jetzt ein Stachel im Hintern.", DEFAULT_COLOR)));
            components.add(getPlayerDisplayName(player)
                    .append(Component.text(" wurde von einem stacheligen Freund umarmt.", DEFAULT_COLOR)));
        }

        if (killerBlock.getType().equals(Material.SWEET_BERRY_BUSH)) {
            components.add(getPlayerDisplayName(player)
                    .append(Component.text(" kam beim Beerensammeln ums Leben.", DEFAULT_COLOR)));
            components.add(getPlayerDisplayName(player)
                    .append(Component.text(" wurde von einem Beerenstrauch besiegt!", DEFAULT_COLOR)));
            components.add(Component.text("Ein Busch hat ", DEFAULT_COLOR)
                    .append(getPlayerDisplayName(player))
                    .append(Component.text(" den gar aus gemacht!", DEFAULT_COLOR)));
            components.add(getPlayerDisplayName(player)
                    .append(Component.text(" wurde von süßen Beeren bitter entäuscht.", DEFAULT_COLOR)));
        }

        if (killerBlock.getType().equals(Material.POINTED_DRIPSTONE)) {
            components.add(getPlayerDisplayName(player)
                    .append(Component.text(" kam zu nah an Gottes Stachel!", DEFAULT_COLOR)));
            components.add(getPlayerDisplayName(player)
                    .append(Component.text(" bekam eine Lanze zwischen die Beine", DEFAULT_COLOR)));
        }

    }

}
