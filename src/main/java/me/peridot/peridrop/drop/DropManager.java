package me.peridot.peridrop.drop;

import me.peridot.peridrop.PeriDrop;
import me.peridot.peridrop.data.configuration.DropConfiguration;
import org.bukkit.configuration.InvalidConfigurationException;

import java.util.ArrayList;
import java.util.List;

public class DropManager {

    private final PeriDrop plugin;

    private List<Drop> dropsList = new ArrayList<>();

    public DropManager(PeriDrop plugin) {
        this.plugin = plugin;
    }

    public void loadDrops() throws InvalidConfigurationException {
        DropConfiguration dropConfiguration = plugin.getConfigurationManager().getDropConfiguration();
        DropParser dropParser = new DropParser(plugin);

        dropsList = dropParser.parseDrops(dropConfiguration.getYamlConfiguration().getConfigurationSection("drops"));
    }

    public List<Drop> getDropsList() {
        return new ArrayList<>(dropsList);
    }
}
