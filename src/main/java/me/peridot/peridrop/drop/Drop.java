package me.peridot.peridrop.drop;

import api.peridot.periapi.configuration.langapi.LangMessage;
import api.peridot.periapi.items.ItemBuilder;
import api.peridot.periapi.utils.Pair;
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

    public ItemBuilder getItem() {
        return this.item;
    }

    public Material getMaterial() {
        return this.material;
    }

    public short getDurability() {
        return this.durability;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public float getChance() {
        return this.chance;
    }

    public Pair<Integer, Integer> getAmount() {
        return this.amount;
    }

    public int getMinAmount() {
        return this.amount.getKey();
    }

    public int getMaxAmount() {
        return this.amount.getValue();
    }

    public Pair<Integer, Integer> getHeight() {
        return this.height;
    }

    public int getMinHeight() {
        return this.height.getKey();
    }

    public int getMaxHeight() {
        return this.height.getValue();
    }

    public boolean acceptHeight(int height) {
        int minHeight = this.getMinHeight();
        int maxHeight = this.getMaxHeight();

        return height >= minHeight && height <= maxHeight;
    }

    public List<Biome> getBiomesList() {
        return new ArrayList<Biome>(this.biomesList);
    }

    private boolean isAllBiomes() {
        return this.getBiomesList().isEmpty();
    }

    public boolean acceptBiome(Biome biome) {
        if (this.isAllBiomes()) return true;
        return this.getBiomesList().contains(biome);
    }

    public String getBiomesListString() {
        StringBuilder biomes = new StringBuilder();

        if (this.biomesList.isEmpty()) return PluginConfiguration.biome_any;

        for (Biome biome : this.biomesList) {
            biomes.append(", " + biome.name().toUpperCase());
        }

        return biomes.toString().replaceFirst(", ", "");
    }

    public List<Material> getToolsList() {
        return new ArrayList<Material>(this.toolsList);
    }

    private boolean isAllTools() {
        return this.getToolsList().isEmpty();
    }

    public boolean acceptTool(Material tool) {
        if (this.isAllTools()) return true;
        return this.getToolsList().contains(tool);
    }

    public String getToolsListString() {
        StringBuilder tools = new StringBuilder();

        if (this.toolsList.isEmpty()) return PluginConfiguration.tool_any;

        for (Material material : this.toolsList) {
            tools.append(", " + material.name().toUpperCase());
        }

        return tools.toString().replaceFirst(", ", "");
    }

    public List<FortuneDrop> getFortuneDropsList() {
        return new ArrayList<>(this.fortuneDropsList);
    }

    public boolean isFortuneAffect() {
        return !this.getFortuneDropsList().isEmpty();
    }

    public FortuneDrop getFortuneDropForTool(ItemStack tool) {
        if (tool.getItemMeta() == null) return null;

        int toolFortuneLevel = tool.getItemMeta().getEnchantLevel(Enchantment.LOOT_BONUS_BLOCKS);

        if (this.isFortuneAffect() && toolFortuneLevel >= 1) {
            FortuneDrop fortuneDropReturn = null;

            for (FortuneDrop fortuneDrop : this.getFortuneDropsList()) {
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

    public LangMessage getMessage() {
        return this.message;
    }

}
