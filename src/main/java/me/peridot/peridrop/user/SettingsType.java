package me.peridot.peridrop.user;

import api.peridot.periapi.items.ItemBuilder;

public enum SettingsType {
    COBBLESTONE_DROP(null, false),
    DROP_NOTIFICATION(null, false),
    LEVEL_UP_NOTIFICATION(null, false);

    private ItemBuilder item;
    private boolean enabled;

    SettingsType(ItemBuilder item, boolean enabled) {
        this.item = item;
        this.enabled = enabled;
    }

    public ItemBuilder getItem() {
        return this.item.clone();
    }

    public void setItem(ItemBuilder item) {
        this.item = item;
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public static boolean isSettingsInventoryEnabled() {
        for (SettingsType type : values()) {
            if (type.isEnabled()) return true;
        }
        return false;
    }
}
