package me.peridot.peridrop.drop;

import api.peridot.periapi.items.ItemBuilder;
import api.peridot.periapi.items.ItemParser;
import api.peridot.periapi.langapi.LangMessage;
import api.peridot.periapi.utils.ColorUtil;
import api.peridot.periapi.utils.Pair;
import me.peridot.peridrop.PeriDrop;
import me.peridot.peridrop.drop.fortune.FortuneDrop;
import me.peridot.peridrop.drop.fortune.FortuneDropParser;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;

import java.util.ArrayList;
import java.util.List;

public class DropParser {

    private final PeriDrop plugin;

    public DropParser(PeriDrop plugin) {
        this.plugin = plugin;
    }

    public Drop parseDrop(ConfigurationSection configurationSection) throws InvalidConfigurationException {
        ItemBuilder item = ItemParser.parseItemBuilder(configurationSection.getConfigurationSection("item"));
        if (item == null) {
            throw new InvalidConfigurationException("Invalid item");
        }

        Material material = Material.matchMaterial(configurationSection.getString("item.material"));
        if (material == null) {
            throw new InvalidConfigurationException("Invalid material");
        }

        short durability = (short) configurationSection.getInt("item.durability");

        String displayName = material.name().toUpperCase();
        if (configurationSection.getString("displayname") != null) {
            displayName = ColorUtil.color(configurationSection.getString("displayname"));
        }

        float chance = (float) (configurationSection.getDouble("chance") / 100F);
        if (chance < 0) {
            throw new InvalidConfigurationException("Chance must be bigger than 0");
        }

        if (chance > 1) {
            throw new InvalidConfigurationException("Chance must be smaller than 100");
        }

        String[] splitedConfigurationAmount = configurationSection.getString("amount").split("-", 2);
        int minAmountRange = 1;
        int maxAmountRange = 1;
        try {
            minAmountRange = Integer.parseInt(splitedConfigurationAmount[0]);
            maxAmountRange = Integer.parseInt(splitedConfigurationAmount[1]);
        } catch (Exception ex) {
            throw new InvalidConfigurationException("Invalid amount range value(s)");
        }
        if (minAmountRange > maxAmountRange) {
            throw new InvalidConfigurationException("Minimal amount must be smaller or equal max amount");
        }
        Pair<Integer, Integer> amount = Pair.of(minAmountRange, maxAmountRange);

        String[] splitedConfigurationHeight = configurationSection.getString("height").split("-", 2);
        int minHeightRange = 1;
        int maxHeightRange = 1;
        try {
            minHeightRange = Integer.parseInt(splitedConfigurationHeight[0]);
            maxHeightRange = Integer.parseInt(splitedConfigurationHeight[1]);
        } catch (Exception ex) {
            throw new InvalidConfigurationException("Invalid height range value(s)");
        }
        if (minHeightRange > maxHeightRange) {
            throw new InvalidConfigurationException("Minimal height must be smaller or equal max height");
        }
        Pair<Integer, Integer> height = Pair.of(minHeightRange, maxHeightRange);

        List<Biome> biomesList = new ArrayList<>();
        for (String biomeString : configurationSection.getStringList("biomes")) {
            Biome biome = null;
            try {
                biome = Biome.valueOf(biomeString.toUpperCase());
            } catch (Exception ex) {
                throw new InvalidConfigurationException("Invalid biome name");
            }
            if (biome != null) {
                biomesList.add(biome);
            }
        }

        List<Material> toolsList = new ArrayList<>();
        for (String toolString : configurationSection.getStringList("tools")) {
            Material tool = null;
            try {
                tool = Material.matchMaterial(toolString);
            } catch (Exception ex) {
                throw new InvalidConfigurationException("Invalid tool name");
            }
            if (tool != null) {
                toolsList.add(tool);
            }
        }

        ConfigurationSection fortuneConfigurationSection = configurationSection.getConfigurationSection("fortune");
        List<FortuneDrop> fortuneDropsList = new ArrayList<>();
        if (fortuneConfigurationSection != null) {
            FortuneDropParser fortuneDropParser = new FortuneDropParser();
            fortuneDropsList = fortuneDropParser.parseFortuneDrops(fortuneConfigurationSection);
        }

        LangMessage message = new LangMessage(configurationSection.getConfigurationSection("message"));

        return new Drop(item, material, durability, displayName, chance, amount, height, biomesList, toolsList, fortuneDropsList, message);
    }

    public List<Drop> parseDrops(ConfigurationSection configurationSection) throws InvalidConfigurationException {
        List<Drop> dropsList = new ArrayList<>();

        for (String key : configurationSection.getKeys(false)) {
            ConfigurationSection keyConfigurationSection = configurationSection.getConfigurationSection(key);

            Drop drop = parseDrop(keyConfigurationSection);

            if (drop != null) {
                dropsList.add(drop);
            }
        }

        return dropsList;
    }

}
