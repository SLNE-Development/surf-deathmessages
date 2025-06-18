package dev.slne.deathmessage.message;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;

public class MessageManager {

    public static final TextColor PRIMARY = TextColor.fromHexString("#3b92d1");
    public static final TextColor SECONDARY = TextColor.fromHexString("#5b5b5b");

    public static final TextColor INFO = TextColor.fromHexString("#40d1db");
    public static final TextColor SUCCESS = TextColor.fromHexString("#65ff64");
    public static final TextColor WARNING = TextColor.fromHexString("#f9c353");
    public static final TextColor ERROR = TextColor.fromHexString("#ee3d51");

    public static final TextColor VARIABLE_KEY = MessageManager.INFO;
    public static final TextColor VARIABLE_VALUE = MessageManager.WARNING;
    public static final TextColor SPACER = NamedTextColor.GRAY;
    public static final TextColor DARK_SPACER = NamedTextColor.DARK_GRAY;

    /**
     * Private constructor to hide the implicit public one.
     */
    private MessageManager() {

    }

    /**
     * Returns a prefix for the plugin.
     *
     * @return The prefix for the plugin.
     */
    public static Component prefix() {
        TextComponent.Builder builder = Component.text();

        builder.append(Component.text(">> ", NamedTextColor.DARK_GRAY));
        builder.append(Component.text("Surf", MessageManager.PRIMARY));
        builder.append(Component.text(" | ", NamedTextColor.DARK_GRAY));

        return builder.build();
    }

    /**
     * Appends the header to the builder
     *
     * @param builder The builder to append the header to
     */
    private static void appendHeader(TextComponent.Builder builder) {
        builder.append(Component.text("CASTCRAFTER", PRIMARY));
        builder.append(Component.newline());
        builder.append(Component.text("COMMUNITY", PRIMARY));
        builder.append(Component.newline());
        builder.append(Component.newline());
    }

    /**
     * Formats the given seconds into a component
     *
     * @param seconds The seconds to format
     * @return The formatted component
     */
    public static Component formatSeconds(long seconds) {
        return formatSeconds(seconds, SPACER, VARIABLE_VALUE);
    }

    /**
     * Formats the given seconds into a component
     *
     * @param seconds     The seconds to format
     * @param spacerColor The color of the spacer
     * @param timeColor   The color of the time
     * @return The formatted component
     */
    public static Component formatSeconds(long seconds, TextColor spacerColor, TextColor timeColor) {
        TextComponent.Builder builder = Component.text();

        final int hours = (int) (seconds / 3600);
        final int minutes = (int) ((seconds % 3600) / 60);
        final int secondsLeft = (int) (seconds % 60);

        String formatter = "%02d";

        builder.append(Component.text(String.format(formatter, hours), timeColor));
        builder.append(Component.text(":", spacerColor));

        builder.append(Component.text(String.format(formatter, minutes), timeColor));
        builder.append(Component.text(":", spacerColor));

        builder.append(Component.text(String.format(formatter, secondsLeft), timeColor));
        return builder.build();
    }
}
