package me.peridot.peridrop.data.configuration;

import api.peridot.periapi.configuration.ConfigurationFile;
import api.peridot.periapi.configuration.langapi.LangAPI;
import me.peridot.peridrop.PeriDrop;
import me.peridot.peridrop.drop.DropsManager;
import org.bukkit.configuration.InvalidConfigurationException;

public class ConfigurationManager {

    private final PeriDrop plugin;

    private PluginConfiguration pluginConfiguration;

    private ConfigurationFile inventoriesConfiguration;

    private ConfigurationFile dropsConfiguration;
    private DropsManager dropsManager;

    private ConfigurationFile messagesConfiguration;
    private LangAPI lang;

    public ConfigurationManager(PeriDrop plugin) {
        this.plugin = plugin;
    }

    public void reloadConfigurations() throws InvalidConfigurationException {
        plugin.saveDefaultConfig();
        pluginConfiguration = new PluginConfiguration(plugin);
        pluginConfiguration.reloadConfiguration();

        inventoriesConfiguration = new ConfigurationFile(this.plugin, "inventories", "inventories");
        inventoriesConfiguration.reloadConfiguration();

        dropsConfiguration = new ConfigurationFile(this.plugin, "drops", "drops");
        dropsConfiguration.reloadConfiguration();
        dropsManager = new DropsManager(plugin);
        dropsManager.loadDrops();

        messagesConfiguration = new ConfigurationFile(this.plugin, "messages", "messages");
        messagesConfiguration.reloadConfiguration();
        lang = new LangAPI(messagesConfiguration.getYamlConfiguration().getConfigurationSection("messages"));
    }

    public PluginConfiguration getPluginConfiguration() {
        if (pluginConfiguration == null) {
            try {
                reloadConfigurations();
            } catch (InvalidConfigurationException ex) {
                ex.printStackTrace();
                plugin.getServer().getPluginManager().disablePlugin(plugin);
            }
        }
        return pluginConfiguration;
    }

    public ConfigurationFile getInventoriesConfiguration() {
        if (inventoriesConfiguration == null) {
            try {
                reloadConfigurations();
            } catch (InvalidConfigurationException ex) {
                ex.printStackTrace();
                plugin.getServer().getPluginManager().disablePlugin(plugin);
            }
        }
        return inventoriesConfiguration;
    }

    public ConfigurationFile getDropsConfiguration() {
        if (dropsConfiguration == null) {
            try {
                reloadConfigurations();
            } catch (InvalidConfigurationException ex) {
                ex.printStackTrace();
                plugin.getServer().getPluginManager().disablePlugin(plugin);
            }
        }
        return dropsConfiguration;
    }

    public DropsManager getDropsManager() {
        if (dropsManager == null) {
            try {
                reloadConfigurations();
            } catch (InvalidConfigurationException ex) {
                ex.printStackTrace();
                plugin.getServer().getPluginManager().disablePlugin(plugin);
            }
        }
        return dropsManager;
    }

    public ConfigurationFile getMessagesConfiguration() {
        if (messagesConfiguration == null) {
            try {
                reloadConfigurations();
            } catch (InvalidConfigurationException ex) {
                ex.printStackTrace();
                plugin.getServer().getPluginManager().disablePlugin(plugin);
            }
        }
        return messagesConfiguration;
    }

    public LangAPI getLang() {
        if (lang == null) {
            try {
                reloadConfigurations();
            } catch (InvalidConfigurationException ex) {
                ex.printStackTrace();
                plugin.getServer().getPluginManager().disablePlugin(plugin);
            }
        }
        return lang;
    }

}
