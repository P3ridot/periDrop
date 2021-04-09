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
    private final UserDatabase userDatabase;
    private final RankDatabase rankDatabase;
    public HikariDataSource database;
    public boolean useMysql;
    public String tableName;
    public int batchLength;

    public DatabaseManager(PeriDrop plugin) {
        this.plugin = plugin;
        this.userDatabase = new UserDatabase(plugin, this);
        this.rankDatabase = new RankDatabase(plugin, this);
    }

    public void init() {
        FileConfiguration configuration = this.plugin.getConfig();
        ConfigurationSection configurationSection = configuration.getConfigurationSection("config.database");

        this.useMysql = configurationSection.getString("type").equalsIgnoreCase("MYSQL");
        this.tableName = configurationSection.getString("tableName");
        this.batchLength = configurationSection.getInt("batchLength");

        this.database = new HikariDataSource();

        if (this.useMysql) {
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
                this.plugin.getServer().getPluginManager().disablePlugin(this.plugin);
            }

            File sqliteFile = new File(this.plugin.getDataFolder(), configurationSection.getString("sqlite.fileName"));
            if (!sqliteFile.exists()) {
                try {
                    if (!sqliteFile.createNewFile()) {
                        this.plugin.getLogger().severe("Failed to create SQLite database file!");
                        this.plugin.getServer().getPluginManager().disablePlugin(this.plugin);
                    }
                } catch (IOException exception) {
                    exception.printStackTrace();
                    this.plugin.getLogger().severe("Failed to create SQLite database file!");
                    this.plugin.getServer().getPluginManager().disablePlugin(this.plugin);
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
            this.plugin.getServer().getPluginManager().disablePlugin(this.plugin);
        }
    }

    public void initTable() {
        String sql = "CREATE TABLE IF NOT EXISTS `" + this.tableName +
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
            this.plugin.getServer().getPluginManager().disablePlugin(this.plugin);
        }
    }

    public UserDatabase getUserDatabase() {
        return this.userDatabase;
    }

    public RankDatabase getRankDatabase() {
        return this.rankDatabase;
    }

}
