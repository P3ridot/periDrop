package me.peridot.peridrop.schedulers;

import me.peridot.peridrop.PeriDrop;
import me.peridot.peridrop.user.User;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class AutoSaveScheduler {

    private final PeriDrop plugin;

    public AutoSaveScheduler(PeriDrop plugin) {
        this.plugin = plugin;
    }

    public void start() {
        Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, () -> {
            for (Player player : Bukkit.getOnlinePlayers()) {
                User user = plugin.getUserManager().createUser(player);
                plugin.getDatabaseManager().getUserDatabase().saveUserAsync(user);
            }
        }, 0, 20 * plugin.getConfigurations().getPluginConfiguration().getInt("tasks.autosave"));
    }

}
