package me.peridot.peridrop.commands;

import api.peridot.periapi.configuration.langapi.LangAPI;
import api.peridot.periapi.utils.replacements.Replacement;
import me.peridot.peridrop.PeriDrop;
import me.peridot.peridrop.data.configuration.ConfigurationManager;
import org.bukkit.command.*;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AdminDropCommand implements CommandExecutor, TabCompleter {

    private final PeriDrop plugin;

    public AdminDropCommand(PeriDrop plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        ConfigurationManager dataManager = plugin.getConfigurationManager();
        LangAPI lang = dataManager.getLang();
        if (!sender.hasPermission("peridrop.cmd.admindrop")) {
            lang.sendMessage(sender, "errors.noperm", new Replacement("{PERMISSION}", "peridrop.cmd.admindrop"));
            return true;
        }
        if (args.length < 1) {
            lang.sendMessage(sender, "admindrop.help", new Replacement("{LABEL}", label));
            return true;
        }
        if (args[0].equalsIgnoreCase("help")) {
            lang.sendMessage(sender, "admindrop.help", new Replacement("{LABEL}", label));
        } else if (args[0].equalsIgnoreCase("reload") || args[0].equalsIgnoreCase("rl")) {
            if (!sender.hasPermission("peridrop.cmd.admindrop.reload")) {
                lang.sendMessage(sender, "errors.noperm", new Replacement("{PERMISSION}", "peridrop.cmd.admindrop.reload"));
                return true;
            }
            try {
                plugin.reloadConfig();
                dataManager.reloadConfigurations();
                plugin.initInventoryManager();
            } catch (InvalidConfigurationException ex) {
                ex.printStackTrace();
                plugin.getServer().getPluginManager().disablePlugin(plugin);
            }
            lang.sendMessage(sender, "admindrop.configuration_reloaded");
        } else if (args[0].equalsIgnoreCase("rank-update") || args[0].equalsIgnoreCase("ranking-update")) {
            if (!sender.hasPermission("peridrop.cmd.admindrop.ranking-update")) {
                lang.sendMessage(sender, "errors.noperm", new Replacement("{PERMISSION}", "peridrop.cmd.admindrop.ranking-update"));
                return true;
            }
            plugin.getDatabaseManager().getRankDatabase().loadRanksAsync();
            lang.sendMessage(sender, "admindrop.ranking_update");
        } else {
            lang.sendMessage(sender, "admindrop.help", new Replacement("{LABEL}", label));
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        String[] arguments = {"help", "reload", "ranking-update"};

        final List<String> completions = new ArrayList<>();

        StringUtil.copyPartialMatches(args[0], Arrays.asList(arguments), completions);

        if (!sender.hasPermission("peridrop.cmd.admindrop.reload")) {
            completions.remove("reload");
        }

        if (!sender.hasPermission("peridrop.cmd.admindrop.ranking-update")) {
            completions.remove("ranking-update");
        }

        return completions;
    }

    public void registerCommand() {
        PluginCommand command = plugin.getCommand("admindrop");
        command.setExecutor(this);
        command.setTabCompleter(this);
    }

}
