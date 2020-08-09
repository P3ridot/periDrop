package me.peridot.peridrop;

import api.peridot.periapi.PeriAPI;
import api.peridot.periapi.configuration.ConfigurationFile;
import api.peridot.periapi.configuration.langapi.LangAPI;
import me.peridot.peridrop.commands.AdminDropCommand;
import me.peridot.peridrop.commands.DropCommand;
import me.peridot.peridrop.data.configuration.ConfigurationManager;
import me.peridot.peridrop.data.configuration.PluginConfiguration;
import me.peridot.peridrop.data.database.DatabaseManager;
import me.peridot.peridrop.drop.DropsManager;
import me.peridot.peridrop.inventories.InventoryManager;
import me.peridot.peridrop.listeners.AsyncPlayerChatListener;
import me.peridot.peridrop.listeners.BlockBreakListener;
import me.peridot.peridrop.listeners.PlayerJoinListener;
import me.peridot.peridrop.listeners.PlayerQuitListener;
import me.peridot.peridrop.schedulers.AutoSaveScheduler;
import me.peridot.peridrop.schedulers.RankingUpdateScheduler;
import me.peridot.peridrop.user.User;
import me.peridot.peridrop.user.UserCache;
import me.peridot.peridrop.user.rank.RankSystem;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class PeriDrop extends JavaPlugin {

    private PeriAPI periAPI;
    private ConfigurationManager configurationManager;
    private DatabaseManager databaseManager;
    private UserCache userCache;
    private RankSystem rankSystem;
    private InventoryManager inventoryManager;

    private static PeriDrop INSTANCE;

    @Override
    public void onEnable() {
        periAPI = new PeriAPI(this);
        periAPI.init();

        configurationManager = new ConfigurationManager(this);
        try {
            configurationManager.reloadConfigurations();
        } catch (InvalidConfigurationException ex) {
            ex.printStackTrace();
            getServer().getPluginManager().disablePlugin(this);
        }

        databaseManager = new DatabaseManager(this);
        databaseManager.init();

        userCache = new UserCache(this);
        rankSystem = new RankSystem();

        initInventoryManager();

        PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(new AsyncPlayerChatListener(this), this);
        pluginManager.registerEvents(new BlockBreakListener(this), this);
        pluginManager.registerEvents(new PlayerJoinListener(this), this);
        pluginManager.registerEvents(new PlayerQuitListener(this), this);

        new AdminDropCommand(this).registerCommand();
        new DropCommand(this).registerCommand();

        new AutoSaveScheduler(this).start();
        new RankingUpdateScheduler(this).start();

        INSTANCE = this;
    }

    @Override
    public void onDisable() {
        for (Player player : getServer().getOnlinePlayers()) {
            User user = userCache.createUser(player);
            getDatabaseManager().getUserDatabase().saveUser(user);
        }
    }

    public PeriAPI getPeriAPI() {
        return periAPI;
    }

    public ConfigurationManager getConfigurationManager() {
        return configurationManager;
    }

    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }

    public UserCache getUserCache() {
        return userCache;
    }

    public RankSystem getRankSystem() {
        return rankSystem;
    }

    public InventoryManager getInventoryManager() {
        return inventoryManager;
    }

    public LangAPI getLang() {
        return this.configurationManager.getLang();
    }

    public PluginConfiguration getPluginConfiguration() {
        return this.configurationManager.getPluginConfiguration();
    }

    public ConfigurationFile getInventoriesConfiguration() {
        return this.configurationManager.getInventoriesConfiguration();
    }

    public ConfigurationFile getDropsConfiguration() {
        return this.configurationManager.getDropsConfiguration();
    }

    public DropsManager getDropsManager() {
        return this.configurationManager.getDropsManager();
    }

    public void initInventoryManager() {
        this.inventoryManager = new InventoryManager(this);
    }

    /*
        Only use for plugins hooks
     */
    public static PeriDrop getInstancee() {
        return INSTANCE;
    }

}
