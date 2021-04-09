package me.peridot.peridrop.schedulers;

import me.peridot.peridrop.PeriDrop;

public class RankingUpdateScheduler {

    private final PeriDrop plugin;

    public RankingUpdateScheduler(PeriDrop plugin) {
        this.plugin = plugin;
    }

    public void start() {
        this.plugin.getServer().getScheduler().runTaskTimerAsynchronously(this.plugin, () -> {
            this.plugin.getDatabaseManager().getRankDatabase().loadRanks();
        }, 0, 20 * this.plugin.getPluginConfiguration().getInt("tasks.ranking-update"));
    }

}
