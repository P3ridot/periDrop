package me.peridot.peridrop.inventories;

import api.peridot.periapi.inventories.CustomInventory;
import api.peridot.periapi.inventories.InventoryContent;
import api.peridot.periapi.inventories.items.InventoryItem;
import api.peridot.periapi.inventories.providers.InventoryProvider;
import api.peridot.periapi.items.ItemBuilder;
import api.peridot.periapi.langapi.Replacement;
import me.peridot.peridrop.PeriDrop;
import me.peridot.peridrop.data.configuration.PluginConfiguration;
import me.peridot.peridrop.drop.Drop;
import me.peridot.peridrop.drop.DropManager;
import me.peridot.peridrop.user.User;
import org.apache.commons.lang.StringUtils;
import org.bukkit.entity.Player;

import java.util.ArrayList;
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
        User user = plugin.getUserManager().createUser(player);
        DropManager dropManager = plugin.getConfigurationManager().getDropManager();

        content.clear();
        for (Drop drop : dropManager.getDropsList()) {
            boolean disabled = user.isDropDisabled(drop);
            String status = disabled ? PluginConfiguration.drop_status_disabled : PluginConfiguration.drop_status_enabled;
            String fortune_status = drop.isFortuneAffect() ? PluginConfiguration.fortune_enabled : PluginConfiguration.fortune_disabled;
            List<String> lore = drop.isFortuneAffect() ? PluginConfiguration.drop_toogle_drop_with_fortune_lore : PluginConfiguration.drop_toogle_drop_without_fortune_lore;
            content.addItem(InventoryItem.builder()
                    .item(new ItemBuilder(drop.getMaterial(), 1, drop.getDurability())
                            .setName(drop.getDisplayName())
                            .setLore(replaceInList(lore, new Replacement("{STATUS}", status),
                                    new Replacement("{CHANCE}", PluginConfiguration.decimalFormat.format(drop.getChance() * 100F)),
                                    new Replacement("{MIN-AMOUNT}", Integer.valueOf(drop.getMinAmount()).toString()),
                                    new Replacement("{MAX-AMOUNT}", Integer.valueOf(drop.getMaxAmount()).toString()),
                                    new Replacement("{MIN-HEIGHT}", Integer.valueOf(drop.getMinHeight()).toString()),
                                    new Replacement("{MAX-HEIGHT}", Integer.valueOf(drop.getMaxHeight()).toString()),
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
                                    .plugin(plugin)
                                    .manager(plugin.getPeriApi().getInventoryManager())
                                    .provider(new FortuneInventory(plugin, rows, drop))
                                    .rows(rows)
                                    .title(PluginConfiguration.fortune_title)
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
        content.fillRow(rows, InventoryItem.builder().item(PluginConfiguration.drop_background_item.clone()).build());
        content.setItem(rows, 4, InventoryItem.builder()
                .item(PluginConfiguration.drop_all_enable_item.clone())
                .consumer(event -> {
                    for (Drop drop : dropManager.getDropsList()) {
                        user.setDropDisabled(drop, false);
                    }
                })
                .update(true)
                .build());
        content.setItem(rows, 6, InventoryItem.builder()
                .item(PluginConfiguration.drop_all_disable_item.clone())
                .consumer(event -> {
                    for (Drop drop : dropManager.getDropsList()) {
                        user.setDropDisabled(drop, true);
                    }
                })
                .update(true)
                .build());
        content.setItem(rows, 9, InventoryItem.builder()
                .item(PluginConfiguration.drop_back_item.clone())
                .consumer(event -> plugin.getInventoryManager().getMenuInventory().open(player))
                .build());
    }

    @Override
    public void update(Player player, InventoryContent content) {
    }

    private List<String> replaceInList(List<String> list, Replacement... replacements) {
        List<String> resultList = new ArrayList<>(list);
        for (int i = 0; i < resultList.size(); i++) {
            resultList.set(i, replace(resultList.get(i), replacements));
        }
        return resultList;
    }

    private String replace(String msg, Replacement... replacements) {
        String toReturn = msg;
        for (Replacement r : replacements) {
            toReturn = StringUtils.replace(toReturn, r.getFrom(), r.getTo());
        }
        return toReturn;
    }
}
