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
        this.plugin.saveDefaultConfig();
        this.pluginConfiguration = new PluginConfiguration(this.plugin);
        this.pluginConfiguration.reloadConfiguration();

        this.inventoriesConfiguration = new ConfigurationFile(this.plugin, "inventories", "inventories");
        this.inventoriesConfiguration.reloadConfiguration();

        this.dropsConfiguration = new ConfigurationFile(this.plugin, "drops", "drops");
        this.dropsConfiguration.reloadConfiguration();
        this.dropsManager = new DropsManager(this.plugin);
        this.dropsManager.loadDrops();

        this.messagesConfiguration = new ConfigurationFile(this.plugin, "messages", "messages");
        this.messagesConfiguration.reloadConfiguration();
        this.lang = new LangAPI(this.messagesConfiguration.getYamlConfiguration().getConfigurationSection("messages"));
    }

    public PluginConfiguration getPluginConfiguration() {
        if (this.pluginConfiguration == null) {
            try {
                this.reloadConfigurations();
            } catch (InvalidConfigurationException ex) {
                ex.printStackTrace();
                this.plugin.getServer().getPluginManager().disablePlugin(this.plugin);
            }
        }
        return this.pluginConfiguration;
    }

    public ConfigurationFile getInventoriesConfiguration() {
        if (this.inventoriesConfiguration == null) {
            try {
                this.reloadConfigurations();
            } catch (InvalidConfigurationException ex) {
                ex.printStackTrace();
                this.plugin.getServer().getPluginManager().disablePlugin(this.plugin);
            }
        }
        return this.inventoriesConfiguration;
    }

    public ConfigurationFile getDropsConfiguration() {
        if (this.dropsConfiguration == null) {
            try {
                this.reloadConfigurations();
            } catch (InvalidConfigurationException ex) {
                ex.printStackTrace();
                this.plugin.getServer().getPluginManager().disablePlugin(this.plugin);
            }
        }
        return this.dropsConfiguration;
    }

    public DropsManager getDropsManager() {
        if (this.dropsManager == null) {
            try {
                this.reloadConfigurations();
            } catch (InvalidConfigurationException ex) {
                ex.printStackTrace();
                this.plugin.getServer().getPluginManager().disablePlugin(this.plugin);
            }
        }
        return this.dropsManager;
    }

    public ConfigurationFile getMessagesConfiguration() {
        if (this.messagesConfiguration == null) {
            try {
                this.reloadConfigurations();
            } catch (InvalidConfigurationException ex) {
                ex.printStackTrace();
                this.plugin.getServer().getPluginManager().disablePlugin(this.plugin);
            }
        }
        return this.messagesConfiguration;
    }

    public LangAPI getLang() {
        if (this.lang == null) {
            try {
                this.reloadConfigurations();
            } catch (InvalidConfigurationException ex) {
                ex.printStackTrace();
                this.plugin.getServer().getPluginManager().disablePlugin(this.plugin);
            }
        }
        return this.lang;
    }

}
