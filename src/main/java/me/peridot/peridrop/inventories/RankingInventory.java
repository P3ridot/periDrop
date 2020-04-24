package me.peridot.peridrop.inventories;

import api.peridot.periapi.configuration.langapi.Replacement;
import api.peridot.periapi.inventories.InventoryContent;
import api.peridot.periapi.inventories.Pagination;
import api.peridot.periapi.inventories.items.InventoryItem;
import api.peridot.periapi.inventories.providers.InventoryProvider;
import api.peridot.periapi.items.ItemBuilder;
import me.peridot.peridrop.PeriDrop;
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
        content.fillRow(1, InventoryItem.builder().item(plugin.getConfigurations().getPluginConfiguration().getItemBuilder("inventories.ranking.buttons.background").clone()).build());

        List<InventoryItem> items = new ArrayList<>();

        boolean usePlayerAsSkullOwner = plugin.getConfigurations().getPluginConfiguration().getBoolean("inventories.ranking.buttons.rank.use_player_as_skull_owner");

        for (Rank rank : plugin.getRankManager().getRanksList()) {
            ItemBuilder item = plugin.getConfigurations().getPluginConfiguration().getItemBuilder("inventories.ranking.buttons.rank").clone();
            item.replaceInName(new Replacement("{NAME}", rank.getIdentifierName()),
                    new Replacement("{POSITION}", Integer.valueOf(rank.getPosition() + 1).toString()),
                    new Replacement("{LEVEL}", Integer.valueOf(rank.getLevel()).toString()),
                    new Replacement("{XP}", Integer.valueOf(rank.getXp()).toString()));
            item.replaceInLore(new Replacement("{NAME}", rank.getIdentifierName()),
                    new Replacement("{POSITION}", Integer.valueOf(rank.getPosition() + 1).toString()),
                    new Replacement("{LEVEL}", Integer.valueOf(rank.getLevel()).toString()),
                    new Replacement("{XP}", Integer.valueOf(rank.getXp()).toString()));
            if (usePlayerAsSkullOwner && rank.getIdentifierName() != null && !rank.getIdentifierName().isEmpty()) {
                item.setSkullOwner(rank.getIdentifierName());
            }
            items.add(InventoryItem.builder()
                    .item(item)
                    .build());
        }

        pagination.setItems(items);
        pagination.setItemsPerPage(9 * 4);

        content.iterator(pagination.getItemsForPage(page)).slotFrom(9).slotTo(45).iterate();
        if (plugin.getConfigurations().getPluginConfiguration().getItemBuilder("inventories.ranking.buttons.empty-rank") != null && plugin.getConfigurations().getPluginConfiguration().getItemBuilder("inventories.ranking.buttons.empty-rank").build().getType() != Material.AIR) {
            content.iterator(InventoryItem.builder().item(plugin.getConfigurations().getPluginConfiguration().getItemBuilder("inventories.ranking.buttons.empty-rank").clone()).build()).onlyEmpty(true).slotFrom(9).slotTo(45).iterate();
        }

        content.fillRow(6, InventoryItem.builder().item(plugin.getConfigurations().getPluginConfiguration().getItemBuilder("inventories.ranking.buttons.background").clone()).build());
        content.setItem(6, 5, InventoryItem.builder()
                .item(plugin.getConfigurations().getPluginConfiguration().getItemBuilder("inventories.ranking.buttons.back").clone())
                .consumer(event -> plugin.getInventoryManager().getMenuInventory().open(player))
                .build());

        ItemBuilder currentPage = plugin.getConfigurations().getPluginConfiguration().getItemBuilder("inventories.ranking.buttons.current-page").clone();
        currentPage.replaceInName(new Replacement("{PAGE}", Integer.valueOf(page + 1).toString()),
                new Replacement("{PAGE-COUNT}", Integer.valueOf(pagination.getPageCount()).toString()));
        currentPage.replaceInLore(new Replacement("{PAGE}", Integer.valueOf(page + 1).toString()),
                new Replacement("{PAGE-COUNT}", Integer.valueOf(pagination.getPageCount()).toString()));

        content.setItem(1, 5, InventoryItem.builder()
                .item(currentPage)
                .consumer(event -> {
                    new AnvilGUI.Builder()
                            .onComplete((anvilPlayer, text) -> {
                                if (text == null || text.isEmpty()) {
                                    return AnvilGUI.Response.text(plugin.getConfigurations().getPluginConfiguration().getColoredString("messages.ranking.not-set"));
                                }
                                int pageInput = 0;
                                try {
                                    pageInput = Integer.parseInt(text);
                                } catch (Exception ex) {
                                    return AnvilGUI.Response.text(plugin.getConfigurations().getPluginConfiguration().getColoredString("messages.ranking.not-number"));
                                }
                                if (pageInput < 1) {
                                    return AnvilGUI.Response.text(plugin.getConfigurations().getPluginConfiguration().getColoredString("messages.ranking.page-smaller-than-one"));
                                }
                                if (pageInput > pagination.getPageCount()) {
                                    return AnvilGUI.Response.text(plugin.getConfigurations().getPluginConfiguration().getColoredString("messages.ranking.page-not-exist").replace("{PAGE}", text));
                                }
                                plugin.getInventoryManager().getRankingInventory().open(anvilPlayer, pageInput - 1);
                                return AnvilGUI.Response.close();
                            })
                            .text(plugin.getConfigurations().getPluginConfiguration().getColoredString("messages.ranking.default-text"))
                            .plugin(plugin)
                            .open(player);
                })
                .build());

        if (!pagination.isFirst(page)) {
            content.setItem(1, 3, InventoryItem.builder()
                    .item(plugin.getConfigurations().getPluginConfiguration().getItemBuilder("inventories.ranking.buttons.previous-page").clone())
                    .consumer(event -> {
                        plugin.getInventoryManager().getRankingInventory().getPersonalInventoryData(player).setOpenedPage(page - 1);
                    })
                    .update(true)
                    .build());
        } else {
            content.setItem(1, 3, InventoryItem.builder().item(plugin.getConfigurations().getPluginConfiguration().getItemBuilder("inventories.ranking.buttons.background").clone()).build());
        }

        if (!pagination.isLast(page)) {
            content.setItem(1, 7, InventoryItem.builder()
                    .item(plugin.getConfigurations().getPluginConfiguration().getItemBuilder("inventories.ranking.buttons.next-page").clone())
                    .consumer(event -> {
                        plugin.getInventoryManager().getRankingInventory().getPersonalInventoryData(player).setOpenedPage(page + 1);
                    })
                    .update(true)
                    .build());
        } else {
            content.setItem(1, 7, InventoryItem.builder().item(plugin.getConfigurations().getPluginConfiguration().getItemBuilder("inventories.ranking.buttons.background").clone()).build());
        }
    }

    @Override
    public void update(Player player, InventoryContent content) {

    }
}
