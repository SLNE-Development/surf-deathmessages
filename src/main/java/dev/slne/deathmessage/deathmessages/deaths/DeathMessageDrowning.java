package dev.slne.deathmessage.deathmessages.deaths;

import dev.slne.deathmessage.deathmessages.DeathMessage;
import net.kyori.adventure.text.Component;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import java.util.List;

public class DeathMessageDrowning extends DeathMessage {

    public DeathMessageDrowning() {
        super(DamageCause.DROWNING);
    }

    @Override
    public void registerDeathMessages(List<Component> components, Player player, Entity killer, Block killerBlock) {
        components.add(getPlayerDisplayName(player)
                .append(Component.text(" ist abgesoffen.", DEFAULT_COLOR)));
        components.add(getPlayerDisplayName(player)
                .append(Component.text(" schwimmt nun mit den Fischen.", DEFAULT_COLOR)));
        components.add(getPlayerDisplayName(player)
                .append(Component.text(" hat vergessen, dass Menschen keine Kiemen haben.", DEFAULT_COLOR)));
        components.add(getPlayerDisplayName(player)
                .append(Component.text(" ist untergegangen!", DEFAULT_COLOR)));
        components.add(getPlayerDisplayName(player)
                .append(Component.text(" ertrank in einem Glas Wasser.", DEFAULT_COLOR)));
        components.add(getPlayerDisplayName(player)
                .append(Component.text(" konnte wohl nicht schwimmen.", DEFAULT_COLOR)));
        components.add(getPlayerDisplayName(player)
                .append(Component.text(" ging unter wie ein Stein.", DEFAULT_COLOR)));
        components.add(Component.text("Das Boot von ", DEFAULT_COLOR)
                .append(getPlayerDisplayName(player))
                .append(Component.text(" ist wohl gekentert.", DEFAULT_COLOR)));
        components.add(getPlayerDisplayName(player)
                .append(Component.text(" ist die Luft ausgegangen.", DEFAULT_COLOR)));
        components.add(getPlayerDisplayName(player)
                .append(Component.text(" wurde von den Tiefen verschluckt.", DEFAULT_COLOR)));
        components.add(getPlayerDisplayName(player)
                .append(Component.text(" hat den Ozean ein wenig zu tief erforscht.", DEFAULT_COLOR)));
        components.add(getPlayerDisplayName(player)
                .append(Component.text(" taucht wohl nicht mehr auf.", DEFAULT_COLOR)));
    }

}
