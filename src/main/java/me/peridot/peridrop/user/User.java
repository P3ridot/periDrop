package me.peridot.peridrop.user;

import me.peridot.peridrop.drop.Drop;
import me.peridot.peridrop.user.rank.Rank;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class User {

    private final UUID uuid;
    private final String name;

    private final List<Drop> disabledDrops = new ArrayList<>();
    private final List<SettingsType> disabledSettings = new ArrayList<>();

    private Rank rank;

    public User(UUID uuid, String name) {
        this.uuid = uuid;
        this.name = name;
        this.rank = new Rank(this);
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public List<Drop> getDisabledDrops() {
        return new ArrayList<>(disabledDrops);
    }

    public boolean isDropDisabled(Drop drop) {
        return disabledDrops.contains(drop);
    }

    public void setDropDisabled(Drop drop, boolean disabled) {
        if (disabled && isDropDisabled(drop) || !disabled && !isDropDisabled(drop)) return;

        if (disabled && !isDropDisabled(drop)) {
            disabledDrops.add(drop);
        } else if (!disabled && isDropDisabled(drop)) {
            disabledDrops.remove(drop);
        }
    }

    public void toggleDrop(Drop drop) {
        setDropDisabled(drop, !isDropDisabled(drop));
    }

    public List<SettingsType> getDisabledSettings() {
        return new ArrayList<>(disabledSettings);
    }

    public boolean isSettingDisabled(SettingsType type) {
        return disabledSettings.contains(type);
    }

    public void setSettingDisabled(SettingsType type, boolean disabled) {
        if (disabled && isSettingDisabled(type) || !disabled && !isSettingDisabled(type)) return;

        if (disabled && !isSettingDisabled(type)) {
            disabledSettings.add(type);
        } else if (!disabled && isSettingDisabled(type)) {
            disabledSettings.remove(type);
        }
    }

    public void toggleSetting(SettingsType type) {
        setSettingDisabled(type, !isSettingDisabled(type));
    }

    public Rank getRank() {
        return rank;
    }

    public void setRank(Rank rank) {
        this.rank = rank;
    }
}
