package me.peridot.peridrop.user.rank;

import me.peridot.peridrop.data.configuration.PluginConfiguration;
import me.peridot.peridrop.modifiable.Modifiable;
import me.peridot.peridrop.user.User;

import java.util.UUID;

public class Rank extends Modifiable implements Comparable<Rank> {

    private final UUID uuid;
    private final String identifierName;

    private int position;

    private int level;
    private int xp;

    public Rank(UUID uuid, String identifierName) {
        this.uuid = uuid;
        this.identifierName = identifierName;
        this.level = PluginConfiguration.ranking_default_level;
    }

    public Rank(User user) {
        this(user.getUuid(), user.getName());
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getIdentifierName() {
        return identifierName;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getXp() {
        return xp;
    }

    public void setXp(int xp) {
        this.xp = xp;
        setModified(true);
    }

    public void changeXp(int change) {
        this.xp = Math.max(0, this.xp + change);
        setModified(true);
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
        setModified(true);
    }

    public void changeLevel(int change) {
        this.level = Math.max(0, this.level + change);
        setModified(true);
    }

    public int compareTo(Rank rank) {
        int lc = Integer.compare(this.getLevel(), rank.getLevel());
        if (lc == 0) {
            int xc = Integer.compare(this.getXp(), rank.getXp());
            if (xc == 0) {
                if (this.identifierName == null) {
                    return -1;
                }
                if (rank.identifierName == null) {
                    return 1;
                }
                return this.identifierName.compareTo(rank.identifierName);
            }
            return xc;
        }
        return lc;
    }
}
