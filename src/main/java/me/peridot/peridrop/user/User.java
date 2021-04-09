package me.peridot.peridrop.user;

import me.peridot.peridrop.drop.Drop;
import me.peridot.peridrop.modifiable.Modifiable;
import me.peridot.peridrop.user.rank.Rank;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class User extends Modifiable {

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
        return this.uuid;
    }

    public String getName() {
        return this.name;
    }

    public List<Drop> getDisabledDrops() {
        return new ArrayList<>(this.disabledDrops);
    }

    public boolean isDropDisabled(Drop drop) {
        return this.disabledDrops.contains(drop);
    }

    public void setDropDisabled(Drop drop, boolean disabled) {
        if (disabled && this.isDropDisabled(drop) || !disabled && !this.isDropDisabled(drop)) return;

        if (disabled && !this.isDropDisabled(drop)) {
            this.disabledDrops.add(drop);
        } else if (!disabled && this.isDropDisabled(drop)) {
            this.disabledDrops.remove(drop);
        }
        this.setModified(true);
    }

    public void toggleDrop(Drop drop) {
        this.setDropDisabled(drop, !this.isDropDisabled(drop));
    }

    public List<SettingsType> getDisabledSettings() {
        return new ArrayList<>(this.disabledSettings);
    }

    public boolean isSettingDisabled(SettingsType type) {
        return this.disabledSettings.contains(type);
    }

    public void setSettingDisabled(SettingsType type, boolean disabled) {
        if (disabled && this.isSettingDisabled(type) || !disabled && !this.isSettingDisabled(type)) return;

        if (disabled && !this.isSettingDisabled(type)) {
            this.disabledSettings.add(type);
        } else if (!disabled && this.isSettingDisabled(type)) {
            this.disabledSettings.remove(type);
        }
        this.setModified(true);
    }

    public void toggleSetting(SettingsType type) {
        this.setSettingDisabled(type, !this.isSettingDisabled(type));
    }

    public Rank getRank() {
        return this.rank;
    }

    public void setRank(Rank rank) {
        this.rank = rank;
        this.setModified(true);
    }

}
