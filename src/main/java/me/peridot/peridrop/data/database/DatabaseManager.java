package me.peridot.peridrop.data.database;

import com.zaxxer.hikari.HikariDataSource;
import me.peridot.peridrop.PeriDrop;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DatabaseManager {

    private final PeriDrop plugin;

    public HikariDataSource database;

    public boolean useMysql;
    public String tableName;
    public int batchLength;

    private final UserDatabase userDatabase;
    private final RankDatabase rankDatabase;

    public DatabaseManager(PeriDrop plugin) {
        this.plugin = plugin;
        userDatabase = new UserDatabase(plugin, this);
        rankDatabase = new RankDatabase(plugin, this);
    }

    public void init() {
        FileConfiguration configuration = plugin.getConfig();
        ConfigurationSection configurationSection = configuration.getConfigurationSection("config.database");

        this.useMysql = configurationSection.getString("type").equalsIgnoreCase("MYSQL");
        this.tableName = configurationSection.getString("tableName");
        this.batchLength = configurationSection.getInt("batchLength");

        this.database = new HikariDataSource();

        if (useMysql) {
            String hostname = configurationSection.getString("mysql.hostname");
            String port = configurationSection.getString("mysql.port");
            String database = configurationSection.getString("mysql.database");
            String username = configurationSection.getString("mysql.username");
            String password = configurationSection.getString("mysql.password");

            boolean useSSL = configurationSection.getBoolean("mysql.useSSL");

            int poolSize = configurationSection.getInt("mysql.poolSize");
            int connectionTimeout = configurationSection.getInt("mysql.connectionTimeout");

            this.database.setJdbcUrl("jdbc:mysql://" + hostname + ":" + port + "/" + database + "?useSSL=" + useSSL);
            this.database.setUsername(username);
            this.database.setPassword(password);

            this.database.setMaximumPoolSize(poolSize);
            this.database.setConnectionTimeout(connectionTimeout);
        } else {
            try {
                Class.forName("org.sqlite.JDBC");
            } catch (Exception ex) {
                ex.printStackTrace();
                this.plugin.getLogger().severe("Failed to initialize SQLite driver!");
                plugin.getServer().getPluginManager().disablePlugin(plugin);
            }

            File sqliteFile = new File(this.plugin.getDataFolder(), configurationSection.getString("sqlite.fileName"));
            if (!sqliteFile.exists()) {
                try {
                    if (!sqliteFile.createNewFile()) {
                        this.plugin.getLogger().severe("Failed to create SQLite database file!");
                        plugin.getServer().getPluginManager().disablePlugin(plugin);
                    }
                } catch (IOException exception) {
                    exception.printStackTrace();
                    this.plugin.getLogger().severe("Failed to create SQLite database file!");
                    plugin.getServer().getPluginManager().disablePlugin(plugin);
                }
            }

            this.database.setJdbcUrl("jdbc:sqlite:" + sqliteFile.getAbsolutePath());
            this.database.setConnectionTestQuery("SELECT * FROM sqlite_master");
        }

        try (Connection testConnection = this.database.getConnection()) {
            this.plugin.getLogger().info("Test database connection successful!");
        } catch (SQLException ex) {
            ex.printStackTrace();
            this.plugin.getLogger().severe("Test database connection failed!");
            plugin.getServer().getPluginManager().disablePlugin(plugin);
        }
    }

    public void initTable() {
        String sql = "CREATE TABLE IF NOT EXISTS `" + tableName +
                "` (`uuid` VARCHAR(36) NOT NULL," +
                "`name` TEXT NOT NULL," +
                "`level` INT NOT NULL," +
                "`xp` INT NOT NULL," +
                "`disabled_settings` TEXT NULL," +
                "PRIMARY KEY (`uuid`));";

        try (Connection connection = this.database.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
            this.plugin.getLogger().severe("Failed to create database table!");
            plugin.getServer().getPluginManager().disablePlugin(plugin);
        }
    }

    public UserDatabase getUserDatabase() {
        return userDatabase;
    }

    public RankDatabase getRankDatabase() {
        return rankDatabase;
    }
}
