package me.peridot.peridrop.inventories.storage;

import api.peridot.periapi.configuration.ConfigurationFile;
import api.peridot.periapi.inventories.InventoryContent;
import api.peridot.periapi.inventories.items.InventoryItem;
import api.peridot.periapi.inventories.providers.InventoryProvider;
import api.peridot.periapi.items.ItemBuilder;
import api.peridot.periapi.utils.replacements.Replacement;
import me.peridot.peridrop.PeriDrop;
import me.peridot.peridrop.data.configuration.PluginConfiguration;
import me.peridot.peridrop.user.SettingsType;
import me.peridot.peridrop.user.User;
import org.bukkit.entity.Player;

public class SettingsInventory implements InventoryProvider {

    private final PeriDrop plugin;

    public SettingsInventory(PeriDrop plugin) {
        this.plugin = plugin;
    }

    @Override
    public void init(Player player, InventoryContent content) {
        PluginConfiguration config = this.plugin.getPluginConfiguration();
        ConfigurationFile inventoriesConfig = this.plugin.getInventoriesConfiguration();

        User user = this.plugin.getUserCache().createUser(player);

        content.clear();
        for (SettingsType type : SettingsType.values()) {
            if (!type.isEnabled()) continue;
            String status = user.isSettingDisabled(type) ? config.getColoredString("messages.settings_status.enabled") : config.getColoredString("messages.settings_status.disabled");
            ItemBuilder item = type.getItem()
                    .replaceInName(new Replacement("{STATUS}", status))
                    .replaceInLore(new Replacement("{STATUS}", status));
            content.addItem(InventoryItem.builder()
                    .item(item)
                    .consumer(event -> user.toggleSetting(type))
                    .update(true)
                    .build());
        }
        content.fillRow(2, InventoryItem.builder().item(inventoriesConfig.getItemBuilder("settings.buttons.background").clone()).build());
        content.setItem(2, 5, InventoryItem.builder()
                .item(inventoriesConfig.getItemBuilder("settings.buttons.back").clone())
                .consumer(event -> this.plugin.getInventoryManager().getMenuInventory().open(player))
                .build());
    }

    @Override
    public void update(Player player, InventoryContent content) {
    }

}
