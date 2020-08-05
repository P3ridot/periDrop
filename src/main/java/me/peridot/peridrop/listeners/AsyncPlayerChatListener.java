package me.peridot.peridrop.listeners;

import api.peridot.periapi.utils.replacements.Replacement;
import api.peridot.periapi.utils.replacements.ReplacementUtil;
import me.peridot.peridrop.PeriDrop;
import me.peridot.peridrop.user.User;
import me.peridot.peridrop.user.rank.Rank;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class AsyncPlayerChatListener implements Listener {

    private final PeriDrop plugin;

    public AsyncPlayerChatListener(PeriDrop plugin) {
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        User user = plugin.getUserCache().createUser(player);
        Rank rank = user.getRank();

        String format = event.getFormat();
        format = ReplacementUtil.replace(format, new Replacement("{MINE-LEVEL}", rank.getLevel()),
                new Replacement("{MINE-XP}", rank.getXp()),
                new Replacement("{MINE-RANK}", rank.getPosition()));
        event.setFormat(format);
    }

}
