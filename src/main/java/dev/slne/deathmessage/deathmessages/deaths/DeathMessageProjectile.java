package dev.slne.deathmessage.deathmessages.deaths;

import dev.slne.deathmessage.deathmessages.DeathMessage;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import java.util.List;

public class DeathMessageProjectile extends DeathMessage {

	public DeathMessageProjectile() {
		super(DamageCause.PROJECTILE);
	}

	@Override
	public void registerDeathMessages(List<Component> components, Player player, Entity killer, Block killerBlock) {

		if (killer instanceof Projectile projectile) {

			Entity owner;
			try {
				owner = Bukkit.getEntity(projectile.getOwnerUniqueId());
			} catch (NullPointerException exception) {
				components.add(getPlayerDisplayName(player)
						.append(Component.text(" wurde erschossen.", DEFAULT_COLOR)));
				return;
			}

			if (owner instanceof Player killerPlayer) {
				components.add(getPlayerDisplayName(player)
						.append(Component.text(" wurde von ", DEFAULT_COLOR))
						.append(getPlayerDisplayName(killerPlayer))
						.append(Component.text(" erschossen!", DEFAULT_COLOR)));
				components.add(getPlayerDisplayName(player)
						.append(Component.text(" sollte sich vor den Pfeilen von ",
								DEFAULT_COLOR))
						.append(getPlayerDisplayName(killerPlayer))
						.append(Component.text(" in Acht nehmen!", DEFAULT_COLOR)));
				components.add(getPlayerDisplayName(killerPlayer)
						.append(Component.text(" ist wohl ein besserer " + "Schütze als ",
								DEFAULT_COLOR))
						.append(getPlayerDisplayName(player))
						.append(Component.text("!", DEFAULT_COLOR)));
				components.add(getPlayerDisplayName(killerPlayer)
						.append(Component.text(" hat ", DEFAULT_COLOR))
						.append(getPlayerDisplayName(player))
						.append(Component.text(" durchlöchert!", DEFAULT_COLOR)));
				components.add(getPlayerDisplayName(player)
						.append(Component.text(" hat den Pfeil von ", DEFAULT_COLOR))
						.append(getPlayerDisplayName(killerPlayer))
						.append(Component.text(" nicht kommen sehen.", DEFAULT_COLOR)));
			} else {
				if (owner instanceof Skeleton) {
					components.add(
							getPlayerDisplayName(player)
									.append(Component.text(
											" wurde von einem Skelett erschossen!", DEFAULT_COLOR)));
					components
							.add(Component.text("Ein Skelett traf ", DEFAULT_COLOR)
									.append(getPlayerDisplayName(player))
									.append(Component.text(" mitten in den Kopf.",
											DEFAULT_COLOR)));
				}

				if (owner instanceof Stray) {
					components.add(
							getPlayerDisplayName(player)
									.append(Component.text(
											" wurde von einem Stray durchlöchert!", DEFAULT_COLOR)));
					components.add(
							Component.text("Ein Stray erschoss ", DEFAULT_COLOR)
									.append(getPlayerDisplayName(player))
									.append(Component.text("!", DEFAULT_COLOR)));
				}

				if (owner instanceof Pillager) {
					components.add(getPlayerDisplayName(player)
							.append(Component.text(
									" bekam einen Armbrustschuss zwischen die Augen!", DEFAULT_COLOR)));
					components.add(Component.text("Eine Pillager Patrouille hat ", DEFAULT_COLOR)
							.append(getPlayerDisplayName(player))
							.append(Component.text(" erschossen.", DEFAULT_COLOR)));
				}

				if (killer instanceof LlamaSpit) {
					components.add(getPlayerDisplayName(player)
							.append(Component.text(" wurde angespuckt!", DEFAULT_COLOR)));
					components.add(Component.text("Ein Lama hat ", DEFAULT_COLOR)
							.append(getPlayerDisplayName(player))
							.append(Component.text(" voll gerotzt.", DEFAULT_COLOR)));
				}

				if (killer instanceof ShulkerBullet) {
					components.add(
							getPlayerDisplayName(player).append(Component
									.text(" kam durch einen Shulker ums Leben.", DEFAULT_COLOR)));
					components
							.add(Component.text("Ein Shulker hat ", DEFAULT_COLOR)
									.append(getPlayerDisplayName(player))
									.append(Component.text(" erschossen.",
											DEFAULT_COLOR)));
				}

				if (killer instanceof Trident) {
					components.add(getPlayerDisplayName(player)
							.append(Component.text(
									" wurde von einem Dreizack aufgespießt!", DEFAULT_COLOR)));
				}

			}

			return;
		}

		components.add(getPlayerDisplayName(player)
				.append(Component.text(" wurde erschossen.", DEFAULT_COLOR)));

	}

}
