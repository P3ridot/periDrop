package me.peridot.peridrop.inventories;

import api.peridot.periapi.inventories.InventoryContent;
import api.peridot.periapi.inventories.Pagination;
import api.peridot.periapi.inventories.items.InventoryItem;
import api.peridot.periapi.inventories.providers.InventoryProvider;
import api.peridot.periapi.items.ItemBuilder;
import api.peridot.periapi.langapi.Replacement;
import me.peridot.peridrop.PeriDrop;
import me.peridot.peridrop.data.configuration.PluginConfiguration;
import me.peridot.peridrop.user.rank.Rank;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class RankingInventory implements InventoryProvider {

    private final PeriDrop plugin;

    public RankingInventory(PeriDrop plugin) {
        this.plugin = plugin;
    }

    @Override
    public void init(Player player, InventoryContent content) {
        Pagination pagination = new Pagination();
        int page = plugin.getInventoryManager().getRankingInventory().getPersonalInventoryData(player).getOpenedPage();

        content.clear();
        content.fillRow(1, InventoryItem.builder().item(PluginConfiguration.ranking_background_item.clone()).build());

        List<InventoryItem> items = new ArrayList<>();

        for (Rank rank : plugin.getRankManager().getRanksList()) {
            ItemBuilder item = PluginConfiguration.ranking_rank_item.clone();
            item.replaceInName(new Replacement("{NAME}", rank.getIdentifierName()),
                    new Replacement("{POSITION}", Integer.valueOf(rank.getPosition() + 1).toString()),
                    new Replacement("{LEVEL}", Integer.valueOf(rank.getLevel()).toString()),
                    new Replacement("{XP}", Integer.valueOf(rank.getXp()).toString()));
            item.replaceInLore(new Replacement("{NAME}", rank.getIdentifierName()),
                    new Replacement("{POSITION}", Integer.valueOf(rank.getPosition() + 1).toString()),
                    new Replacement("{LEVEL}", Integer.valueOf(rank.getLevel()).toString()),
                    new Replacement("{XP}", Integer.valueOf(rank.getXp()).toString()));
            if (PluginConfiguration.ranking_use_player_as_skull_owner) {
                item.setSkullOwner(rank.getIdentifierName());
            }
            items.add(InventoryItem.builder()
                    .item(item)
                    .build());
        }

        pagination.setItems(items);
        pagination.setItemsPerPage(9 * 4);

        content.iterator(pagination.getItemsForPage(page)).slotFrom(9).slotTo(45).iterate();
        if (PluginConfiguration.ranking_rank_empty_item != null && PluginConfiguration.ranking_rank_empty_item.build().getType() != Material.AIR) {
            content.iterator(InventoryItem.builder().item(PluginConfiguration.ranking_rank_empty_item.clone()).build()).onlyEmpty(true).slotFrom(9).slotTo(45).iterate();
        }

        content.fillRow(6, InventoryItem.builder().item(PluginConfiguration.ranking_background_item.clone()).build());
        content.setItem(6, 5, InventoryItem.builder()
                .item(PluginConfiguration.ranking_back_item.clone())
                .consumer(event -> plugin.getInventoryManager().getMenuInventory().open(player))
                .build());

        ItemBuilder actualPage = PluginConfiguration.ranking_actual_page_item.clone();
        actualPage.replaceInName(new Replacement("{PAGE}", Integer.valueOf(page + 1).toString()),
                new Replacement("{PAGE-COUNT}", Integer.valueOf(pagination.getPageCount()).toString()));
        actualPage.replaceInLore(new Replacement("{PAGE}", Integer.valueOf(page + 1).toString()),
                new Replacement("{PAGE-COUNT}", Integer.valueOf(pagination.getPageCount()).toString()));

        content.setItem(1, 5, InventoryItem.builder()
                .item(actualPage)
                .consumer(event -> {
                    new AnvilGUI.Builder()
                            .onComplete((anvilPlayer, text) -> {
                                if (text == null || text.isEmpty()) {
                                    return AnvilGUI.Response.text(PluginConfiguration.ranking_not_set);
                                }
                                int pageInput = 0;
                                try {
                                    pageInput = Integer.parseInt(text);
                                } catch (Exception ex) {
                                    return AnvilGUI.Response.text(PluginConfiguration.ranking_not_number);
                                }
                                if (pageInput < 1) {
                                    return AnvilGUI.Response.text(PluginConfiguration.ranking_page_smaller_than_one);
                                }
                                if (pageInput > pagination.getPageCount()) {
                                    return AnvilGUI.Response.text(PluginConfiguration.ranking_page_not_exist.replace("{PAGE}", text));
                                }
                                plugin.getInventoryManager().getRankingInventory().open(anvilPlayer, pageInput - 1);
                                return AnvilGUI.Response.close();
                            })
                            .text(PluginConfiguration.ranking_default_text)
                            .plugin(plugin)
                            .open(player);
                })
                .build());

        if (!pagination.isFirst(page)) {
            content.setItem(1, 3, InventoryItem.builder()
                    .item(PluginConfiguration.ranking_previous_page_item.clone())
                    .consumer(event -> {
                        plugin.getInventoryManager().getRankingInventory().getPersonalInventoryData(player).setOpenedPage(page - 1);
                    })
                    .update(true)
                    .build());
        } else {
            content.setItem(1, 3, InventoryItem.builder().item(PluginConfiguration.ranking_background_item.clone()).build());
        }

        if (!pagination.isLast(page)) {
            content.setItem(1, 7, InventoryItem.builder()
                    .item(PluginConfiguration.ranking_next_page_item.clone())
                    .consumer(event -> {
                        plugin.getInventoryManager().getRankingInventory().getPersonalInventoryData(player).setOpenedPage(page + 1);
                    })
                    .update(true)
                    .build());
        } else {
            content.setItem(1, 7, InventoryItem.builder().item(PluginConfiguration.ranking_background_item.clone()).build());
        }
    }

    @Override
    public void update(Player player, InventoryContent content) {

    }
}
