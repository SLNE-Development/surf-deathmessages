package dev.slne.deathmessage.deathmessages.deaths;

import dev.slne.deathmessage.deathmessages.DeathMessage;
import net.kyori.adventure.text.Component;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import java.util.List;

public class DeathMessageEntityAttack extends DeathMessage {

    public DeathMessageEntityAttack() {
        super(DamageCause.ENTITY_ATTACK);
    }

    @Override
    public void registerDeathMessages(List<Component> components, Player player, Entity killer, Block killerBlock) {
        if (killer instanceof Player playerKiller) {
            components.add(getPlayerDisplayName(player).append(Component.text(" wurde von ", DEFAULT_COLOR))
                    .append(getPlayerDisplayName(playerKiller)).append(Component.text(" ermordet!", DEFAULT_COLOR)));
            components.add(getPlayerDisplayName(player).append(Component.text(" wurde von ", DEFAULT_COLOR))
                    .append(getPlayerDisplayName(playerKiller)).append(Component.text(" aufgespießt!", DEFAULT_COLOR)));
            components.add(getPlayerDisplayName(player).append(Component.text(" hätte sich nicht mit ", DEFAULT_COLOR))
                    .append(getPlayerDisplayName(playerKiller))
                    .append(Component.text(" anlegen sollen!", DEFAULT_COLOR)));
            components.add(getPlayerDisplayName(player).append(Component.text(" wurde von ", DEFAULT_COLOR))
                    .append(getPlayerDisplayName(playerKiller))
                    .append(Component.text(" zur Strecke gebracht!", DEFAULT_COLOR)));

            components.add(getPlayerDisplayName(playerKiller).append(Component.text(" tötete ", DEFAULT_COLOR))
                    .append(getPlayerDisplayName(player))
                    .append(Component.text(" ohne mit der Wimper zu zucken!", DEFAULT_COLOR)));
            components.add(getPlayerDisplayName(playerKiller).append(Component.text(" hat ", DEFAULT_COLOR))
                    .append(getPlayerDisplayName(player))
                    .append(Component.text(" hinterrücks ermordet!", DEFAULT_COLOR)));
            components.add(getPlayerDisplayName(playerKiller).append(Component.text(" hat ", DEFAULT_COLOR))
                    .append(getPlayerDisplayName(player))
                    .append(Component.text(" eine Lektion erteilt!", DEFAULT_COLOR)));
            components.add(getPlayerDisplayName(playerKiller).append(Component.text(" hat ", DEFAULT_COLOR))
                    .append(getPlayerDisplayName(player))
                    .append(Component.text(" ins Jenseits befördert!", DEFAULT_COLOR)));
        } else {
            if (killer instanceof ZombieVillager) {
                components.add(getPlayerDisplayName(player)
                        .append(Component.text(" wurde von einem Zombie Villager getötet.", DEFAULT_COLOR)));
                components.add(
                        Component.text("Ein Zombie Villager hat ", DEFAULT_COLOR).append(getPlayerDisplayName(player))
                                .append(Component.text(" den gar ausgemacht!", DEFAULT_COLOR)));
            } else if (killer instanceof Husk) {
                components.add(getPlayerDisplayName(player)
                        .append(Component.text(" wurde von einer Mumie ermordet!", DEFAULT_COLOR)));
                components.add(Component.text("Ein Wüstenzombie fiel über ", DEFAULT_COLOR)
                        .append(getPlayerDisplayName(player)).append(Component.text(" her!", DEFAULT_COLOR)));
            } else if (killer instanceof Drowned) {
                components.add(getPlayerDisplayName(player)
                        .append(Component.text(" traf auf einen Bewohner Atlantis.", DEFAULT_COLOR)));
                components.add(getPlayerDisplayName(player)
                        .append(Component.text(" wurde von einem Ertrunkenem getötet!", DEFAULT_COLOR)));
                components
                        .add(Component.text("Ein Ertrunkener hat ", DEFAULT_COLOR).append(getPlayerDisplayName(player))
                                .append(Component.text(" aus dem Leben gerissen!", DEFAULT_COLOR)));
                components
                        .add(Component.text("Ein Ertrunkener hat ", DEFAULT_COLOR).append(getPlayerDisplayName(player))
                                .append(Component.text(" in die Tiefe gezogen!", DEFAULT_COLOR)));
            } else if (killer instanceof Zombie) {
                components.add(getPlayerDisplayName(player)
                        .append(Component.text("'s Gehirn wird gerade von einem Zombie gefressen!", DEFAULT_COLOR)));
                components.add(getPlayerDisplayName(player)
                        .append(Component.text(" wurde von einem Zombie getötet!", DEFAULT_COLOR)));
                components.add(getPlayerDisplayName(player)
                        .append(Component.text(" ist heute das Mittagessen.", DEFAULT_COLOR)));
                components.add(Component.text("Ein Zombie hat ", DEFAULT_COLOR).append(getPlayerDisplayName(player))
                        .append(Component.text(" zerrissen!", DEFAULT_COLOR)));
            } else if (killer instanceof Skeleton) {
                components.add(getPlayerDisplayName(player)
                        .append(Component.text(" wurde von einem Skelett getötet!", DEFAULT_COLOR)));
            } else if (killer instanceof WitherSkeleton) {
                components.add(getPlayerDisplayName(player)
                        .append(Component.text(" wurde von einem Wither Skelett getötet!", DEFAULT_COLOR)));
                components.add(getPlayerDisplayName(player)
                        .append(Component.text(" hat sich mit einem Wither Skelett angelegt.", DEFAULT_COLOR)));
                components.add(
                        Component.text("Ein Wither Skelett hat ", DEFAULT_COLOR).append(getPlayerDisplayName(player))
                                .append(Component.text(" aus dem Leben gerissen!", DEFAULT_COLOR)));
            } else if (killer instanceof Spider) {
                components.add(getPlayerDisplayName(player)
                        .append(Component.text(" wurde von einer Spinne erledigt!", DEFAULT_COLOR)));
                components.add(getPlayerDisplayName(player)
                        .append(Component.text(" wurde in einem Spinnennetz gefangen.", DEFAULT_COLOR)));
                components.add(Component.text("Eine Spinne hat ", DEFAULT_COLOR).append(getPlayerDisplayName(player))
                        .append(Component.text(" eingefangen.", DEFAULT_COLOR)));
                components.add(Component.text("Eine Spinne hat ", DEFAULT_COLOR).append(getPlayerDisplayName(player))
                        .append(Component.text(" getötet.", DEFAULT_COLOR)));
            } else if (killer instanceof Wolf) {
                components.add(getPlayerDisplayName(player)
                        .append(Component.text(" wurde von einem Wolf zerfleischt!", DEFAULT_COLOR)));
                components.add(Component.text("Ein Wolf ist über ", DEFAULT_COLOR).append(getPlayerDisplayName(player))
                        .append(Component.text(" hergefallen.", DEFAULT_COLOR)));
                components.add(Component.text("Des Menschen bester Freund? Offenbar nicht der von ", DEFAULT_COLOR)
                        .append(getPlayerDisplayName(player)));
            } else if (killer instanceof Blaze) {
                components.add(getPlayerDisplayName(player)
                        .append(Component.text(" wurde von einer Lohe in Brand gesteckt.", DEFAULT_COLOR)));
                components.add(Component.text("Eine Lohe hat ", DEFAULT_COLOR).append(getPlayerDisplayName(player))
                        .append(Component.text(" geröstet.", DEFAULT_COLOR)));
            } else if (killer instanceof Bee) {
                components.add(getPlayerDisplayName(player)
                        .append(Component.text(" wird sich in Zukunft von Bienen fernhalten.", DEFAULT_COLOR)));
                components.add(getPlayerDisplayName(player)
                        .append(Component.text(" wollte sich am Honig vergreifen.", DEFAULT_COLOR)));
                components.add(Component.text("Eine Biene wollte ihren Honig nicht mit ", DEFAULT_COLOR)
                        .append(getPlayerDisplayName(player)).append(Component.text(" teilen.", DEFAULT_COLOR)));
                components.add(Component.text("Die freche Biene Maja hat ", DEFAULT_COLOR)
                        .append(getPlayerDisplayName(player)).append(Component.text(" zerstochen.", DEFAULT_COLOR)));
            } else if (killer instanceof Enderman) {
                components.add(getPlayerDisplayName(player)
                        .append(Component.text(" wurde von einem Enderman getötet.", DEFAULT_COLOR)));
                components.add(getPlayerDisplayName(player)
                        .append(Component.text(" starrte einen Enderman zu lange an.", DEFAULT_COLOR)));
                components.add(Component.text("Ein Enderman hat ", DEFAULT_COLOR).append(getPlayerDisplayName(player))
                        .append(Component.text(" Kopf gestohlen.", DEFAULT_COLOR)));
                components.add(Component.text("Ein Enderman hat ", DEFAULT_COLOR).append(getPlayerDisplayName(player))
                        .append(Component.text(" erledigt.", DEFAULT_COLOR)));
                components.add(Component.text("Der Mann in Schwarz. Er kam auch ", DEFAULT_COLOR)
                        .append(getPlayerDisplayName(player)).append(Component.text(" holen.", DEFAULT_COLOR)));
            } else if (killer instanceof IronGolem) {
                components.add(getPlayerDisplayName(player)
                        .append(Component.text(" wurde von einem Eisengolem zermatscht!", DEFAULT_COLOR)));
                components.add(getPlayerDisplayName(player).append(
                        Component.text(" wird Eisengolems in Zukunft mit mehr Respekt begegnen.", DEFAULT_COLOR)));
                components.add(Component.text("Ein Eisengolem vernichtete ", DEFAULT_COLOR)
                        .append(getPlayerDisplayName(player)).append(Component.text("!", DEFAULT_COLOR)));
                components.add(Component.text("Der Eisengolem hatte wohl keine Rose für ", DEFAULT_COLOR)
                        .append(getPlayerDisplayName(player)).append(Component.text("!", DEFAULT_COLOR)));
            } else if (killer instanceof Panda) {
                components.add(getPlayerDisplayName(player)
                        .append(Component.text(" wurde von einem Panda in die Schranken gewiesen.", DEFAULT_COLOR)));
                components.add(Component.text("So süß und doch so tödlich. ", DEFAULT_COLOR)
                        .append(getPlayerDisplayName(player))
                        .append(Component.text(" wird sich einem Panda nicht mehr nähern.", DEFAULT_COLOR)));
            } else if (killer instanceof MagmaCube) {
                components.add(getPlayerDisplayName(player)
                        .append(Component.text(" wurde von einem MagmaCube zermalmt.", DEFAULT_COLOR)));
                components.add(Component.text("Ein MagmaCube hat ", DEFAULT_COLOR).append(getPlayerDisplayName(player))
                        .append(Component.text(" zerquetscht!", DEFAULT_COLOR)));
            } else if (killer instanceof Slime) {
                components.add(getPlayerDisplayName(player)
                        .append(Component.text(" wurde vom Glibber aufgesaugt.", DEFAULT_COLOR)));
                components.add(getPlayerDisplayName(player)
                        .append(Component.text(" hat das Wort einschleimen wörtlich genommen", DEFAULT_COLOR)));
                components.add(getPlayerDisplayName(player)
                        .append(Component.text(" hat den Wackelpudding unterschätzt.", DEFAULT_COLOR)));
                components.add(getPlayerDisplayName(player)
                        .append(Component.text(" steht eh mehr auf rote Grütze.", DEFAULT_COLOR)));
                components.add(getPlayerDisplayName(player)
                        .append(Component.text(" wurde absorbiert.", DEFAULT_COLOR)));
                components.add(Component.text("Ein Schwabbel hat ", DEFAULT_COLOR).append(getPlayerDisplayName(player))
                        .append(Component.text(" zerquetscht!", DEFAULT_COLOR)));
            } else if (killer instanceof PolarBear) {
                components.add(getPlayerDisplayName(player)
                        .append(Component.text(" wurde von einem Eisbär aufgeschlitzt!", DEFAULT_COLOR)));
                components.add(getPlayerDisplayName(player)
                        .append(Component.text(" hat die Stärke eines Eisbären unterschätzt.", DEFAULT_COLOR)));
                components.add(Component.text("Ein Eisbär hat ", DEFAULT_COLOR).append(getPlayerDisplayName(player))
                        .append(Component.text(" zerfleischt!", DEFAULT_COLOR)));
            } else if (killer instanceof PufferFish) {
                components.add(getPlayerDisplayName(player).append(Component
                        .text(" näherte sich einem Kugelfisch und bekam seine Stacheln zu spüren.", DEFAULT_COLOR)));
                components.add(getPlayerDisplayName(player)
                        .append(Component.text(" wurde von einem Kugelfisch vergiftet.", DEFAULT_COLOR)));
                components.add(Component.text("Ein Kugelfisch hat ", DEFAULT_COLOR).append(getPlayerDisplayName(player))
                        .append(Component.text(" in der Himmel befördert.", DEFAULT_COLOR)));
            } else if (killer instanceof Ravager) {
                components.add(getPlayerDisplayName(player)
                        .append(Component.text(" wurde von einem Verwüster zertreten!", DEFAULT_COLOR)));
                components.add(getPlayerDisplayName(player)
                        .append(Component.text(" wurde von einem Verwüster vernichtet!", DEFAULT_COLOR)));
                components.add(Component.text("Ein Verwüster hat ", DEFAULT_COLOR).append(getPlayerDisplayName(player))
                        .append(Component.text(" dem Erdboden gleich gemacht.", DEFAULT_COLOR)));
                components.add(Component.text("Ein Verwüster hat ", DEFAULT_COLOR).append(getPlayerDisplayName(player))
                        .append(Component.text(" zertrampelt!", DEFAULT_COLOR)));
            } else if (killer instanceof Evoker) {
                components.add(getPlayerDisplayName(player)
                        .append(Component.text(" wurde von einem Magier erledigt.", DEFAULT_COLOR)));
                components.add(Component.text("Ein Magier hat ", DEFAULT_COLOR).append(getPlayerDisplayName(player))
                        .append(Component.text(" verzaubert, aber wohl nicht im guten Sinne!", DEFAULT_COLOR)));
                components.add(Component.text("Ein Magier hat sich um ", DEFAULT_COLOR)
                        .append(getPlayerDisplayName(player)).append(Component.text(" gekümmert!", DEFAULT_COLOR)));
            } else if (killer instanceof Illusioner) {
                components.add(getPlayerDisplayName(player)
                        .append(Component.text(" wurde von einem Illusionist aus dem Leben gerissen!", DEFAULT_COLOR)));
                components
                        .add(Component.text("Ein Illusionist hat ", DEFAULT_COLOR).append(getPlayerDisplayName(player))
                                .append(Component.text(" den Rest gegeben!", DEFAULT_COLOR)));
            } else if (killer instanceof Vindicator) {
                components.add(getPlayerDisplayName(player)
                        .append(Component.text(" wurde von einem Vindicator ausradiert.", DEFAULT_COLOR)));
                components.add(Component.text("Ein Vindicator hat ", DEFAULT_COLOR).append(getPlayerDisplayName(player))
                        .append(Component.text(" gezeigt, wo der der Hammer hängt.", DEFAULT_COLOR)));
            } else if (killer instanceof Raider) {
                components.add(getPlayerDisplayName(player)
                        .append(Component.text(" ist einer Pillager Patrouille zum Opfer gefallen.", DEFAULT_COLOR)));
                components.add(getPlayerDisplayName(player)
                        .append(Component.text(" wurde von einem Pillager getötet.", DEFAULT_COLOR)));
                components.add(Component.text("Ein Pillager nimmt keine Gefangenen, ", DEFAULT_COLOR)
                        .append(getPlayerDisplayName(player)).append(Component.text("!", DEFAULT_COLOR)));
            } else if (killer instanceof Piglin) {
                components.add(getPlayerDisplayName(player)
                        .append(Component.text(" hätte besser Gold als Bestechung mitnehmen sollen.", DEFAULT_COLOR)));
                components.add(getPlayerDisplayName(player)
                        .append(Component.text(" wurde von einem Piglin erledigt.", DEFAULT_COLOR)));
                components.add(Component.text("Ein Piglin verging sich an ", DEFAULT_COLOR)
                        .append(getPlayerDisplayName(player)).append(Component.text("!", DEFAULT_COLOR)));
            } else if (killer instanceof PiglinBrute) {
                components.add(getPlayerDisplayName(player)
                        .append(Component.text(" weiß nun, eine Piglin Brute nicht zu unterschätzen.", DEFAULT_COLOR)));
                components.add(getPlayerDisplayName(player)
                        .append(Component.text("'s Kopf wurde mit einer Goldaxt gespalten.", DEFAULT_COLOR)));
                components.add(getPlayerDisplayName(player)
                        .append(Component.text(" wurde von einnem Piglin erledigt.", DEFAULT_COLOR)));
                components.add(Component.text("Die Piglin Brute war zu viel für ", DEFAULT_COLOR)
                        .append(getPlayerDisplayName(player)).append(Component.text("!", DEFAULT_COLOR)));
            } else if (killer instanceof Phantom) {
                components.add(getPlayerDisplayName(player)
                        .append(Component.text(" hätte lieber schlafen sollen.", DEFAULT_COLOR)));
                components.add(getPlayerDisplayName(player)
                        .append(Component.text(" dachte es wäre nur ein Vogel.", DEFAULT_COLOR)));
                components.add(Component.text("Ein Phantom hat sich um ", DEFAULT_COLOR)
                        .append(getPlayerDisplayName(player)).append(Component.text(" gekümmert!", DEFAULT_COLOR)));
            } else if (killer instanceof Hoglin) {
                components.add(getPlayerDisplayName(player)
                        .append(Component.text(" wurde von einem Hoglin zerstampft.", DEFAULT_COLOR)));
                components.add(Component.text("Ein Hoglin hat ", DEFAULT_COLOR).append(getPlayerDisplayName(player))
                        .append(Component.text(" zertrampelt!", DEFAULT_COLOR)));
            } else if (killer instanceof Zoglin) {
                components.add(getPlayerDisplayName(player)
                        .append(Component.text(" wurde von einem Zoglin geplättet.", DEFAULT_COLOR)));
                components.add(Component.text("Ein Zoglin hat ", DEFAULT_COLOR).append(getPlayerDisplayName(player))
                        .append(Component.text(" plattgewalzt!", DEFAULT_COLOR)));
            } else if (killer instanceof Vex) {
                components.add(getPlayerDisplayName(player)
                        .append(Component.text(" wurde von einem Plagegeist zerteilt.", DEFAULT_COLOR)));
                components.add(Component.text("Ein Plagegeist hat ", DEFAULT_COLOR).append(getPlayerDisplayName(player))
                        .append(Component.text(" ausgeschaltet!", DEFAULT_COLOR)));
                components.add(Component.text("Eine gute Fee? Oh nein, es nur nur ein Plagegeist ", DEFAULT_COLOR)
                        .append(getPlayerDisplayName(player)).append(Component.text("!", DEFAULT_COLOR)));
            } else if (killer instanceof Silverfish) {
                components.add(getPlayerDisplayName(player)
                        .append(Component.text(" wurde von einem Silberfisch terminiert.", DEFAULT_COLOR)));
                components.add(getPlayerDisplayName(player)
                        .append(Component.text(" hat eine zu feuchte Toilette.", DEFAULT_COLOR)));
                components
                        .add(Component.text("Ein Silberfisch hat ", DEFAULT_COLOR).append(getPlayerDisplayName(player))
                                .append(Component.text(" zu Tode erschreckt.", DEFAULT_COLOR)));
            } else if (killer instanceof Endermite) {
                components.add(getPlayerDisplayName(player)
                        .append(Component.text(" wurde von einer Endermite getötet.", DEFAULT_COLOR)));
                components.add(
                        Component.text("Ein Ratte aus dem End? ", DEFAULT_COLOR).append(getPlayerDisplayName(player))
                                .append(Component.text(" hat das gereicht.", DEFAULT_COLOR)));
            } else if (killer instanceof EnderDragon) {
                components.add(getPlayerDisplayName(player)
                        .append(Component.text(" wurde vom EnderDragon getötet!", DEFAULT_COLOR)));
                components.add(getPlayerDisplayName(player)
                        .append(Component.text(" wurde vom Drachen erledigt!", DEFAULT_COLOR)));
                components.add(getPlayerDisplayName(player)
                        .append(Component.text(" hatte keine Chance gegen den Drachen.", DEFAULT_COLOR)));
                components.add(Component.text("Ein Drache hat ", DEFAULT_COLOR).append(getPlayerDisplayName(player))
                        .append(Component.text(" gefressen!", DEFAULT_COLOR)));
            } else if (killer instanceof Wither) {
                components.add(getPlayerDisplayName(player)
                        .append(Component.text(" wurde vom Wither getötet!", DEFAULT_COLOR)));
                components.add(getPlayerDisplayName(player)
                        .append(Component.text(" wurde vom Wither ausradiert!", DEFAULT_COLOR)));
                components.add(getPlayerDisplayName(player)
                        .append(Component.text(" hatte keine Chance gegen den Wither.", DEFAULT_COLOR)));
                components.add(getPlayerDisplayName(player)
                        .append(Component.text(" wurde vom Wither zerbombt!", DEFAULT_COLOR)));
                components.add(Component.text("Der Wither hat ", DEFAULT_COLOR).append(getPlayerDisplayName(player))
                        .append(Component.text(" den Rest gegeben!", DEFAULT_COLOR)));
            } else if (killer instanceof Warden) {
                components
                        .add(getPlayerDisplayName(player).append(Component.text(" war wohl zu laut.", DEFAULT_COLOR)));
                components.add(getPlayerDisplayName(player)
                        .append(Component.text(" wurde vom Warden in den Abgrund gezogen", DEFAULT_COLOR)));
                components.add(getPlayerDisplayName(player)
                        .append(Component.text(" sollte nächstes mal leiser sein.", DEFAULT_COLOR)));
                components.add(Component.text("Der Warden hat ", DEFAULT_COLOR).append(getPlayerDisplayName(player))
                        .append(Component.text(" zu recht gewiesen!", DEFAULT_COLOR)));
            }
        }
    }
}
