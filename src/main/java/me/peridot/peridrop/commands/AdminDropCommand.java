package me.peridot.peridrop.commands;

import api.peridot.periapi.langapi.LangAPI;
import api.peridot.periapi.langapi.Replacement;
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
        LangAPI langAPI = dataManager.getLangApi();
        if (!sender.hasPermission("peridrop.cmd.admindrop")) {
            langAPI.sendMessage(sender, "errors.noperm", new Replacement("{PERMISSION}", "peridrop.cmd.admindrop"));
            return true;
        }
        if (args.length < 1) {
            langAPI.sendMessage(sender, "admindrop.help", new Replacement("{LABEL}", label));
            return true;
        }
        if (args[0].equalsIgnoreCase("help")) {
            langAPI.sendMessage(sender, "admindrop.help", new Replacement("{LABEL}", label));
        } else if (args[0].equalsIgnoreCase("reload") || args[0].equalsIgnoreCase("rl")) {
            if (sender.hasPermission("peridrop.cmd.admindrop.reload")) {
                langAPI.sendMessage(sender, "errors.noperm", new Replacement("{PERMISSION}", "peridrop.cmd.admindrop.reload"));
                return true;
            }
            try {
                plugin.reloadConfig();
                dataManager.reloadConfigurations();
            } catch (InvalidConfigurationException ex) {
                ex.printStackTrace();
                plugin.getServer().getPluginManager().disablePlugin(plugin);
            }
            langAPI.sendMessage(sender, "admindrop.configuration_reloaded");
        } else if (args[0].equalsIgnoreCase("rank-update") || args[0].equalsIgnoreCase("ranking-update")) {
            if (!sender.hasPermission("peridrop.cmd.admindrop.ranking-update")) {
                langAPI.sendMessage(sender, "errors.noperm", new Replacement("{PERMISSION}", "peridrop.cmd.admindrop.ranking-update"));
                return true;
            }
            plugin.getDatabaseManager().getRankDatabase().loadRanksAsync();
            langAPI.sendMessage(sender, "admindrop.ranking_update");
        } else {
            langAPI.sendMessage(sender, "admindrop.help", new Replacement("{LABEL}", label));
        }
        return true;
    }

    public void registerCommand() {
        PluginCommand command = plugin.getCommand("admindrop");
        command.setExecutor(this);
        command.setTabCompleter(this);
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
}
