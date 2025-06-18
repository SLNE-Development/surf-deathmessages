package dev.slne.deathmessage.deathmessages.deaths;

import dev.slne.deathmessage.deathmessages.DeathMessage;
import net.kyori.adventure.text.Component;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import java.util.List;

public class DeathMessageWither extends DeathMessage {

    public DeathMessageWither() {
        super(DamageCause.WITHER);
    }

    @Override
    public void registerDeathMessages(List<Component> components, Player player, Entity killer, Block killerBlock) {
        if (killer instanceof WitherSkull || killer instanceof Wither) {
            components.add(getPlayerDisplayName(player).append(Component.text(" hatte Wohl nicht die Ausrüstung um gegen den Wither zu kämpfen.", DEFAULT_COLOR)));
            components.add(Component.text("Ein Wither hat ", DEFAULT_COLOR).append(getPlayerDisplayName(player)).append(Component.text(" aus dem Leben gerissen!", DEFAULT_COLOR)));
            components.add(Component.text("Eins zu null für den Wither, ", DEFAULT_COLOR).append(getPlayerDisplayName(player)).append(Component.text("!", DEFAULT_COLOR)));

        } else if (killer instanceof WitherSkeleton) {
            components.add(getPlayerDisplayName(player).append(Component.text(" ist verfault.", DEFAULT_COLOR)));
            components.add(Component.text("Ein Wither Skelett hatte ", DEFAULT_COLOR).append(getPlayerDisplayName(player)).append(Component.text(" vergifetet!", DEFAULT_COLOR)));
        } else {
            components.add(getPlayerDisplayName(player).append(Component.text(" starb durch eine Rose.", DEFAULT_COLOR)));
            components.add(getPlayerDisplayName(player).append(Component.text(" kam beim Blumenpflücken ums leben!", DEFAULT_COLOR)));
            components.add(getPlayerDisplayName(player).append(Component.text(" wird sobald nicht mehr an Rosen schnuppern.", DEFAULT_COLOR)));
        }
    }

}
