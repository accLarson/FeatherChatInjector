package dev.zerek.FeatherChatInjector;

import dev.zerek.FeatherChatInjector.listeners.AsyncPlayerChatListener;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.EventExecutor;
import org.bukkit.plugin.java.JavaPlugin;

public final class FeatherChatInjector extends JavaPlugin {

    @Override
    public void onEnable() {
        saveDefaultConfig();
        EventPriority eventPriority = EventPriority.valueOf(getConfig().getString("event-listener-priority", "LOW").toUpperCase());
        AsyncPlayerChatListener chatListener = new AsyncPlayerChatListener();
        EventExecutor executor = (l, e) -> ((AsyncPlayerChatListener)l).onChat((AsyncPlayerChatEvent)e);
        getServer().getPluginManager().registerEvent(AsyncPlayerChatEvent.class, chatListener, eventPriority, executor, this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
