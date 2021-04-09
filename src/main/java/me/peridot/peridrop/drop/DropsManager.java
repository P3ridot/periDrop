package me.peridot.peridrop.drop;

import api.peridot.periapi.configuration.ConfigurationFile;
import me.peridot.peridrop.PeriDrop;
import org.bukkit.configuration.InvalidConfigurationException;

import java.util.ArrayList;
import java.util.List;

public class DropsManager {

    private final PeriDrop plugin;

    private List<Drop> dropsList = new ArrayList<>();

    public DropsManager(PeriDrop plugin) {
        this.plugin = plugin;
    }

    public void loadDrops() throws InvalidConfigurationException {
        ConfigurationFile dropConfiguration = this.plugin.getDropsConfiguration();
        DropParser dropParser = new DropParser(this.plugin);

        this.dropsList = dropParser.parseDrops(dropConfiguration.getYamlConfiguration().getConfigurationSection("drops"));
    }

    public List<Drop> getDropsList() {
        return new ArrayList<>(this.dropsList);
    }

}
