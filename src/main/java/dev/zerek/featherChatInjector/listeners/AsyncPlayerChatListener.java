package dev.zerek.FeatherChatInjector.listeners;

import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class AsyncPlayerChatListener implements Listener {
    private static final Pattern PLACEHOLDER_PATTERN = Pattern.compile("\\{([^{}]+)}", Pattern.CASE_INSENSITIVE);
    private static final char COLOR_CHAR = '§';

    private final Logger logger = Bukkit.getLogger();

    public @SuppressWarnings("deprecation") void onChat(AsyncPlayerChatEvent event) {
        final String format = event.getFormat();

        if (!format.contains("{")) {
            event.setFormat(translate(PlaceholderAPI.setPlaceholders(event.getPlayer(), format)));
            return;
        }

        final Matcher matcher = PLACEHOLDER_PATTERN.matcher(format);
        final StringBuilder result = new StringBuilder();

        while (matcher.find()) {
            final String placeholder = matcher.group(1).trim();
            matcher.appendReplacement(result, "%" + placeholder + "%");
        }
        matcher.appendTail(result);

        String processedFormat = PlaceholderAPI.setPlaceholders(event.getPlayer(), result.toString());
        event.setFormat(translate(processedFormat));
    }

    private String translate(String message) {
        return translateHexColorCodes(message);
    }

    private String translateHexColorCodes(String message) {
        final Pattern hexPattern = Pattern.compile("<#([A-Fa-f0-9]{6})>");
        Matcher matcher = hexPattern.matcher(message);
        StringBuilder buffer = new StringBuilder(message.length() + 4 * 8);

        while (matcher.find()) {
            String group = matcher.group(1);
            matcher.appendReplacement(buffer, COLOR_CHAR + "x"
                    + COLOR_CHAR + group.charAt(0) + COLOR_CHAR + group.charAt(1)
                    + COLOR_CHAR + group.charAt(2) + COLOR_CHAR + group.charAt(3)
                    + COLOR_CHAR + group.charAt(4) + COLOR_CHAR + group.charAt(5)
            );
        }
        return matcher.appendTail(buffer).toString();
    }
}
