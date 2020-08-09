package me.peridot.peridrop.drop.fortune;

import api.peridot.periapi.utils.Pair;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;

import java.util.ArrayList;
import java.util.List;

public class FortuneDropParser {

    public FortuneDrop parseFortuneDrop(ConfigurationSection configurationSection) throws InvalidConfigurationException {
        int fortuneLevel = 0;
        try {
            fortuneLevel = Integer.parseInt(configurationSection.getName());
        } catch (Exception ex) {
            throw new InvalidConfigurationException("Invalid fortune level");
        }
        if (fortuneLevel < 1) {
            throw new InvalidConfigurationException("Fortune level must be bigger or equal 1");
        }

        float chance = (float) (configurationSection.getDouble("chance") / 100F);
        if (chance < 0) {
            throw new InvalidConfigurationException("Chance must be bigger than 0");
        }

        if (chance > 1) {
            throw new InvalidConfigurationException("Chance must be smaller than 100");
        }

        String[] splitedConfigurationAmount = configurationSection.getString("amount").split("-", 2);
        int minRange = 0;
        int maxRange = 0;
        try {
            minRange = Integer.parseInt(splitedConfigurationAmount[0]);
            maxRange = Integer.parseInt(splitedConfigurationAmount[1]);
        } catch (Exception ex) {
            throw new InvalidConfigurationException("Invalid amount range value");
        }
        if (minRange > maxRange) {
            throw new InvalidConfigurationException("Minimal amount must be smaller or equal max amount");
        }
        Pair<Integer, Integer> amount = Pair.of(minRange, maxRange);

        return new FortuneDrop(fortuneLevel, chance, amount);
    }

    public List<FortuneDrop> parseFortuneDrops(ConfigurationSection configurationSection) throws InvalidConfigurationException {
        List<FortuneDrop> fortuneDropsList = new ArrayList<>();

        for (String key : configurationSection.getKeys(false)) {
            ConfigurationSection keyConfigurationSection = configurationSection.getConfigurationSection(key);

            FortuneDrop fortuneDrop = parseFortuneDrop(keyConfigurationSection);

            if (fortuneDrop != null) {
                fortuneDropsList.add(fortuneDrop);
            }
        }

        return fortuneDropsList;
    }

}
