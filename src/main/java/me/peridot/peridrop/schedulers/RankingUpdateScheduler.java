package me.peridot.peridrop.schedulers;

import me.peridot.peridrop.PeriDrop;
import me.peridot.peridrop.data.configuration.PluginConfiguration;
import org.bukkit.Bukkit;

public class RankingUpdateScheduler {

    private final PeriDrop plugin;

    public RankingUpdateScheduler(PeriDrop plugin) {
        this.plugin = plugin;
    }

    public void start() {
        Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, () -> {
            plugin.getDatabaseManager().getRankDatabase().loadRanksAsync();
        }, 0, 20 * PluginConfiguration.ranking_update_delay);
    }

}
