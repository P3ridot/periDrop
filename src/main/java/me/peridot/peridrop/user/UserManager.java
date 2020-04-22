package me.peridot.peridrop.user;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class UserManager {

    private final Map<UUID, User> userMap = new HashMap<>();
    private final Cache<UUID, User> userCache = CacheBuilder.newBuilder()
            .expireAfterWrite(30, TimeUnit.MINUTES)
            .build();

    public void removeUser(UUID uuid) {
        final User user = userMap.get(uuid);
        if (user != null) {
            userCache.put(uuid, user);
        }
        userMap.remove(uuid);
    }

    public void removeUser(Player player) {
        removeUser(player.getUniqueId());
    }

    public User createUser(UUID uuid) {
        User user = userMap.get(uuid);
        if (user == null) {
            final User cachedUser = userCache.getIfPresent(uuid);
            if (cachedUser != null) {
                userMap.put(uuid, user = cachedUser);
                return user;
            }
            userMap.put(uuid, user = new User(uuid, Bukkit.getPlayer(uuid).getName()));
        }
        return user;
    }

    public User createUser(Player player) {
        return createUser(player.getUniqueId());
    }

}
