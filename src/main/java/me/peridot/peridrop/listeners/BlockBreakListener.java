package me.peridot.peridrop.listeners;

import api.peridot.periapi.configuration.langapi.LangAPI;
import api.peridot.periapi.utils.Pair;
import api.peridot.periapi.utils.Sounds;
import api.peridot.periapi.utils.replacements.Replacement;
import com.udojava.evalex.Expression;
import me.peridot.peridrop.PeriDrop;
import me.peridot.peridrop.data.configuration.PluginConfiguration;
import me.peridot.peridrop.drop.Drop;
import me.peridot.peridrop.drop.DropsManager;
import me.peridot.peridrop.drop.fortune.FortuneDrop;
import me.peridot.peridrop.user.SettingsType;
import me.peridot.peridrop.user.User;
import me.peridot.peridrop.user.rank.Rank;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.concurrent.ThreadLocalRandom;

public class BlockBreakListener implements Listener {

    private final PeriDrop plugin;

    public BlockBreakListener(PeriDrop plugin) {
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        PluginConfiguration config = this.plugin.getPluginConfiguration();
        DropsManager dropsManager = this.plugin.getDropsManager();
        LangAPI lang = this.plugin.getLang();

        Player player = event.getPlayer();
        User user = this.plugin.getUserCache().createUser(player);
        Rank rank = user.getRank();
        ItemStack tool = player.getItemInHand();
        Block block = event.getBlock();
        Location blockLocation = block.getLocation();

        if (PluginConfiguration.blockedDropsMap.containsKey(block.getType())) {
            if (PluginConfiguration.blockedDropsMap.get(block.getType())) {
                if (tool.getEnchantmentLevel(Enchantment.SILK_TOUCH) >= 1) return;
                lang.sendMessage(player, "errors.blocked-drop.silktouch", new Replacement("{BLOCK}", block.getType().name().toUpperCase()));
            } else {
                lang.sendMessage(player, "errors.blocked-drop.not-silktouch", new Replacement("{BLOCK}", block.getType().name().toUpperCase()));
            }
            block.setType(Material.AIR);
            event.setCancelled(true);
            return;
        }

        if (PluginConfiguration.dropExpMap.containsKey(block.getType())) {
            Pair<Integer, Integer> expPair = PluginConfiguration.dropExpMap.get(block.getType());
            int exp = 0;
            if (expPair.getKey().equals(expPair.getValue())) {
                exp = expPair.getKey();
            } else {
                exp = ThreadLocalRandom.current().nextInt(Math.min(expPair.getKey(), expPair.getValue()), Math.max(expPair.getKey(), expPair.getValue()) + 1);
            }
            player.giveExp(exp);
        }

        if (tool == null || tool.getType() == Material.AIR) return;
        if (block.getType() != Material.STONE) return;

        if (user.isSettingDisabled(SettingsType.COBBLESTONE_DROP)) {
            event.setCancelled(true);
            block.setType(Material.AIR);
            this.recalculateDurability(player, tool);
        }

        rank.changeXp(ThreadLocalRandom.current().nextInt(config.getInt("ranking.drop-exp.min"), config.getInt("ranking.drop-exp.max") + 1));
        if (rank.getXp() >= this.requiredExp(rank.getLevel() + 1, rank)) {
            rank.changeXp(-this.requiredExp(rank.getLevel() + 1, rank));
            if (rank.getXp() < 0) rank.setXp(0);
            rank.changeLevel(1);
            if (!user.isSettingDisabled(SettingsType.LEVEL_UP_NOTIFICATION)) {
                lang.sendMessage(player, "ranking.level-up", new Replacement("{OLD-LEVEL}", rank.getLevel() - 1),
                        new Replacement("{NEW-LEVEL}", rank.getLevel()),
                        new Replacement("{NEXT-LEVEL}", rank.getLevel() + 1),
                        new Replacement("{EXP-TO-NEXT-LEVEL}", this.requiredExp(rank.getLevel() + 1, rank)));
            }
            this.plugin.getDatabaseManager().getUserDatabase().saveUser(user);
            this.plugin.getRankSystem().update(rank);
        }

        if (dropsManager.getDropsList().isEmpty()) return;

        for (Drop drop : dropsManager.getDropsList()) {
            float chance = drop.getChance();

            int minAmount = drop.getMinAmount();
            int maxAmount = drop.getMaxAmount();

            FortuneDrop fortuneDrop = drop.getFortuneDropForTool(tool);
            if (fortuneDrop != null) {
                chance = fortuneDrop.getChance();

                minAmount = fortuneDrop.getMinAmount();
                maxAmount = fortuneDrop.getMaxAmount();
            }

            if (chance >= Math.random()) {
                if (!drop.acceptTool(tool.getType())) continue;
                if (!drop.acceptBiome(block.getBiome())) continue;
                if (!drop.acceptHeight(block.getY())) continue;
                if (user.isDropDisabled(drop)) continue;

                int amount = 1;
                if (minAmount == maxAmount) {
                    amount = minAmount;
                } else {
                    amount = ThreadLocalRandom.current().nextInt(Math.min(minAmount, maxAmount), Math.max(minAmount, maxAmount) + 1);
                }
                ItemStack dropItem = drop.getItem()
                        .clone()
                        .setAmount(amount)
                        .replaceInName(new Replacement("{NAME}", player.getName()),
                                new Replacement("{DISPLAYNAME}", player.getDisplayName()),
                                new Replacement("{TOOL}", tool.getType().name().toUpperCase()),
                                new Replacement("{DATE}", PluginConfiguration.dateFormat.format(LocalDateTime.now())))
                        .replaceInLore(new Replacement("{NAME}", player.getName()),
                                new Replacement("{DISPLAYNAME}", player.getDisplayName()),
                                new Replacement("{TOOL}", tool.getType().name().toUpperCase()),
                                new Replacement("{DATE}", PluginConfiguration.dateFormat.format(LocalDateTime.now())))
                        .build();

                blockLocation.getWorld().dropItemNaturally(this.center(blockLocation), dropItem);

                if (!user.isSettingDisabled(SettingsType.DROP_NOTIFICATION)) {
                    drop.getMessage().send(player);
                }
            }
        }
    }

    private int requiredExp(int level, Rank rank) {
        Expression expression = new Expression(this.plugin.getPluginConfiguration().getString("ranking.level-up.required-exp"));

        expression.with("level", new BigDecimal(level));
        expression.with("player_position", new BigDecimal(rank.getPosition()));
        expression.with("player_level", new BigDecimal(rank.getLevel()));

        return Math.round(expression.eval().floatValue());
    }

    private void recalculateDurability(Player player, final ItemStack item) {
        if (player.getGameMode() == GameMode.CREATIVE) return;
        if (item.getType().getMaxDurability() == 0) {
            return;
        }
        final int enchantLevel = item.getEnchantmentLevel(Enchantment.DURABILITY);
        final short d = item.getDurability();
        if (enchantLevel > 0) {
            if ((100 / (enchantLevel + 1) / 100F) > Math.random()) {
                if (d == item.getType().getMaxDurability()) {
                    player.getInventory().clear(player.getInventory().getHeldItemSlot());
                    player.playSound(player.getLocation(), Sounds.ITEM_BREAK.bukkitSound(), 1.0f, 1.0f);
                } else {
                    item.setDurability((short) (d + 1));
                }
            }
        } else if (d == item.getType().getMaxDurability()) {
            player.getInventory().clear(player.getInventory().getHeldItemSlot());
            player.playSound(player.getLocation(), Sounds.ITEM_BREAK.bukkitSound(), 1.0f, 1.0f);
        } else {
            item.setDurability((short) (d + 1));
        }
    }


    private Location center(Location location) {
        location.setX(location.getBlockX() + 0.5);
        location.setY(location.getBlockY() + 0.5);
        location.setZ(location.getBlockZ() + 0.5);
        return location;
    }

}
