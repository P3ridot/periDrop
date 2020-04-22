package me.peridot.peridrop.inventories;

import api.peridot.periapi.inventories.InventoryContent;
import api.peridot.periapi.inventories.items.InventoryItem;
import api.peridot.periapi.inventories.providers.InventoryProvider;
import me.peridot.peridrop.PeriDrop;
import me.peridot.peridrop.data.configuration.PluginConfiguration;
import me.peridot.peridrop.user.SettingsType;
import org.bukkit.entity.Player;

public class MenuInventory implements InventoryProvider {

    private final PeriDrop plugin;

    public MenuInventory(PeriDrop plugin) {
        this.plugin = plugin;
    }

    @Override
    public void init(Player player, InventoryContent content) {
        content.fill(InventoryItem.builder().item(PluginConfiguration.menu_background_item.clone()).build());
        content.setItem(PluginConfiguration.menu_drop_stone_slot, InventoryItem.builder()
                .item(PluginConfiguration.menu_drop_stone_item.clone())
                .consumer(event -> plugin.getInventoryManager().getDropInventory().open(player))
                .build());

        content.setItem(PluginConfiguration.menu_ranking_slot, InventoryItem.builder()
                .item(PluginConfiguration.menu_ranking_item.clone())
                .consumer(event -> plugin.getInventoryManager().getRankingInventory().open(player, 0))
                .build());
        if (SettingsType.isSettingsInventoryEnabled()) {
            content.setItem(PluginConfiguration.menu_settings_slot, InventoryItem.builder()
                    .item(PluginConfiguration.menu_settings_item.clone())
                    .consumer(event -> plugin.getInventoryManager().getSettingsInventory().open(player))
                    .build());
        }
        content.setItem(PluginConfiguration.menu_drop_exp_slot, InventoryItem.builder()
                .item(PluginConfiguration.menu_drop_exp_item.clone())
                .build());
    }

    @Override
    public void update(Player player, InventoryContent content) {
    }
}
