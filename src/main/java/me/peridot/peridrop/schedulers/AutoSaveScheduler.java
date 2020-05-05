package me.peridot.peridrop.schedulers;

import me.peridot.peridrop.PeriDrop;
import org.bukkit.Bukkit;

public class AutoSaveScheduler {

    private final PeriDrop plugin;

    public AutoSaveScheduler(PeriDrop plugin) {
        this.plugin = plugin;
    }

    public void start() {
        Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, () -> {
            plugin.getDatabaseManager().getUserDatabase().saveUsersAsync(plugin.getUserCache().getModifiedUsers());
        }, 0, 20 * plugin.getConfigurations().getPluginConfiguration().getInt("tasks.autosave"));
    }

}
