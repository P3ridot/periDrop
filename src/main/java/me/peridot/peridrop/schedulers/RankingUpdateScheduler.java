package me.peridot.peridrop.schedulers;

import me.peridot.peridrop.PeriDrop;

public class RankingUpdateScheduler {

    private final PeriDrop plugin;

    public RankingUpdateScheduler(PeriDrop plugin) {
        this.plugin = plugin;
    }

    public void start() {
        plugin.getServer().getScheduler().runTaskTimerAsynchronously(plugin, () -> {
            plugin.getDatabaseManager().getRankDatabase().loadRanks();
        }, 0, 20 * plugin.getPluginConfiguration().getInt("tasks.ranking-update"));
    }

}
