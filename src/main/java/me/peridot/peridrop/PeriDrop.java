package me.peridot.peridrop;

import api.peridot.periapi.PeriAPI;
import me.peridot.peridrop.commands.AdminDropCommand;
import me.peridot.peridrop.commands.DropCommand;
import me.peridot.peridrop.data.configuration.ConfigurationManager;
import me.peridot.peridrop.data.database.DatabaseManager;
import me.peridot.peridrop.inventories.InventoryManager;
import me.peridot.peridrop.listeners.BlockBreakListener;
import me.peridot.peridrop.listeners.PlayerJoinListener;
import me.peridot.peridrop.listeners.PlayerQuitListener;
import me.peridot.peridrop.schedulers.AutoSaveScheduler;
import me.peridot.peridrop.schedulers.RankingUpdateScheduler;
import me.peridot.peridrop.user.User;
import me.peridot.peridrop.user.UserManager;
import me.peridot.peridrop.user.rank.RankManager;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class PeriDrop extends JavaPlugin {

    private ConfigurationManager configurationManager;
    private DatabaseManager databaseManager;
    private UserManager userManager;
    private RankManager rankManager;
    private PeriAPI periAPI;
    private InventoryManager inventoryManager;

    @Override
    public void onEnable() {
        configurationManager = new ConfigurationManager(this);
        try {
            configurationManager.reloadConfigurations();
        } catch (InvalidConfigurationException ex) {
            ex.printStackTrace();
            getServer().getPluginManager().disablePlugin(this);
        }

        databaseManager = new DatabaseManager(this);
        databaseManager.init();

        userManager = new UserManager();
        rankManager = new RankManager();

        periAPI = new PeriAPI(this);
        periAPI.init();

        initInventoryManager();

        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(new BlockBreakListener(this), this);
        pluginManager.registerEvents(new PlayerJoinListener(this), this);
        pluginManager.registerEvents(new PlayerQuitListener(this), this);

        new AdminDropCommand(this).registerCommand();
        new DropCommand(this).registerCommand();

        new AutoSaveScheduler(this).start();
        new RankingUpdateScheduler(this).start();
    }

    @Override
    public void onDisable() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            User user = userManager.createUser(player);
            getDatabaseManager().getUserDatabase().saveUser(user);
        }
    }

    public ConfigurationManager getConfigurations() {
        return configurationManager;
    }

    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }

    public UserManager getUserManager() {
        return userManager;
    }

    public RankManager getRankManager() {
        return rankManager;
    }

    public PeriAPI getPeriAPI() {
        return periAPI;
    }

    public InventoryManager getInventoryManager() {
        return inventoryManager;
    }

    public void initInventoryManager() {
        this.inventoryManager = new InventoryManager(this);
    }
}
