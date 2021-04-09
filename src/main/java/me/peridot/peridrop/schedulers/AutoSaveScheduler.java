package me.peridot.peridrop.schedulers;

import me.peridot.peridrop.PeriDrop;

public class AutoSaveScheduler {

    private final PeriDrop plugin;

    public AutoSaveScheduler(PeriDrop plugin) {
        this.plugin = plugin;
    }

    public void start() {
        this.plugin.getServer().getScheduler().runTaskTimerAsynchronously(this.plugin, () -> {
            this.plugin.getDatabaseManager().getUserDatabase().saveUsers(this.plugin.getUserCache().getModifiedUsers());
        }, 0, 20 * this.plugin.getPluginConfiguration().getInt("tasks.autosave"));
    }

}
