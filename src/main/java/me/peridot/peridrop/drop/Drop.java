package me.peridot.peridrop.drop;

import api.peridot.periapi.items.ItemBuilder;
import api.peridot.periapi.langapi.LangMessage;
import api.peridot.periapi.utils.Pair;
import jdk.nashorn.internal.objects.annotations.Getter;
import me.peridot.peridrop.data.configuration.PluginConfiguration;
import me.peridot.peridrop.drop.fortune.FortuneDrop;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class Drop {

    private final ItemBuilder item;
    private final Material material;
    private final short durability;
    private final String displayName;
    private final float chance;
    private final Pair<Integer, Integer> amount;
    private final Pair<Integer, Integer> height;
    private final List<Biome> biomesList;
    private final List<Material> toolsList;
    private final List<FortuneDrop> fortuneDropsList;
    private final LangMessage message;

    public Drop(ItemBuilder item, Material material, short durability, String displayName, float chance, Pair<Integer, Integer> amount, Pair<Integer, Integer> height, List<Biome> biomesList, List<Material> toolsList, List<FortuneDrop> fortuneDropsList, LangMessage message) {
        this.item = item;
        this.material = material;
        this.durability = durability;
        this.displayName = displayName;
        this.chance = chance;
        this.amount = amount;
        this.height = height;
        this.biomesList = biomesList;
        this.toolsList = toolsList;
        this.fortuneDropsList = fortuneDropsList;
        this.message = message;
    }

    @Getter
    public ItemBuilder getItem() {
        return item;
    }

    @Getter
    public Material getMaterial() {
        return material;
    }

    @Getter
    public short getDurability() {
        return durability;
    }

    @Getter
    public String getDisplayName() {
        return displayName;
    }

    @Getter
    public float getChance() {
        return chance;
    }

    @Getter
    public Pair<Integer, Integer> getAmount() {
        return amount;
    }

    @Getter
    public int getMinAmount() {
        return amount.getKey();
    }

    @Getter
    public int getMaxAmount() {
        return amount.getValue();
    }

    @Getter
    public Pair<Integer, Integer> getHeight() {
        return height;
    }

    @Getter
    public int getMinHeight() {
        return height.getKey();
    }

    @Getter
    public int getMaxHeight() {
        return height.getValue();
    }

    public boolean acceptHeight(int height) {
        int minHeight = getMinHeight();
        int maxHeight = getMaxHeight();

        return height >= minHeight && height <= maxHeight;
    }

    @Getter
    public List<Biome> getBiomesList() {
        return new ArrayList<Biome>(biomesList);
    }

    private boolean isAllBiomes() {
        return getBiomesList().isEmpty();
    }

    public boolean acceptBiome(Biome biome) {
        if (isAllBiomes()) return true;
        return getBiomesList().contains(biome);
    }

    @Getter
    public String getBiomesListString() {
        StringBuilder biomes = new StringBuilder();

        if (biomesList.isEmpty()) return PluginConfiguration.biome_any;

        for (Biome biome : biomesList) {
            biomes.append(", " + biome.name().toUpperCase());
        }

        return biomes.toString().replaceFirst(", ", "");
    }

    @Getter
    public List<Material> getToolsList() {
        return new ArrayList<Material>(toolsList);
    }

    private boolean isAllTools() {
        return getToolsList().isEmpty();
    }

    public boolean acceptTool(Material tool) {
        if (isAllTools()) return true;
        return getToolsList().contains(tool);
    }

    @Getter
    public String getToolsListString() {
        StringBuilder tools = new StringBuilder();

        if (toolsList.isEmpty()) return PluginConfiguration.tool_any;

        for (Material material : toolsList) {
            tools.append(", " + material.name().toUpperCase());
        }

        return tools.toString().replaceFirst(", ", "");
    }

    @Getter
    public List<FortuneDrop> getFortuneDropsList() {
        return new ArrayList<>(fortuneDropsList);
    }

    public boolean isFortuneAffect() {
        return !getFortuneDropsList().isEmpty();
    }

    @Getter
    public FortuneDrop getFortuneDropForTool(ItemStack tool) {
        if (tool.getItemMeta() == null) return null;

        int toolFortuneLevel = tool.getItemMeta().getEnchantLevel(Enchantment.LOOT_BONUS_BLOCKS);

        if (isFortuneAffect() && toolFortuneLevel >= 1) {
            FortuneDrop fortuneDropReturn = null;

            for (FortuneDrop fortuneDrop : getFortuneDropsList()) {
                if (toolFortuneLevel >= fortuneDrop.getFortuneLevel()) {
                    if (fortuneDropReturn == null || fortuneDropReturn.getFortuneLevel() < fortuneDrop.getFortuneLevel()) {
                        fortuneDropReturn = fortuneDrop;
                    }
                }
            }

            return fortuneDropReturn;
        }
        return null;
    }

    @Getter
    public LangMessage getMessage() {
        return message;
    }
}
