package dev.slne.deathmessage.deathmessages.deaths;

import dev.slne.deathmessage.deathmessages.DeathMessage;
import net.kyori.adventure.text.Component;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import java.util.List;

public class DeathMessageFall extends DeathMessage {

    public DeathMessageFall() {
        super(DamageCause.FALL);
    }

    @Override
    public void registerDeathMessages(List<Component> components, Player player, Entity killer, Block killerBlock) {

        if (player.getInventory().getChestplate() != null
                && player.getInventory().getChestplate().getType().name().contains("ELYTRA")) {
            components.add(getPlayerDisplayName(player)
                    .append(Component.text(" ist mit dem Gesicht voran gelandet.", DEFAULT_COLOR)));
            components.add(getPlayerDisplayName(player)
                    .append(Component.text(" konnte mit der Elytra nicht umgehen.", DEFAULT_COLOR)));
            components.add(getPlayerDisplayName(player)
                    .append(Component.text("'s Flugzeug ist abgestürzt!' ", DEFAULT_COLOR)));
            components.add(getPlayerDisplayName(player)
                    .append(Component.text(" zerschellte auf die Landebahn!", DEFAULT_COLOR)));
            components.add(getPlayerDisplayName(player)
                    .append(Component.text(" hat vergessen seine Flügel auszubreiten!", DEFAULT_COLOR)));
            components.add(getPlayerDisplayName(player)
                    .append(Component.text(" dachte er könnte fliegen!", DEFAULT_COLOR)));
            components.add(getPlayerDisplayName(player)
                    .append(Component.text(" vergaß, dass Elytren zum Fliegen gedacht sind.", DEFAULT_COLOR)));
            components.add(getPlayerDisplayName(player)
                    .append(Component.text(" hätte vor dem Flug das Handbuch lesen sollen.", DEFAULT_COLOR)));
        } else {
            components.add(getPlayerDisplayName(player)
                    .append(Component.text(" ist auf dem Boden aufgeschlagen!", DEFAULT_COLOR)));
            components.add(getPlayerDisplayName(player)
                    .append(Component.text(" hat die Höhe wohl unterschätzt!", DEFAULT_COLOR)));
            components.add(getPlayerDisplayName(player)
                    .append(Component.text(" hat den Boden weicher eingeschätzt.", DEFAULT_COLOR)));
            components.add(getPlayerDisplayName(player)
                    .append(Component.text(" zermatschte auf dem Boden.", DEFAULT_COLOR)));
            components.add(getPlayerDisplayName(player)
                    .append(Component.text(" dachte er hätte einen Fallschirm.", DEFAULT_COLOR)));
            components.add(getPlayerDisplayName(player)
                    .append(Component.text(" fiel elegant wie ein Stein zu boden.", DEFAULT_COLOR)));
            components.add(getPlayerDisplayName(player)
                    .append(Component.text(" hat X mit Y verwechselt.", DEFAULT_COLOR)));
            components.add(getPlayerDisplayName(player)
                    .append(Component.text(" hatte die Schwerkraft nicht auf dem Schirm!", DEFAULT_COLOR)));
            components.add(getPlayerDisplayName(player)
                    .append(Component.text(" hat den Boden geküsst.", DEFAULT_COLOR)));
            components.add(getPlayerDisplayName(player)
                    .append(Component.text(" hat den schnellsten Weg nach unten gewählt!", DEFAULT_COLOR)));
        }
    }

}
