package me.peridot.peridrop.listeners;

import me.peridot.peridrop.PeriDrop;
import me.peridot.peridrop.user.User;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {

    private final PeriDrop plugin;

    public PlayerQuitListener(PeriDrop plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        User user = this.plugin.getUserCache().createUser(player);
        this.plugin.getDatabaseManager().getUserDatabase().saveUserAsync(user);
        this.plugin.getRankSystem().update(user);
        this.plugin.getUserCache().removeUser(player);
    }

}
