package me.peridot.peridrop.inventories.storage;

import api.peridot.periapi.configuration.ConfigurationFile;
import api.peridot.periapi.inventories.InventoryContent;
import api.peridot.periapi.inventories.items.InventoryItem;
import api.peridot.periapi.inventories.providers.InventoryProvider;
import api.peridot.periapi.items.ItemBuilder;
import api.peridot.periapi.utils.replacements.Replacement;
import me.peridot.peridrop.PeriDrop;
import me.peridot.peridrop.data.configuration.PluginConfiguration;
import me.peridot.peridrop.drop.Drop;
import me.peridot.peridrop.drop.fortune.FortuneDrop;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;

public class FortuneInventory implements InventoryProvider {

    private final PeriDrop plugin;
    private final int rows;
    private final Drop drop;

    public FortuneInventory(PeriDrop plugin, int rows, Drop drop) {
        this.plugin = plugin;
        this.rows = rows;
        this.drop = drop;
    }

    @Override
    public void init(Player player, InventoryContent content) {
        ConfigurationFile inventoriesConfig = this.plugin.getInventoriesConfiguration();

        content.clear();
        for (FortuneDrop fortuneDrop : this.drop.getFortuneDropsList()) {
            ItemBuilder item = inventoriesConfig.getItemBuilder("fortune.buttons.fortune").clone();
            item.replaceInName(new Replacement("{FORTUNE-LEVEL}", fortuneDrop.getFortuneLevel()),
                    new Replacement("{CHANCE}", PluginConfiguration.decimalFormat.format(fortuneDrop.getChance() * 100F)),
                    new Replacement("{OLD-CHANCE}", PluginConfiguration.decimalFormat.format(this.drop.getChance() * 100F)),
                    new Replacement("{DIFFERENCE-CHANCE}", PluginConfiguration.decimalFormat.format(Math.abs(fortuneDrop.getChance() * 100F - this.drop.getChance() * 100F))),
                    new Replacement("{MIN-AMOUNT}", fortuneDrop.getMinAmount()),
                    new Replacement("{MAX-AMOUNT}", fortuneDrop.getMaxAmount()),
                    new Replacement("{OLD-MIN-AMOUNT}", this.drop.getMinAmount()),
                    new Replacement("{OLD-MAX-AMOUNT}", this.drop.getMaxAmount()),
                    new Replacement("{DIFFERENCE-MIN-AMOUNT}", Math.abs(fortuneDrop.getMinAmount() - this.drop.getMinAmount())),
                    new Replacement("{DIFFERENCE-MAX-AMOUNT}", Math.abs(fortuneDrop.getMaxAmount() - this.drop.getMaxAmount())));
            item.replaceInLore(new Replacement("{FORTUNE-LEVEL}", fortuneDrop.getFortuneLevel()),
                    new Replacement("{CHANCE}", PluginConfiguration.decimalFormat.format(fortuneDrop.getChance() * 100F)),
                    new Replacement("{OLD-CHANCE}", PluginConfiguration.decimalFormat.format(this.drop.getChance() * 100F)),
                    new Replacement("{DIFFERENCE-CHANCE}", PluginConfiguration.decimalFormat.format(Math.abs(fortuneDrop.getChance() * 100F - this.drop.getChance() * 100F))),
                    new Replacement("{MIN-AMOUNT}", fortuneDrop.getMinAmount()),
                    new Replacement("{MAX-AMOUNT}", fortuneDrop.getMaxAmount()),
                    new Replacement("{OLD-MIN-AMOUNT}", this.drop.getMinAmount()),
                    new Replacement("{OLD-MAX-AMOUNT}", this.drop.getMaxAmount()),
                    new Replacement("{DIFFERENCE-MIN-AMOUNT}", Math.abs(fortuneDrop.getMinAmount() - this.drop.getMinAmount())),
                    new Replacement("{DIFFERENCE-MAX-AMOUNT}", Math.abs(fortuneDrop.getMaxAmount() - this.drop.getMaxAmount())));

            if (inventoriesConfig.getBoolean("fortune.buttons.fortune.enchant_with_fortune")) {
                item.addUnsafeEnchantment(Enchantment.LOOT_BONUS_BLOCKS, fortuneDrop.getFortuneLevel());
            }
            content.addItem(InventoryItem.builder()
                    .item(item.build())
                    .build());
        }
        content.fillRow(this.rows, InventoryItem.builder().item(inventoriesConfig.getItemBuilder("fortune.buttons.background").clone()).build());
        content.setItem(this.rows, 5, InventoryItem.builder()
                .item(inventoriesConfig.getItemBuilder("fortune.buttons.back").clone().build())
                .consumer(event -> this.plugin.getInventoryManager().getDropInventory().open(player))
                .build());
    }

    @Override
    public void update(Player player, InventoryContent content) {
    }

}
