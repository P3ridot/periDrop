package me.peridot.peridrop.inventories;

import api.peridot.periapi.configuration.ConfigurationFile;
import api.peridot.periapi.inventories.CustomInventory;
import api.peridot.periapi.inventories.PeriInventoryManager;
import me.peridot.peridrop.PeriDrop;
import me.peridot.peridrop.inventories.storage.DropInventory;
import me.peridot.peridrop.inventories.storage.MenuInventory;
import me.peridot.peridrop.inventories.storage.RankingInventory;
import me.peridot.peridrop.inventories.storage.SettingsInventory;
import me.peridot.peridrop.user.SettingsType;

public class InventoryManager {

    private final PeriDrop plugin;
    private final PeriInventoryManager manager;

    private final CustomInventory menuInventory;
    private final CustomInventory dropInventory;
    private final CustomInventory settingsInventory;
    private final CustomInventory rankingInventory;

    public InventoryManager(PeriDrop plugin) {
        this.plugin = plugin;
        this.manager = plugin.getPeriAPI().getInventoryManager();

        ConfigurationFile inventoriesConfig = plugin.getInventoriesConfiguration();
        reloadSettingsConfiguration(inventoriesConfig);

        menuInventory = CustomInventory.builder()
                .plugin(plugin)
                .manager(manager)
                .provider(new MenuInventory(plugin))
                .rows(inventoriesConfig.getInt("menu.size"))
                .title(inventoriesConfig.getColoredString("menu.title"))
                .updateDelay(-1)
                .build();

        int drop_inventory_rows = (int) Math.ceil((float) plugin.getDropsManager().getDropsList().size() / 9) + 1;
        if (drop_inventory_rows > 6) {
            drop_inventory_rows = 6;
        }
        dropInventory = CustomInventory.builder()
                .plugin(plugin)
                .manager(manager)
                .provider(new DropInventory(plugin, drop_inventory_rows))
                .rows(drop_inventory_rows)
                .title(inventoriesConfig.getColoredString("drop.title"))
                .updateDelay(-1)
                .build();

        settingsInventory = CustomInventory.builder()
                .plugin(plugin)
                .manager(manager)
                .provider(new SettingsInventory(plugin))
                .rows(2)
                .title(inventoriesConfig.getColoredString("settings.title"))
                .updateDelay(-1)
                .build();

        rankingInventory = CustomInventory.builder()
                .plugin(plugin)
                .manager(manager)
                .provider(new RankingInventory(plugin))
                .rows(6)
                .title(inventoriesConfig.getColoredString("ranking.title"))
                .updateDelay(-1)
                .build();
    }

    private void reloadSettingsConfiguration(ConfigurationFile inventoriesConfig) {
        SettingsType.COBBLESTONE_DROP.setEnabled(inventoriesConfig.getBoolean("settings.buttons.cobblestone_drop.enabled"));
        SettingsType.DROP_NOTIFICATION.setEnabled(inventoriesConfig.getBoolean("settings.buttons.drop_notification.enabled"));
        SettingsType.LEVEL_UP_NOTIFICATION.setEnabled(inventoriesConfig.getBoolean("settings.buttons.level_up_notification.enabled"));
        if (SettingsType.COBBLESTONE_DROP.isEnabled()) {
            SettingsType.COBBLESTONE_DROP.setItem(inventoriesConfig.getItemBuilder("settings.buttons.cobblestone_drop"));
        }
        if (SettingsType.DROP_NOTIFICATION.isEnabled()) {
            SettingsType.DROP_NOTIFICATION.setItem(inventoriesConfig.getItemBuilder("settings.buttons.drop_notification"));
        }
        if (SettingsType.LEVEL_UP_NOTIFICATION.isEnabled()) {
            SettingsType.LEVEL_UP_NOTIFICATION.setItem(inventoriesConfig.getItemBuilder("settings.buttons.level_up_notification"));
        }
    }

    public CustomInventory getMenuInventory() {
        return menuInventory;
    }

    public CustomInventory getDropInventory() {
        return dropInventory;
    }

    public CustomInventory getSettingsInventory() {
        return settingsInventory;
    }

    public CustomInventory getRankingInventory() {
        return rankingInventory;
    }

}
