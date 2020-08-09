package me.peridot.peridrop.data.configuration;

import api.peridot.periapi.configuration.ConfigurationProvider;
import api.peridot.periapi.utils.Pair;
import api.peridot.periapi.utils.simple.ColorUtil;
import me.peridot.peridrop.PeriDrop;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class PluginConfiguration extends ConfigurationProvider {

    private final PeriDrop plugin;

    public static DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    public static DecimalFormat decimalFormat = new DecimalFormat("#.00");

    public static String tool_any;
    public static String biome_any;
    public static int ranking_default_level;

    public static Map<Material, Boolean> blockedDropsMap = new HashMap<>();
    public static Map<Material, Pair<Integer, Integer>> dropExpMap = new HashMap<>();

    public PluginConfiguration(PeriDrop plugin) {
        super(plugin.getConfig().getConfigurationSection("config"));
        this.plugin = plugin;
    }

    public void reloadConfiguration() {
        FileConfiguration configuration = plugin.getConfig();
        ConfigurationSection configurationSection = configuration.getConfigurationSection("config");

        dateFormat = DateTimeFormatter.ofPattern(configurationSection.getString("format.date"));
        decimalFormat = new DecimalFormat(configurationSection.getString("format.decimal"));

        tool_any = ColorUtil.color(configurationSection.getString("messages.tool-any"));
        biome_any = ColorUtil.color(configurationSection.getString("messages.biome-any"));
        ranking_default_level = configurationSection.getInt("ranking.default-level");

        blockedDropsMap.clear();
        for (String string : configurationSection.getStringList("blocked-drops")) {
            String[] splited = string.split(":", 2);
            try {
                Material material = Material.matchMaterial(splited[0]);
                boolean silktouch = Boolean.parseBoolean(splited[1]);
                blockedDropsMap.put(material, silktouch);
            } catch (Exception ex) {
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
                    plugin.getLogger().severe("Invalid range value(s)");
                    continue;
                }

                dropExpMap.put(material, Pair.of(minRange, maxRange));
            } catch (Exception ignored) {
            }
        }
    }

}
