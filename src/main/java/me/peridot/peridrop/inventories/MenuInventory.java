package me.peridot.peridrop.inventories;

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
        content.fill(InventoryItem.builder().item(plugin.getConfigurations().getPluginConfiguration().getItemBuilder("inventories.menu.buttons.background").clone()).build());
        content.setItem(plugin.getConfigurations().getPluginConfiguration().getInt("inventories.menu.buttons.stone.slot"), InventoryItem.builder()
                .item(plugin.getConfigurations().getPluginConfiguration().getItemBuilder("inventories.menu.buttons.stone").clone())
                .consumer(event -> plugin.getInventoryManager().getDropInventory().open(player))
                .build());

        content.setItem(plugin.getConfigurations().getPluginConfiguration().getInt("inventories.menu.buttons.ranking.slot"), InventoryItem.builder()
                .item(plugin.getConfigurations().getPluginConfiguration().getItemBuilder("inventories.menu.buttons.ranking").clone())
                .consumer(event -> plugin.getInventoryManager().getRankingInventory().open(player, 0))
                .build());
        if (SettingsType.isSettingsInventoryEnabled()) {
            content.setItem(plugin.getConfigurations().getPluginConfiguration().getInt("inventories.menu.buttons.settings.slot"), InventoryItem.builder()
                    .item(plugin.getConfigurations().getPluginConfiguration().getItemBuilder("inventories.menu.buttons.settings").clone())
                    .consumer(event -> plugin.getInventoryManager().getSettingsInventory().open(player))
                    .build());
        }
        content.setItem(plugin.getConfigurations().getPluginConfiguration().getInt("inventories.menu.buttons.drop-exp.slot"), InventoryItem.builder()
                .item(plugin.getConfigurations().getPluginConfiguration().getItemBuilder("inventories.menu.buttons.drop-exp").clone())
                .build());
    }

    @Override
    public void update(Player player, InventoryContent content) {
    }
}
