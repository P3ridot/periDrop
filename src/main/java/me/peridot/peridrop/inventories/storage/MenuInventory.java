package me.peridot.peridrop.inventories.storage;

import api.peridot.periapi.configuration.ConfigurationFile;
import api.peridot.periapi.inventories.InventoryContent;
import api.peridot.periapi.inventories.items.InventoryItem;
import api.peridot.periapi.inventories.providers.InventoryProvider;
import me.peridot.peridrop.PeriDrop;
import me.peridot.peridrop.user.SettingsType;
import org.bukkit.entity.Player;

public class MenuInventory implements InventoryProvider {

    private final PeriDrop plugin;

    public MenuInventory(PeriDrop plugin) {
        this.plugin = plugin;
    }

    @Override
    public void init(Player player, InventoryContent content) {
        ConfigurationFile inventoriesConfig = this.plugin.getInventoriesConfiguration();

        content.fill(InventoryItem.builder().item(inventoriesConfig.getItemBuilder("menu.buttons.background").clone()).build());
        content.setItem(inventoriesConfig.getInt("menu.buttons.stone.slot"), InventoryItem.builder()
                .item(inventoriesConfig.getItemBuilder("menu.buttons.stone").clone())
                .consumer(event -> this.plugin.getInventoryManager().getDropInventory().open(player))
                .build());

        content.setItem(inventoriesConfig.getInt("menu.buttons.ranking.slot"), InventoryItem.builder()
                .item(inventoriesConfig.getItemBuilder("menu.buttons.ranking").clone())
                .consumer(event -> this.plugin.getInventoryManager().getRankingInventory().open(player, 0))
                .build());
        if (SettingsType.isSettingsInventoryEnabled()) {
            content.setItem(inventoriesConfig.getInt("menu.buttons.settings.slot"), InventoryItem.builder()
                    .item(inventoriesConfig.getItemBuilder("menu.buttons.settings").clone())
                    .consumer(event -> this.plugin.getInventoryManager().getSettingsInventory().open(player))
                    .build());
        }
        content.setItem(inventoriesConfig.getInt("menu.buttons.drop-exp.slot"), InventoryItem.builder()
                .item(inventoriesConfig.getItemBuilder("menu.buttons.drop-exp").clone())
                .build());
    }

    @Override
    public void update(Player player, InventoryContent content) {
    }

}
