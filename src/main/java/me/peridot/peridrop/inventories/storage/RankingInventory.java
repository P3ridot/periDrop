package me.peridot.peridrop.inventories.storage;

import api.peridot.periapi.configuration.ConfigurationFile;
import api.peridot.periapi.inventories.InventoryContent;
import api.peridot.periapi.inventories.Pagination;
import api.peridot.periapi.inventories.items.InventoryItem;
import api.peridot.periapi.inventories.providers.InventoryProvider;
import api.peridot.periapi.items.ItemBuilder;
import api.peridot.periapi.packets.SignInput;
import api.peridot.periapi.utils.replacements.Replacement;
import api.peridot.periapi.utils.replacements.ReplacementUtil;
import me.peridot.peridrop.PeriDrop;
import me.peridot.peridrop.data.configuration.PluginConfiguration;
import me.peridot.peridrop.user.rank.Rank;
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
        PluginConfiguration config = this.plugin.getPluginConfiguration();
        ConfigurationFile inventoriesConfig = this.plugin.getInventoriesConfiguration();

        Pagination pagination = new Pagination();
        int page = this.plugin.getInventoryManager().getRankingInventory().getPersonalInventoryData(player).getOpenedPage();

        content.clear();
        content.fillRow(1, InventoryItem.builder().item(inventoriesConfig.getItemBuilder("ranking.buttons.background").clone()).build());

        List<InventoryItem> items = new ArrayList<>();

        boolean usePlayerAsSkullOwner = inventoriesConfig.getBoolean("ranking.buttons.rank.use_player_as_skull_owner");

        for (Rank rank : this.plugin.getRankSystem().getRanksList()) {
            ItemBuilder item = inventoriesConfig.getItemBuilder("ranking.buttons.rank").clone();
            item.replaceInName(new Replacement("{NAME}", rank.getIdentifierName()),
                    new Replacement("{POSITION}", rank.getPosition() + 1),
                    new Replacement("{LEVEL}", rank.getLevel()),
                    new Replacement("{XP}", rank.getXp()));
            item.replaceInLore(new Replacement("{NAME}", rank.getIdentifierName()),
                    new Replacement("{POSITION}", rank.getPosition() + 1),
                    new Replacement("{LEVEL}", rank.getLevel()),
                    new Replacement("{XP}", rank.getXp()));
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
        ItemBuilder emptyRank = inventoriesConfig.getItemBuilder("ranking.buttons.empty-rank");
        if (emptyRank != null && emptyRank.build().getType() != Material.AIR) {
            content.iterator(InventoryItem.builder()
                    .item(emptyRank.clone()).build())
                    .onlyEmpty(true)
                    .slotFrom(9)
                    .slotTo(45)
                    .iterate();
        }

        content.fillRow(6, InventoryItem.builder().item(inventoriesConfig.getItemBuilder("ranking.buttons.background").clone()).build());
        content.setItem(6, 5, InventoryItem.builder()
                .item(inventoriesConfig.getItemBuilder("ranking.buttons.back").clone())
                .consumer(event -> this.plugin.getInventoryManager().getMenuInventory().open(player))
                .build());

        ItemBuilder currentPage = inventoriesConfig.getItemBuilder("ranking.buttons.current-page").clone();
        currentPage.replaceInName(new Replacement("{PAGE}", page + 1),
                new Replacement("{PAGE-COUNT}", pagination.getPageCount()));
        currentPage.replaceInLore(new Replacement("{PAGE}", page + 1),
                new Replacement("{PAGE-COUNT}", pagination.getPageCount()));

        content.setItem(1, 5, InventoryItem.builder()
                .item(currentPage)
                .consumer(event -> {
                    SignInput.builder()
                            .plugin(this.plugin)
                            .text(config.getColoredStringList("messages.ranking.sign.default-text"))
                            .completeFunction((playerSign, text) -> {
                                if (text[0] == null || text[0].isEmpty()) {
                                    return SignInput.response().close();
                                }
                                int pageInput = 0;
                                try {
                                    pageInput = Integer.parseInt(text[0]);
                                } catch (Exception ex) {
                                    return SignInput.response().text(config.getColoredStringList("messages.ranking.sign.not-number"));
                                }
                                if (pageInput < 1) {
                                    return SignInput.response().text(config.getColoredStringList("messages.ranking.sign.page-smaller-than-one"));
                                }
                                if (pageInput > pagination.getPageCount()) {
                                    return SignInput.response().text(ReplacementUtil.replace(config.getColoredStringList("messages.ranking.sign.page-not-exist"), new Replacement("{PAGE}", text)));
                                }
                                int finalPageInput = pageInput;
                                this.plugin.getServer().getScheduler().runTaskLater(this.plugin, () -> this.plugin.getInventoryManager().getRankingInventory().open(playerSign, finalPageInput - 1), 2L);
                                return SignInput.response().close();
                            })
                            .build().open(player);
                })
                .build());

        if (!pagination.isFirst(page)) {
            content.setItem(1, 3, InventoryItem.builder()
                    .item(inventoriesConfig.getItemBuilder("ranking.buttons.previous-page").clone())
                    .consumer(event -> {
                        this.plugin.getInventoryManager().getRankingInventory().getPersonalInventoryData(player).setOpenedPage(page - 1);
                    })
                    .update(true)
                    .build());
        } else {
            content.setItem(1, 3, InventoryItem.builder().item(inventoriesConfig.getItemBuilder("ranking.buttons.background").clone()).build());
        }

        if (!pagination.isLast(page)) {
            content.setItem(1, 7, InventoryItem.builder()
                    .item(inventoriesConfig.getItemBuilder("ranking.buttons.next-page").clone())
                    .consumer(event -> {
                        this.plugin.getInventoryManager().getRankingInventory().getPersonalInventoryData(player).setOpenedPage(page + 1);
                    })
                    .update(true)
                    .build());
        } else {
            content.setItem(1, 7, InventoryItem.builder().item(inventoriesConfig.getItemBuilder("ranking.buttons.background").clone()).build());
        }
    }

    @Override
    public void update(Player player, InventoryContent content) {

    }

}
