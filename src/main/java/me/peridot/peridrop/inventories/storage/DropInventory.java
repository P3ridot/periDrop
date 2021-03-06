package me.peridot.peridrop.inventories.storage;

import api.peridot.periapi.configuration.ConfigurationFile;
import api.peridot.periapi.inventories.CustomInventory;
import api.peridot.periapi.inventories.InventoryContent;
import api.peridot.periapi.inventories.items.InventoryItem;
import api.peridot.periapi.inventories.providers.InventoryProvider;
import api.peridot.periapi.items.ItemBuilder;
import api.peridot.periapi.utils.replacements.Replacement;
import api.peridot.periapi.utils.replacements.ReplacementUtil;
import me.peridot.peridrop.PeriDrop;
import me.peridot.peridrop.data.configuration.PluginConfiguration;
import me.peridot.peridrop.drop.Drop;
import me.peridot.peridrop.drop.DropsManager;
import me.peridot.peridrop.user.User;
import org.bukkit.entity.Player;

import java.util.List;

public class DropInventory implements InventoryProvider {

    private final PeriDrop plugin;

    private final int rows;

    public DropInventory(PeriDrop plugin, int rows) {
        this.plugin = plugin;
        this.rows = rows;
    }

    @Override
    public void init(Player player, InventoryContent content) {
        User user = this.plugin.getUserCache().createUser(player);
        PluginConfiguration config = this.plugin.getPluginConfiguration();
        ConfigurationFile inventoriesConfig = this.plugin.getInventoriesConfiguration();
        DropsManager dropsManager = this.plugin.getDropsManager();

        content.clear();
        for (Drop drop : dropsManager.getDropsList()) {
            boolean disabled = user.isDropDisabled(drop);
            String status = disabled ? config.getColoredString("messages.drop_status.disabled") : config.getColoredString("messages.drop_status.enabled");
            String fortune_status = drop.isFortuneAffect() ? config.getColoredString("messages.fortune.enabled") : config.getColoredString("messages.fortune.disabled");
            List<String> lore = drop.isFortuneAffect() ? inventoriesConfig.getColoredStringList("drop.buttons.drop_toogle.with_fortune.lore") : inventoriesConfig.getColoredStringList("drop.buttons.drop_toogle.without_fortune.lore");
            content.addItem(InventoryItem.builder()
                    .item(new ItemBuilder(drop.getMaterial(), 1, drop.getDurability())
                            .setName(drop.getDisplayName())
                            .setLore(ReplacementUtil.replace(lore, new Replacement("{STATUS}", status),
                                    new Replacement("{CHANCE}", PluginConfiguration.decimalFormat.format(drop.getChance() * 100F)),
                                    new Replacement("{MIN-AMOUNT}", drop.getMinAmount()),
                                    new Replacement("{MAX-AMOUNT}", drop.getMaxAmount()),
                                    new Replacement("{MIN-HEIGHT}", drop.getMinHeight()),
                                    new Replacement("{MAX-HEIGHT}", drop.getMaxHeight()),
                                    new Replacement("{BIOMES}", drop.getBiomesListString()),
                                    new Replacement("{TOOLS}", drop.getToolsListString()),
                                    new Replacement("{FORTUNE}", fortune_status)))
                            .build())
                    .consumer(event -> {
                        if (drop.isFortuneAffect() && event.getClick().isRightClick()) {
                            int rows = (int) Math.ceil((float) drop.getFortuneDropsList().size() / 9) + 1;
                            if (rows > 6) {
                                rows = 6;
                            }
                            CustomInventory fortuneInventory = CustomInventory.builder()
                                    .plugin(this.plugin)
                                    .manager(this.plugin.getPeriAPI().getInventoryManager())
                                    .provider(new FortuneInventory(this.plugin, rows, drop))
                                    .rows(rows)
                                    .title(inventoriesConfig.getColoredString("fortune.title"))
                                    .updateDelay(-1)
                                    .build();
                            fortuneInventory.open(player);
                        } else {
                            user.toggleDrop(drop);
                        }
                    })
                    .update(true)
                    .build());
        }
        content.fillRow(this.rows, InventoryItem.builder().item(inventoriesConfig.getItemBuilder("drop.buttons.background").clone()).build());
        content.setItem(this.rows, 4, InventoryItem.builder()
                .item(inventoriesConfig.getItemBuilder("drop.buttons.all_enable").clone())
                .consumer(event -> {
                    for (Drop drop : dropsManager.getDropsList()) {
                        user.setDropDisabled(drop, false);
                    }
                })
                .update(true)
                .build());
        content.setItem(this.rows, 6, InventoryItem.builder()
                .item(inventoriesConfig.getItemBuilder("drop.buttons.all_disable").clone())
                .consumer(event -> {
                    for (Drop drop : dropsManager.getDropsList()) {
                        user.setDropDisabled(drop, true);
                    }
                })
                .update(true)
                .build());
        content.setItem(this.rows, 9, InventoryItem.builder()
                .item(inventoriesConfig.getItemBuilder("drop.buttons.back").clone())
                .consumer(event -> this.plugin.getInventoryManager().getMenuInventory().open(player))
                .build());
    }

    @Override
    public void update(Player player, InventoryContent content) {
    }

}
