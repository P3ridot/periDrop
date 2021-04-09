package me.peridot.peridrop.listeners;

import me.peridot.peridrop.PeriDrop;
import me.peridot.peridrop.user.User;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    private final PeriDrop plugin;

    public PlayerJoinListener(PeriDrop plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        User user = this.plugin.getUserCache().createUser(player);
        this.plugin.getDatabaseManager().getUserDatabase().loadUserAsync(user);
        this.plugin.getRankSystem().update(user);
    }

}
