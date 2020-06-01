package me.peridot.peridrop.schedulers;

import me.peridot.peridrop.PeriDrop;

public class AutoSaveScheduler {

    private final PeriDrop plugin;

    public AutoSaveScheduler(PeriDrop plugin) {
        this.plugin = plugin;
    }

    public void start() {
        plugin.getServer().getScheduler().runTaskTimerAsynchronously(plugin, () -> {
            plugin.getDatabaseManager().getUserDatabase().saveUsers(plugin.getUserCache().getModifiedUsers());
        }, 0, 20 * plugin.getConfigurations().getPluginConfiguration().getInt("tasks.autosave"));
    }

}
