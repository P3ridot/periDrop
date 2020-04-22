package me.peridot.peridrop.user;

import api.peridot.periapi.items.ItemBuilder;
import jdk.nashorn.internal.objects.annotations.Getter;
import jdk.nashorn.internal.objects.annotations.Setter;

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

    @Getter
    public ItemBuilder getItem() {
        return item.clone();
    }

    @Setter
    public void setItem(ItemBuilder item) {
        this.item = item;
    }

    @Getter
    public boolean isEnabled() {
        return enabled;
    }

    @Setter
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
