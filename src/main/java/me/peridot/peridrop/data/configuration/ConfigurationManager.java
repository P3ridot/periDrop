package me.peridot.peridrop.data.configuration;

import api.peridot.periapi.configuration.langapi.LangAPI;
import me.peridot.peridrop.PeriDrop;
import me.peridot.peridrop.drop.DropManager;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;

public class ConfigurationManager {

    private final PeriDrop plugin;

    private PluginConfiguration pluginConfiguration;

    private DropConfiguration dropConfiguration;
    private DropManager dropManager;

    private MessagesConfiguration messagesConfiguration;
    private LangAPI lang;

    public ConfigurationManager(PeriDrop plugin) {
        this.plugin = plugin;
    }

    public void reloadConfigurations() throws InvalidConfigurationException {
        plugin.saveDefaultConfig();
        pluginConfiguration = new PluginConfiguration(plugin);
        pluginConfiguration.reloadConfiguration();

        dropConfiguration = new DropConfiguration(plugin);
        dropConfiguration.reloadConfiguration();
        dropManager = new DropManager(plugin);
        dropManager.loadDrops();

        messagesConfiguration = new MessagesConfiguration(plugin);
        messagesConfiguration.reloadConfiguration();
        lang = new LangAPI(messagesConfiguration.getYamlConfiguration().getConfigurationSection("messages"));
    }

    public PluginConfiguration getPluginConfiguration() {
        if (pluginConfiguration == null) {
            try {
                reloadConfigurations();
            } catch (InvalidConfigurationException ex) {
                ex.printStackTrace();
                Bukkit.getPluginManager().disablePlugin(plugin);
            }
        }
        return pluginConfiguration;
    }

    public DropConfiguration getDropConfiguration() {
        if (dropConfiguration == null) {
            try {
                reloadConfigurations();
            } catch (InvalidConfigurationException ex) {
                ex.printStackTrace();
                Bukkit.getPluginManager().disablePlugin(plugin);
            }
        }
        return dropConfiguration;
    }

    public DropManager getDropManager() {
        if (dropManager == null) {
            try {
                reloadConfigurations();
            } catch (InvalidConfigurationException ex) {
                ex.printStackTrace();
                Bukkit.getPluginManager().disablePlugin(plugin);
            }
        }
        return dropManager;
    }

    public MessagesConfiguration getMessagesConfiguration() {
        if (messagesConfiguration == null) {
            try {
                reloadConfigurations();
            } catch (InvalidConfigurationException ex) {
                ex.printStackTrace();
                Bukkit.getPluginManager().disablePlugin(plugin);
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
                Bukkit.getPluginManager().disablePlugin(plugin);
            }
        }
        return lang;
    }
}
