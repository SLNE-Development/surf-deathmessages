package dev.slne.deathmessage.deathmessages.deaths;

import dev.slne.deathmessage.deathmessages.DeathMessage;
import net.kyori.adventure.text.Component;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import java.util.List;

public class DeathMessageEntityExplosion extends DeathMessage {

    public DeathMessageEntityExplosion() {
        super(DamageCause.ENTITY_EXPLOSION);
    }

    @Override
    public void registerDeathMessages(List<Component> components, Player player, Entity killer, Block killerBlock) {
        if (killer instanceof EnderCrystal) {
            components.add(getPlayerDisplayName(player)
                    .append(Component.text(" wurde von einem End Crystal zerfetzt!", DEFAULT_COLOR)));
            components.add(Component.text("Ein wunderschöner Kristall hat ", DEFAULT_COLOR)
                    .append(getPlayerDisplayName(player))
                    .append(Component.text(" in die Luft gejagt!", DEFAULT_COLOR)));
        } else if (killer instanceof Creeper) {
            components.add(getPlayerDisplayName(player)
                    .append(Component.text(" wurde von einem Creeper hops genommen.", DEFAULT_COLOR)));
            components.add(getPlayerDisplayName(player)
                    .append(Component.text(" wurde von einem Creeper in tausend Teile zerfetzt.", DEFAULT_COLOR)));
            components.add(Component.text("Ein Creeper hat ", DEFAULT_COLOR).append(getPlayerDisplayName(player))
                    .append(Component.text(" in die Luft gejagt!", DEFAULT_COLOR)));
            components.add(getPlayerDisplayName(player)
                    .append(Component.text(" erlag der explosiven Persönlichkeit eines Creepers.", DEFAULT_COLOR)));
            components.add(getPlayerDisplayName(player)
                    .append(Component.text(" verwschand mit einem großen Knall.", DEFAULT_COLOR)));
        } else if (killer instanceof Fireball) {
            components.add(
                    getPlayerDisplayName(player).append(Component.text(" wurde vom Ghast getroffen!", DEFAULT_COLOR)));
            components.add(Component.text("Ein Ghast hat ", DEFAULT_COLOR).append(getPlayerDisplayName(player))
                    .append(Component.text(" in tausend Teile zerfetzt!", DEFAULT_COLOR)));
        } else if (killer instanceof TNTPrimed) {
            components.add(getPlayerDisplayName(player)
                    .append(Component.text(" hat den Böller nicht rechtzeitig weggeworfen.", DEFAULT_COLOR)));
            components.add(Component.text("Der Sprengmeister ", DEFAULT_COLOR).append(getPlayerDisplayName(player))
                    .append(Component.text(" sollte seine Berufswahl überdenken.", DEFAULT_COLOR)));
            components.add(Component.text("3, 2, 1, BOOM! ", DEFAULT_COLOR).append(getPlayerDisplayName(player))
                    .append(Component.text(" wurde nie wieder gesehen.", DEFAULT_COLOR)));
            components.add(getPlayerDisplayName(player)
                    .append(Component.text(" ist das TNT um die Ohrenn geflogen!", DEFAULT_COLOR)));
            components.add(
                    Component.text("Illegale Pyrotechnik fordert immer wieder Opfer. Diesmal war es ", DEFAULT_COLOR)
                            .append(getPlayerDisplayName(player)).append(Component.text("!", DEFAULT_COLOR)));
        } else {
            components.add(getPlayerDisplayName(player).append(Component.text(" wurde weggesprengt!", DEFAULT_COLOR)));
        }
    }
}
