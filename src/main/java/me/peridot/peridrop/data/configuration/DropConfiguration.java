package me.peridot.peridrop.data.configuration;

import me.peridot.peridrop.PeriDrop;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class DropConfiguration {

    private final PeriDrop plugin;

    private YamlConfiguration yamlConfiguration;

    public DropConfiguration(PeriDrop plugin) {
        this.plugin = plugin;
    }

    public void reloadConfiguration() {
        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdir();
        }

        File file = new File(plugin.getDataFolder(), "drops.yml");

        if (!file.exists()) {
            plugin.saveResource("drops.yml", true);
        }

        yamlConfiguration = YamlConfiguration.loadConfiguration(file);
    }

    public YamlConfiguration getYamlConfiguration() {
        return yamlConfiguration;
    }
}
