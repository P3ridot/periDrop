package me.peridot.peridrop.data.configuration;

import api.peridot.periapi.items.ItemBuilder;
import api.peridot.periapi.items.ItemParser;
import api.peridot.periapi.utils.ColorUtil;
import api.peridot.periapi.utils.Pair;
import me.peridot.peridrop.PeriDrop;
import me.peridot.peridrop.user.SettingsType;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PluginConfiguration {

    private final PeriDrop plugin;

    public static int autosave_delay;
    public static int ranking_update_delay;

    public static DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    public static DecimalFormat decimalFormat = new DecimalFormat("#.00");

    public static String drop_status_enabled;
    public static String drop_status_disabled;
    public static String settings_status_enabled;
    public static String settings_status_disabled;
    public static String fortune_enabled;
    public static String fortune_disabled;
    public static String tool_any;
    public static String biome_any;
    public static String ranking_default_text;
    public static String ranking_not_set;
    public static String ranking_not_number;
    public static String ranking_page_not_exist;
    public static String ranking_page_smaller_than_one;

    public static int ranking_default_level;
    public static int ranking_drop_min_exp;
    public static int ranking_drop_max_exp;
    public static String ranking_required_exp_formula;

    public static String menu_title;
    public static int menu_size;
    public static ItemBuilder menu_background_item;
    public static ItemBuilder menu_drop_stone_item;
    public static ItemBuilder menu_settings_item;
    public static ItemBuilder menu_ranking_item;
    public static ItemBuilder menu_drop_exp_item;
    public static int menu_drop_stone_slot;
    public static int menu_settings_slot;
    public static int menu_ranking_slot;
    public static int menu_drop_exp_slot;
    public static boolean menu_drop_exp_enabled;

    public static String drop_title;
    public static ItemBuilder drop_background_item;
    public static ItemBuilder drop_back_item;
    public static ItemBuilder drop_all_enable_item;
    public static ItemBuilder drop_all_disable_item;
    public static List<String> drop_toogle_drop_with_fortune_lore;
    public static List<String> drop_toogle_drop_without_fortune_lore;

    public static String fortune_title;
    public static ItemBuilder fortune_background_item;
    public static ItemBuilder fortune_back_item;
    public static ItemBuilder fortune_item;
    public static boolean fortune_enchant_with_fortune;

    public static String settings_title;
    public static ItemBuilder settings_background_item;
    public static ItemBuilder settings_back_item;

    public static String ranking_title;
    public static ItemBuilder ranking_background_item;
    public static ItemBuilder ranking_back_item;
    public static ItemBuilder ranking_actual_page_item;
    public static ItemBuilder ranking_next_page_item;
    public static ItemBuilder ranking_previous_page_item;
    public static ItemBuilder ranking_rank_item;
    public static ItemBuilder ranking_rank_empty_item;
    public static boolean ranking_use_player_as_skull_owner;

    public static Map<Material, Boolean> blockedDropsMap = new HashMap<>();
    public static Map<Material, Pair<Integer, Integer>> dropExpMap = new HashMap<>();

    public PluginConfiguration(PeriDrop plugin) {
        this.plugin = plugin;
    }

    public void reloadConfiguration() {
        FileConfiguration configuration = plugin.getConfig();
        ConfigurationSection configurationSection = configuration.getConfigurationSection("config");

        autosave_delay = configurationSection.getInt("tasks.autosave");
        ranking_update_delay = configurationSection.getInt("tasks.ranking-update");

        dateFormat = DateTimeFormatter.ofPattern(configurationSection.getString("format.date"));
        decimalFormat = new DecimalFormat(configurationSection.getString("format.decimal"));

        drop_status_enabled = ColorUtil.color(configurationSection.getString("messages.drop_status.enabled"));
        drop_status_disabled = ColorUtil.color(configurationSection.getString("messages.drop_status.disabled"));
        settings_status_enabled = ColorUtil.color(configurationSection.getString("messages.settings_status.enabled"));
        settings_status_disabled = ColorUtil.color(configurationSection.getString("messages.settings_status.disabled"));
        fortune_enabled = ColorUtil.color(configurationSection.getString("messages.fortune.enabled"));
        fortune_disabled = ColorUtil.color(configurationSection.getString("messages.fortune.disabled"));
        tool_any = ColorUtil.color(configurationSection.getString("messages.tool-any"));
        biome_any = ColorUtil.color(configurationSection.getString("messages.biome-any"));
        ranking_default_text = ColorUtil.color(configurationSection.getString("messages.ranking.default-text"));
        ranking_not_set = ColorUtil.color(configurationSection.getString("messages.ranking.not-set"));
        ranking_not_number = ColorUtil.color(configurationSection.getString("messages.ranking.not-number"));
        ranking_page_not_exist = ColorUtil.color(configurationSection.getString("messages.ranking.page-not-exist"));
        ranking_page_smaller_than_one = ColorUtil.color(configurationSection.getString("messages.ranking.page-smaller-than-one"));

        ranking_default_level = configurationSection.getInt("ranking.default-level");
        ranking_drop_min_exp = configurationSection.getInt("ranking.drop-exp.min");
        ranking_drop_max_exp = configurationSection.getInt("ranking.drop-exp.max");
        ranking_required_exp_formula = configurationSection.getString("ranking.level-up.required-exp");

        menu_title = ColorUtil.color(configurationSection.getString("inventories.menu.title"));
        menu_size = configurationSection.getInt("inventories.menu.size");
        menu_background_item = ItemParser.parseItemBuilder(configurationSection.getConfigurationSection("inventories.menu.buttons.background"));
        menu_drop_stone_item = ItemParser.parseItemBuilder(configurationSection.getConfigurationSection("inventories.menu.buttons.stone"));
        menu_settings_item = ItemParser.parseItemBuilder(configurationSection.getConfigurationSection("inventories.menu.buttons.settings"));
        menu_ranking_item = ItemParser.parseItemBuilder(configurationSection.getConfigurationSection("inventories.menu.buttons.ranking"));
        menu_drop_exp_item = ItemParser.parseItemBuilder(configurationSection.getConfigurationSection("inventories.menu.buttons.drop-exp"));
        menu_drop_stone_slot = configurationSection.getInt("inventories.menu.buttons.stone.slot");
        menu_settings_slot = configurationSection.getInt("inventories.menu.buttons.settings.slot");
        menu_ranking_slot = configurationSection.getInt("inventories.menu.buttons.ranking.slot");
        menu_drop_exp_slot = configurationSection.getInt("inventories.menu.buttons.drop-exp.slot");
        menu_drop_exp_enabled = configurationSection.getBoolean("inventories.menu.buttons.drop-exp.enabled");

        drop_title = ColorUtil.color(configurationSection.getString("inventories.drop.title"));
        drop_background_item = ItemParser.parseItemBuilder(configurationSection.getConfigurationSection("inventories.drop.buttons.background"));
        drop_back_item = ItemParser.parseItemBuilder(configurationSection.getConfigurationSection("inventories.drop.buttons.back"));
        drop_all_enable_item = ItemParser.parseItemBuilder(configurationSection.getConfigurationSection("inventories.drop.buttons.all_enable"));
        drop_all_disable_item = ItemParser.parseItemBuilder(configurationSection.getConfigurationSection("inventories.drop.buttons.all_disable"));
        drop_toogle_drop_with_fortune_lore = ColorUtil.color(configurationSection.getStringList("inventories.drop.buttons.drop_toogle.with_fortune.lore"));
        drop_toogle_drop_without_fortune_lore = ColorUtil.color(configurationSection.getStringList("inventories.drop.buttons.drop_toogle.without_fortune.lore"));

        fortune_title = ColorUtil.color(configurationSection.getString("inventories.fortune.title"));
        fortune_background_item = ItemParser.parseItemBuilder(configurationSection.getConfigurationSection("inventories.fortune.buttons.background"));
        fortune_back_item = ItemParser.parseItemBuilder(configurationSection.getConfigurationSection("inventories.fortune.buttons.back"));
        fortune_item = ItemParser.parseItemBuilder(configurationSection.getConfigurationSection("inventories.fortune.buttons.fortune"));
        fortune_enchant_with_fortune = configurationSection.getBoolean("inventories.fortune.buttons.fortune.enchant_with_fortune");

        settings_title = ColorUtil.color(configurationSection.getString("inventories.settings.title"));
        settings_background_item = ItemParser.parseItemBuilder(configurationSection.getConfigurationSection("inventories.settings.buttons.background"));
        settings_back_item = ItemParser.parseItemBuilder(configurationSection.getConfigurationSection("inventories.settings.buttons.back"));

        ranking_title = ColorUtil.color(configurationSection.getString("inventories.ranking.title"));
        ranking_background_item = ItemParser.parseItemBuilder(configurationSection.getConfigurationSection("inventories.ranking.buttons.background"));
        ranking_back_item = ItemParser.parseItemBuilder(configurationSection.getConfigurationSection("inventories.ranking.buttons.back"));
        ranking_actual_page_item = ItemParser.parseItemBuilder(configurationSection.getConfigurationSection("inventories.ranking.buttons.actual-page"));
        ranking_next_page_item = ItemParser.parseItemBuilder(configurationSection.getConfigurationSection("inventories.ranking.buttons.next-page"));
        ranking_previous_page_item = ItemParser.parseItemBuilder(configurationSection.getConfigurationSection("inventories.ranking.buttons.previous-page"));
        ranking_rank_item = ItemParser.parseItemBuilder(configurationSection.getConfigurationSection("inventories.ranking.buttons.rank"));
        ranking_rank_empty_item = ItemParser.parseItemBuilder(configurationSection.getConfigurationSection("inventories.ranking.buttons.empty-rank"));
        ranking_use_player_as_skull_owner = configurationSection.getBoolean("inventories.ranking.buttons.rank.use_player_as_skull_owner");

        SettingsType.COBBLESTONE_DROP.setEnabled(configurationSection.getBoolean("inventories.settings.buttons.cobblestone_drop.enabled"));
        SettingsType.DROP_NOTIFICATION.setEnabled(configurationSection.getBoolean("inventories.settings.buttons.drop_notification.enabled"));
        SettingsType.LEVEL_UP_NOTIFICATION.setEnabled(configurationSection.getBoolean("inventories.settings.buttons.level_up_notification.enabled"));
        if (SettingsType.COBBLESTONE_DROP.isEnabled()) {
            SettingsType.COBBLESTONE_DROP.setItem(ItemParser.parseItemBuilder(configurationSection.getConfigurationSection("inventories.settings.buttons.cobblestone_drop")));
        }
        if (SettingsType.DROP_NOTIFICATION.isEnabled()) {
            SettingsType.DROP_NOTIFICATION.setItem(ItemParser.parseItemBuilder(configurationSection.getConfigurationSection("inventories.settings.buttons.drop_notification")));
        }
        if (SettingsType.LEVEL_UP_NOTIFICATION.isEnabled()) {
            SettingsType.LEVEL_UP_NOTIFICATION.setItem(ItemParser.parseItemBuilder(configurationSection.getConfigurationSection("inventories.settings.buttons.level_up_notification")));
        }

        blockedDropsMap.clear();
        for (String string : configurationSection.getStringList("blocked-drops")) {
            String[] splited = string.split(":", 2);
            try {
                Material material = Material.matchMaterial(splited[0]);
                boolean silktouch = Boolean.parseBoolean(splited[1]);
                blockedDropsMap.put(material, silktouch);
            } catch (Exception ignored) {
            }
        }

        dropExpMap.clear();
        for (String string : configurationSection.getStringList("drop-exp")) {
            String[] splited = string.split(":", 2);
            try {
                Material material = Material.matchMaterial(splited[0]);
                String[] amountRangeSplited = splited[1].split("-", 2);

                int minRange = 0;
                int maxRange = 0;
                try {
                    minRange = Integer.parseInt(amountRangeSplited[0]);
                    maxRange = Integer.parseInt(amountRangeSplited[1]);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    Bukkit.getLogger().severe("Invalid range value(s)");
                    continue;
                }

                dropExpMap.put(material, Pair.of(minRange, maxRange));
            } catch (Exception ignored) {
            }
        }
    }
}
