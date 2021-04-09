package me.peridot.peridrop.data.database;

import me.peridot.peridrop.PeriDrop;
import me.peridot.peridrop.user.SettingsType;
import me.peridot.peridrop.user.User;
import me.peridot.peridrop.user.rank.Rank;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class UserDatabase {

    private final PeriDrop plugin;
    private final DatabaseManager databaseManager;

    public UserDatabase(PeriDrop plugin, DatabaseManager databaseManager) {
        this.plugin = plugin;
        this.databaseManager = databaseManager;
    }

    public void loadUser(User user) {
        this.databaseManager.initTable();

        String sql = "SELECT * FROM `" + this.databaseManager.tableName + "` WHERE `uuid`=?;";

        Rank rank = user.getRank();

        try (Connection connection = this.databaseManager.database.getConnection();
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
                rank.setModified(false);
                user.setModified(false);
            }
        } catch (SQLException ex) {
            this.plugin.getLogger().severe("Failed to load rank data!");
            ex.printStackTrace();
        }
    }

    public void loadUserAsync(User user) {
        this.plugin.getServer().getScheduler().runTaskAsynchronously(this.plugin, () -> this.loadUser(user));
    }

    public void saveUser(User user) {
        this.databaseManager.initTable();

        String sql = "";
        if (this.databaseManager.useMysql) {
            sql = "INSERT INTO `" + this.databaseManager.tableName + "` (`uuid`, `name`, `level`, `xp`, `disabled_settings`) VALUES (?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE `level`=?, `xp`=?, `disabled_settings`=?;";
        } else {
            sql = "INSERT OR REPLACE INTO `" + this.databaseManager.tableName + "` (`uuid`, `name`, `level`, `xp`, `disabled_settings`) VALUES (?, ?, ?, ?, ?);";
        }

        Rank rank = user.getRank();

        try (Connection connection = this.databaseManager.database.getConnection();
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

            if (this.databaseManager.useMysql) {
                statement.setInt(6, rank.getLevel());
                statement.setInt(7, rank.getXp());
                statement.setString(8, disabledSettings.toString().replaceFirst(";", ""));
            }

            user.setModified(false);
            rank.setModified(false);

            statement.executeUpdate();
        } catch (SQLException ex) {
            this.plugin.getLogger().severe("Failed to save rank data!");
            ex.printStackTrace();
        }
    }

    public void saveUserAsync(User user) {
        this.plugin.getServer().getScheduler().runTaskAsynchronously(this.plugin, () -> this.saveUser(user));
    }

    public void saveUsers(Set<User> users) {
        this.databaseManager.initTable();

        String sql = "";
        if (this.databaseManager.useMysql) {
            sql = "INSERT INTO `" + this.databaseManager.tableName + "` (`uuid`, `name`, `level`, `xp`, `disabled_settings`) VALUES (?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE `level`=?, `xp`=?, `disabled_settings`=?;";
        } else {
            sql = "INSERT OR REPLACE INTO `" + this.databaseManager.tableName + "` (`uuid`, `name`, `level`, `xp`, `disabled_settings`) VALUES (?, ?, ?, ?, ?);";
        }

        try (Connection connection = this.databaseManager.database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            int i = 0;
            for (User user : users) {
                Rank rank = user.getRank();

                StringBuilder disabledSettings = new StringBuilder();
                for (SettingsType setting : user.getDisabledSettings()) {
                    disabledSettings.append(";").append(setting.name().toUpperCase());
                }
                statement.setString(1, user.getUuid().toString());
                statement.setString(2, user.getName());
                statement.setInt(3, rank.getLevel());
                statement.setInt(4, rank.getXp());
                statement.setString(5, disabledSettings.toString().replaceFirst(";", ""));
                if (this.databaseManager.useMysql) {
                    statement.setInt(6, rank.getLevel());
                    statement.setInt(7, rank.getXp());
                    statement.setString(8, disabledSettings.toString().replaceFirst(";", ""));
                }

                user.setModified(false);
                rank.setModified(false);

                statement.addBatch();
                i++;

                if (i % 1000 == 0 || i == users.size()) {
                    statement.executeBatch();
                }
            }
        } catch (SQLException ex) {
            this.plugin.getLogger().severe("Failed to save rank data!");
            ex.printStackTrace();
        }
    }

    public void saveUsersAsync(Set<User> users) {
        this.plugin.getServer().getScheduler().runTaskAsynchronously(this.plugin, () -> this.saveUsers(users));
    }

}
