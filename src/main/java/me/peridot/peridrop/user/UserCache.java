package me.peridot.peridrop.user;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import me.peridot.peridrop.PeriDrop;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class UserCache {

    private final PeriDrop plugin;

    private final Map<UUID, User> userMap = new HashMap<>();
    private final Cache<UUID, User> userCache = CacheBuilder.newBuilder()
            .expireAfterWrite(30, TimeUnit.MINUTES)
            .build();

    public UserCache(PeriDrop plugin) {
        this.plugin = plugin;
    }

    public void removeUser(UUID uuid) {
        User user = this.userMap.get(uuid);
        if (user != null) {
            this.userCache.put(uuid, user);
        }
        this.userMap.remove(uuid);
    }

    public void removeUser(Player player) {
        this.removeUser(player.getUniqueId());
    }

    public User createUser(UUID uuid) {
        User user = this.userMap.get(uuid);
        if (user == null) {
            User cachedUser = this.userCache.getIfPresent(uuid);
            if (cachedUser != null) {
                this.userMap.put(uuid, user = cachedUser);
                return user;
            }
            this.userMap.put(uuid, user = new User(uuid, this.plugin.getServer().getPlayer(uuid).getName()));
        }
        return user;
    }

    public User createUser(Player player) {
        return this.createUser(player.getUniqueId());
    }

    public Set<User> getModifiedUsers() {
        return this.userMap.values().stream()
                .filter(user -> user.isModified() || user.getRank().isModified())
                .collect(Collectors.toSet());
    }

}
