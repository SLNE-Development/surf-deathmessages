package dev.slne.surfDeathMessagesNew.deathmessages

import dev.slne.surf.surfapi.core.api.messages.Colors
import dev.slne.surf.surfapi.core.api.messages.adventure.buildText
import dev.slne.surf.surfapi.core.api.messages.builder.SurfComponentBuilder
import dev.slne.surf.surfapi.core.api.util.mutableObject2ObjectMapOf
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextReplacementConfig
import org.bukkit.entity.*
import org.bukkit.event.entity.EntityDamageEvent.DamageCause

object DeathMessageProvider {
    private val genericMessages = mutableObject2ObjectMapOf(
        DamageCause.FALL to listOf(
            buildText {
                appendDeathPrefix()
                variableValue("<player>")
                spacer(" ist auf die harte Tour gelandet.")
            },
            buildText {
                appendDeathPrefix()
                variableValue("<player>")
                spacer(" hat die Höhe unterschätzt.")
            }
        ),
        DamageCause.VOID to listOf(
            buildText {
                appendDeathPrefix()
                variableValue("<player>")
                spacer(" fällt nun für immer!")
            },
            buildText {
                appendDeathPrefix()
                variableValue("<player>")
                spacer(" ist im Nichts verschwunden!")
            },
            buildText {
                appendDeathPrefix()
                variableValue("<player>")
                spacer(" wird den Boden wohl nie erreichen.")
            }
        ),
        DamageCause.THORNS to listOf(
            buildText {
                appendDeathPrefix()
                variableValue("<player>")
                spacer(" hat sich an einer Rüstung zu Tode gepikst!")
            }
        ),
        DamageCause.SUICIDE to listOf(
            buildText {
                appendDeathPrefix()
                variableValue("<player>")
                spacer(" hat sich das Leben genommen!")
            }
        ),
        DamageCause.SUFFOCATION to listOf(
            buildText {
                appendDeathPrefix()
                variableValue("<player>")
                spacer(" wurde lebendig begraben!")
            },
            buildText {
                appendDeathPrefix()
                variableValue("<player>")
                spacer(" muss nicht mehr begraben werden.")
            }
        ),
        DamageCause.STARVATION to listOf(
            buildText {
                appendDeathPrefix()
                variableValue("<player>")
                spacer(" ist verhungert!")
            },
            buildText {
                appendDeathPrefix()
                variableValue("<player>")
                spacer(" übertrieb es mit der Diät.")
            }
        ),
        DamageCause.POISON to listOf(
            buildText {
                appendDeathPrefix()
                variableValue("<player>")
                spacer(" starb an einer Vergiftung!")
            },
            buildText {
                appendDeathPrefix()
                variableValue("<player>")
                spacer(" hat wohl nicht auf das Verfallsdatum geachtet.")
            }
        ),
        DamageCause.MAGIC to listOf(
            buildText {
                appendDeathPrefix()
                variableValue("<player>")
                spacer(" wurde durch Magie getötet!")
            },
            buildText {
                appendDeathPrefix()
                variableValue("<player>")
                spacer(" hat den falschen Trank getrunken.")
            }
        ),
        DamageCause.LIGHTNING to listOf(
            buildText {
                appendDeathPrefix()
                variableValue("<player>")
                spacer(" hat Bekanntschaft mit dem ohmschen Gesetz gemacht!")
            },
            buildText {
                appendDeathPrefix()
                variableValue("<player>")
                spacer(" wurde vom Blitz erschlagen!")
            }
        ),
        DamageCause.LAVA to listOf(
            buildText {
                appendDeathPrefix()
                variableValue("<player>")
                spacer(" verwechselte Lava mit dem kühlen Nass!")
            },
            buildText {
                appendDeathPrefix()
                variableValue("<player>")
                spacer(" wollte einen Vulkan von innen erkunden.")
            }
        ),
        DamageCause.FIRE to listOf(
            buildText {
                appendDeathPrefix()
                variableValue("<player>")
                spacer(" brannte wie Zunder!")
            },
            buildText {
                appendDeathPrefix()
                variableValue("<player>")
                spacer(" ging in Flammen auf!")
            }
        ),
        DamageCause.FIRE_TICK to listOf(
            buildText {
                appendDeathPrefix()
                variableValue("<player>")
                spacer(" ist nur noch ein Häufchen Asche!")
            }
        ),
        DamageCause.DROWNING to listOf(
            buildText {
                appendDeathPrefix()
                variableValue("<player>")
                spacer(" ist abgesoffen.")
            },
            buildText {
                appendDeathPrefix()
                variableValue("<player>")
                spacer(" schwimmt nun mit den Fischen.")
            }
        ),
        DamageCause.CRAMMING to listOf(
            buildText {
                appendDeathPrefix()
                variableValue("<player>")
                spacer(" wurde in der Masse zerdrückt!")
            }
        ),
        DamageCause.DRAGON_BREATH to listOf(
            buildText {
                appendDeathPrefix()
                spacer("Ein Drache hat ")
                variableValue("<player>")
                spacer(" geröstet.")
            }
        ),
        DamageCause.CONTACT to listOf(
            buildText {
                appendDeathPrefix()
                variableValue("<player>")
                spacer(" wurde zu Tode gestochen!")
            }
        ),
        DamageCause.FREEZE to listOf(
            buildText {
                appendDeathPrefix()
                variableValue("<player>")
                spacer(" hat seinen Mantel vergessen!")
            }
        ), DamageCause.WITHER to listOf(
            buildText {
                appendDeathPrefix()
                variableValue("<player>")
                spacer(" wurde vom Verfall verschlungen!")
            },
            buildText {
                appendDeathPrefix()
                variableValue("<player>")
                spacer(" ist verwelkt.")
            },
            buildText {
                appendDeathPrefix()
                variableValue("<player>")
                spacer(" ist innerlich zerbröselt.")
            }
        ), DamageCause.SONIC_BOOM to listOf(
            buildText {
                appendDeathPrefix()
                variableValue("<player>")
                spacer(" wurde von einem Laser durchbohrt!")
            },
            buildText {
                appendDeathPrefix()
                variableValue("<player>")
                spacer(" wurde verstrahlt!")
            }
        )
    )

    private val entityMessages = mutableObject2ObjectMapOf(
        Creeper::class to listOf(
            buildText {
                appendDeathPrefix()
                variableValue("<player>")
                spacer(" wurde von einem Creeper zerfetzt!")
            },
            buildText {
                appendDeathPrefix()
                variableValue("<player>")
                spacer(" wurde von einem Creeper in tausend Teile zerfetzt.")
            }
        ),
        Zombie::class to listOf(
            buildText {
                appendDeathPrefix()
                variableValue("<player>")
                spacer(" wurde von einem Zombie getötet!")
            },
            buildText {
                appendDeathPrefix()
                variableValue("<player>")
                spacer("'s Gehirn wird gerade von einem Zombie gefressen!")
            }
        ),
        Skeleton::class to listOf(
            buildText {
                appendDeathPrefix()
                variableValue("<player>")
                spacer(" wurde von einem Skelett getötet!")
            }
        ),
        Wither::class to listOf(
            buildText {
                appendDeathPrefix()
                variableValue("<player>")
                spacer(" wurde vom Wither getötet!")
            },
            buildText {
                appendDeathPrefix()
                variableValue("<player>")
                spacer(" hatte wohl nicht die Ausrüstung um gegen den Wither zu kämpfen.")
            }
        ),
        WitherSkeleton::class to listOf(
            buildText {
                appendDeathPrefix()
                variableValue("<player>")
                spacer(" wurde von einem Wither Skelett getötet!")
            },
            buildText {
                appendDeathPrefix()
                variableValue("<player>")
                spacer(" ist verfault.")
            }
        ),
        EnderDragon::class to listOf(
            buildText {
                appendDeathPrefix()
                variableValue("<player>")
                spacer(" wurde vom EnderDragon getötet!")
            },
            buildText {
                appendDeathPrefix()
                variableValue(playerPlaceHolder)
                spacer(" dachte, er könnte auf einem Drachen fliegen")
            }
        ),
        Player::class to listOf(
            buildText {
                appendDeathPrefix()
                variableValue("<player>")
                spacer(" wurde von ")
                variableValue("<killer>")
                spacer(" ermordet!")
            }
        ),
        Skeleton::class to listOf(
            buildText {
                appendDeathPrefix()
                variableValue("<player>")
                spacer(" wurde von einem Stray durchlöchert!")
            }
        ),
        Ghast::class to listOf(
            buildText {
                appendDeathPrefix()
                variableValue("<player>")
                spacer(" wurde vom Ghast getroffen!")
            }
        ),
        TNTPrimed::class to listOf(
            buildText {
                appendDeathPrefix()
                variableValue("<player>")
                spacer(" hat den Böller nicht rechtzeitig weggeworfen.")
            }
        ),
        Spider::class to listOf(
            buildText {
                appendDeathPrefix()
                variableValue("<player>")
                spacer(" wurde von einer Spinne erledigt!")
            }
        ),
        Wolf::class to listOf(
            buildText {
                appendDeathPrefix()
                variableValue("<player>")
                spacer(" wurde von einem Wolf zerfleischt!")
            }
        ),
        Blaze::class to listOf(
            buildText {
                appendDeathPrefix()
                variableValue("<player>")
                spacer(" wurde von einer Lohe in Brand gesteckt.")
            }
        ),
        Bee::class to listOf(
            buildText {
                appendDeathPrefix()
                variableValue("<player>")
                spacer(" wird sich in Zukunft von Bienen fernhalten.")
            }
        ),
        Enderman::class to listOf(
            buildText {
                appendDeathPrefix()
                variableValue("<player>")
                spacer(" wurde von einem Enderman getötet.")
            }
        ),
        IronGolem::class to listOf(
            buildText {
                appendDeathPrefix()
                variableValue("<player>")
                spacer(" wurde von einem Eisengolem zermatscht!")
            }
        ),
        Ravager::class to listOf(
            buildText {
                appendDeathPrefix()
                variableValue("<player>")
                spacer(" wurde von einem Verwüster zertreten!")
            }
        ),
        Piglin::class to listOf(
            buildText {
                appendDeathPrefix()
                variableValue("<player>")
                spacer(" wurde von einem Piglin erledigt.")
            }
        ),
        Phantom::class to listOf(
            buildText {
                appendDeathPrefix()
                variableValue("<player>")
                spacer(" hätte lieber schlafen sollen.")
            }
        ),
        Hoglin::class to listOf(
            buildText {
                appendDeathPrefix()
                variableValue("<player>")
                spacer(" wurde von einem Hoglin zerstampft.")
            }
        ),
        Creaking::class to listOf(
            buildText {
                appendDeathPrefix()
                variableValue("<player>")
                spacer(" wurde von knarrendem Holz erschreckt!")
            },
            buildText {
                appendDeathPrefix()
                variableValue("<player>")
                spacer(" hat dem kreischenden Knarren Tribut gezollt!")
            },
            buildText {
                appendDeathPrefix()
                variableValue("<player>")
                spacer(" wurde zu Brennholz verarbeitet!")
            }
        )
    )

    private val DEFAULT_MESSAGE = buildText {
        appendDeathPrefix()
        variableValue("<player>")
        spacer(" ist gestorben.")
    }

    private fun getMessage(cause: DamageCause?, killer: Entity?): Component {
        val entityMessage = killer
            ?.let { kr ->
                entityMessages.entries
                    .firstNotNullOfOrNull { (clazz, templates) -> if (clazz.isInstance(kr)) templates.random() else null }
            }
        return entityMessage ?: genericMessages[cause]?.random() ?: DEFAULT_MESSAGE
    }

    fun getDeathMessageComponent(
        player: Player,
        cause: DamageCause?,
        killer: Entity?
    ): Component {
        var deathMessage = getMessage(cause, killer)
        deathMessage = deathMessage.replaceText(
            TextReplacementConfig.builder().matchLiteral("<player>").replacement(player.displayName()).build()
        )

        killer?.let { kr ->
            val killerName = when (kr) {
                is Player -> kr.displayName()
                else -> Component.translatable(kr.type.translationKey()).colorIfAbsent(Colors.VARIABLE_VALUE)
            }
            deathMessage = deathMessage.replaceText(
                TextReplacementConfig.builder().matchLiteral("<killer>").replacement(killerName).build()
            )
        }

        return deathMessage
    }

    private fun SurfComponentBuilder.appendDeathPrefix() = append {
        spacer("[")
        error("💀")
        spacer("]")
        appendSpace()
    }
}
