package me.peridot.peridrop.inventories;

import api.peridot.periapi.inventories.InventoryContent;
import api.peridot.periapi.inventories.items.InventoryItem;
import api.peridot.periapi.inventories.providers.InventoryProvider;
import api.peridot.periapi.items.ItemBuilder;
import api.peridot.periapi.langapi.Replacement;
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
        content.clear();
        for (FortuneDrop fortuneDrop : drop.getFortuneDropsList()) {
            ItemBuilder item = PluginConfiguration.fortune_item.clone();
            item.replaceInName(new Replacement("{FORTUNE-LEVEL}", Integer.valueOf(fortuneDrop.getFortuneLevel()).toString()),
                    new Replacement("{CHANCE}", PluginConfiguration.decimalFormat.format(fortuneDrop.getChance() * 100F)),
                    new Replacement("{OLD-CHANCE}", PluginConfiguration.decimalFormat.format(drop.getChance() * 100F)),
                    new Replacement("{DIFFERENCE-CHANCE}", PluginConfiguration.decimalFormat.format(Math.abs(fortuneDrop.getChance() * 100F - drop.getChance() * 100F))),
                    new Replacement("{MIN-AMOUNT}", Integer.valueOf(fortuneDrop.getMinAmount()).toString()),
                    new Replacement("{MAX-AMOUNT}", Integer.valueOf(fortuneDrop.getMaxAmount()).toString()),
                    new Replacement("{OLD-MIN-AMOUNT}", Integer.valueOf(drop.getMinAmount()).toString()),
                    new Replacement("{OLD-MAX-AMOUNT}", Integer.valueOf(drop.getMaxAmount()).toString()),
                    new Replacement("{DIFFERENCE-MIN-AMOUNT}", Integer.valueOf(Math.abs(fortuneDrop.getMinAmount() - drop.getMinAmount())).toString()),
                    new Replacement("{DIFFERENCE-MAX-AMOUNT}", Integer.valueOf(Math.abs(fortuneDrop.getMaxAmount() - drop.getMaxAmount())).toString()));
            item.replaceInLore(new Replacement("{FORTUNE-LEVEL}", Integer.valueOf(fortuneDrop.getFortuneLevel()).toString()),
                    new Replacement("{CHANCE}", PluginConfiguration.decimalFormat.format(fortuneDrop.getChance() * 100F)),
                    new Replacement("{OLD-CHANCE}", PluginConfiguration.decimalFormat.format(drop.getChance() * 100F)),
                    new Replacement("{DIFFERENCE-CHANCE}", PluginConfiguration.decimalFormat.format(Math.abs(fortuneDrop.getChance() * 100F - drop.getChance() * 100F))),
                    new Replacement("{MIN-AMOUNT}", Integer.valueOf(fortuneDrop.getMinAmount()).toString()),
                    new Replacement("{MAX-AMOUNT}", Integer.valueOf(fortuneDrop.getMaxAmount()).toString()),
                    new Replacement("{OLD-MIN-AMOUNT}", Integer.valueOf(drop.getMinAmount()).toString()),
                    new Replacement("{OLD-MAX-AMOUNT}", Integer.valueOf(drop.getMaxAmount()).toString()),
                    new Replacement("{DIFFERENCE-MIN-AMOUNT}", Integer.valueOf(Math.abs(fortuneDrop.getMinAmount() - drop.getMinAmount())).toString()),
                    new Replacement("{DIFFERENCE-MAX-AMOUNT}", Integer.valueOf(Math.abs(fortuneDrop.getMaxAmount() - drop.getMaxAmount())).toString()));

            if (PluginConfiguration.fortune_enchant_with_fortune) {
                item.addUnsafeEnchantment(Enchantment.LOOT_BONUS_BLOCKS, fortuneDrop.getFortuneLevel());
            }
            content.addItem(InventoryItem.builder()
                    .item(item.build())
                    .build());
        }
        content.fillRow(rows, InventoryItem.builder().item(PluginConfiguration.drop_background_item.clone()).build());
        content.setItem(rows, 5, InventoryItem.builder()
                .item(PluginConfiguration.fortune_back_item.clone().build())
                .consumer(event -> plugin.getInventoryManager().getDropInventory().open(player))
                .build());
    }

    @Override
    public void update(Player player, InventoryContent content) {
    }
}
