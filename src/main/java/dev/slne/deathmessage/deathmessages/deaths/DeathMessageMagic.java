package dev.slne.deathmessage.deathmessages.deaths;

import dev.slne.deathmessage.deathmessages.DeathMessage;
import net.kyori.adventure.text.Component;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Witch;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import java.util.List;

public class DeathMessageMagic extends DeathMessage {

    public DeathMessageMagic() {
        super(DamageCause.MAGIC);
    }

    @Override
    public void registerDeathMessages(List<Component> components, Player player, Entity killer, Block killerBlock) {
        if (killer instanceof Witch) {
            components.add(getPlayerDisplayName(player).append(Component.text(" wurde verhext!", DEFAULT_COLOR)));
            components.add(getPlayerDisplayName(player).append(Component.text(" wurde von einer Hexe getötet!", DEFAULT_COLOR)));
            components.add(getPlayerDisplayName(player).append(Component.text(" hat am falschen Haus geklopft.", DEFAULT_COLOR)));
            components.add(Component.text("Knusper, knusper, knäuschen, wer knuspert da an meinem Häuschen? Es war ", DEFAULT_COLOR).append(getPlayerDisplayName(player)).append(Component.text(".", DEFAULT_COLOR)));

        } else {
            components.add(getPlayerDisplayName(player).append(Component.text(" wurde durch Magie getötet!", DEFAULT_COLOR)));
            components.add(getPlayerDisplayName(player).append(Component.text(" hat den falschen Trank getrunken.", DEFAULT_COLOR)));
        }
    }
}
