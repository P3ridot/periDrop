package me.peridot.peridrop.inventories;

import api.peridot.periapi.inventories.CustomInventory;
import api.peridot.periapi.inventories.PeriInventoryManager;
import me.peridot.peridrop.PeriDrop;

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

        menuInventory = CustomInventory.builder()
                .plugin(plugin)
                .manager(manager)
                .provider(new MenuInventory(plugin))
                .rows(plugin.getConfigurations().getPluginConfiguration().getInt("inventories.menu.size"))
                .title(plugin.getConfigurations().getPluginConfiguration().getColoredString("inventories.menu.title"))
                .updateDelay(-1)
                .build();

        int drop_inventory_rows = (int) Math.ceil((float) plugin.getConfigurations().getDropManager().getDropsList().size() / 9) + 1;
        if (drop_inventory_rows > 6) {
            drop_inventory_rows = 6;
        }
        dropInventory = CustomInventory.builder()
                .plugin(plugin)
                .manager(manager)
                .provider(new DropInventory(plugin, drop_inventory_rows))
                .rows(drop_inventory_rows)
                .title(plugin.getConfigurations().getPluginConfiguration().getColoredString("inventories.drop.title"))
                .updateDelay(20)
                .build();

        settingsInventory = CustomInventory.builder()
                .plugin(plugin)
                .manager(manager)
                .provider(new SettingsInventory(plugin))
                .rows(2)
                .title(plugin.getConfigurations().getPluginConfiguration().getColoredString("inventories.settings.title"))
                .updateDelay(20)
                .build();

        rankingInventory = CustomInventory.builder()
                .plugin(plugin)
                .manager(manager)
                .provider(new RankingInventory(plugin))
                .rows(6)
                .title(plugin.getConfigurations().getPluginConfiguration().getColoredString("inventories.ranking.title"))
                .updateDelay(-1)
                .build();
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
