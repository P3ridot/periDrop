package me.peridot.peridrop.data.database;

import me.peridot.peridrop.PeriDrop;
import me.peridot.peridrop.user.SettingsType;
import me.peridot.peridrop.user.User;
import me.peridot.peridrop.user.rank.Rank;
import org.bukkit.Bukkit;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDatabase {

    private final PeriDrop plugin;
    private final DatabaseManager databaseManager;

    public UserDatabase(PeriDrop plugin, DatabaseManager databaseManager) {
        this.plugin = plugin;
        this.databaseManager = databaseManager;
    }

    public void loadUser(User user) {
        databaseManager.initTable();

        String sql = "SELECT * FROM `" + databaseManager.tableName + "` WHERE `uuid`=?;";

        Rank rank = user.getRank();

        try (Connection connection = databaseManager.database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, user.getUuid().toString());

            ResultSet result = statement.executeQuery();
            if (result.next()) {
                rank.setLevel(result.getInt("level"));
                rank.setXp(result.getInt("xp"));

                List<SettingsType> disabledSettings = new ArrayList<>();
                String[] rawDisabledSettingsSplited = result.getString("disabled_settings").split(";");
                for (String disabledSetting : rawDisabledSettingsSplited) {
                    if (disabledSetting.isEmpty() || disabledSetting == null) continue;
                    disabledSettings.add(SettingsType.valueOf(disabledSetting.toUpperCase()));
                }
                disabledSettings.forEach(settingsType -> user.setSettingDisabled(settingsType, true));
            }
        } catch (SQLException ex) {
            this.plugin.getLogger().severe("Failed to load rank data!");
            ex.printStackTrace();
        }
    }

    public void loadUserAsync(User user) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> loadUser(user));
    }

    public void saveUser(User user) {
        databaseManager.initTable();

        String sql = "";
        if (databaseManager.useMysql) {
            sql = "INSERT INTO `" + databaseManager.tableName + "` (`uuid`, `name`, `level`, `xp`, `disabled_settings`) VALUES (?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE `level`=?, `xp`=?, `disabled_settings`=?;";
        } else {
            sql = "INSERT OR REPLACE INTO `" + databaseManager.tableName + "` (`uuid`, `name`, `level`, `xp`, `disabled_settings`) VALUES (?, ?, ?, ?, ?);";
        }

        Rank rank = user.getRank();

        try (Connection connection = databaseManager.database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            StringBuilder disabledSettings = new StringBuilder();
            for (SettingsType setting : user.getDisabledSettings()) {
                disabledSettings.append(";").append(setting.name().toUpperCase());
            }

            statement.setString(1, user.getUuid().toString());
            statement.setString(2, user.getName());
            statement.setInt(3, rank.getLevel());
            statement.setInt(4, rank.getXp());
            statement.setString(5, disabledSettings.toString().replaceFirst(";", ""));

            if (databaseManager.useMysql) {
                statement.setInt(6, rank.getLevel());
                statement.setInt(7, rank.getXp());
                statement.setString(8, disabledSettings.toString().replaceFirst(";", ""));
            }

            statement.executeUpdate();
        } catch (SQLException ex) {
            this.plugin.getLogger().severe("Failed to save rank data!");
            ex.printStackTrace();
        }
    }

    public void saveUserAsync(User user) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> saveUser(user));
    }
}
