package me.peridot.peridrop.data.database;

import me.peridot.peridrop.PeriDrop;
import me.peridot.peridrop.user.rank.Rank;
import org.bukkit.Bukkit;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class RankDatabase {

    private final PeriDrop plugin;
    private final DatabaseManager databaseManager;

    public RankDatabase(PeriDrop plugin, DatabaseManager databaseManager) {
        this.plugin = plugin;
        this.databaseManager = databaseManager;
    }

    public void loadRanks() {
        plugin.getRankSystem().clear();
        databaseManager.initTable();

        String sql = "SELECT * FROM `" + databaseManager.tableName + "`;";

        try (Connection connection = databaseManager.database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            ResultSet result = statement.executeQuery();
            while (result.next()) {
                UUID uuid = UUID.fromString(result.getString("uuid"));
                String name = result.getString("name");
                int level = result.getInt("level");
                int xp = result.getInt("xp");

                Rank rank = new Rank(uuid, name);
                rank.setLevel(level);
                rank.setXp(xp);
                rank.setModified(false);
                plugin.getRankSystem().update(rank);
            }
        } catch (SQLException ex) {
            this.plugin.getLogger().severe("Failed to load rank data!");
            ex.printStackTrace();
        }
    }

    public void loadRanksAsync() {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, this::loadRanks);
    }

    public void loadRank(Rank rank) {
        databaseManager.initTable();

        String sql = "SELECT * FROM `" + databaseManager.tableName + "` WHERE `uuid`=?;";

        try (Connection connection = databaseManager.database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, rank.getUuid().toString());

            ResultSet result = statement.executeQuery();
            if (result.next()) {
                rank.setLevel(result.getInt("level"));
                rank.setXp(result.getInt("xp"));
                rank.setModified(false);
            }
        } catch (SQLException ex) {
            this.plugin.getLogger().severe("Failed to load rank data!");
            ex.printStackTrace();
        }
    }

    public void loadRankAsync(Rank rank) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> loadRank(rank));
    }
}
