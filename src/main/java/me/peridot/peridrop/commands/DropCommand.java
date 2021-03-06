package me.peridot.peridrop.commands;

import api.peridot.periapi.configuration.langapi.LangAPI;
import api.peridot.periapi.utils.replacements.Replacement;
import me.peridot.peridrop.PeriDrop;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;

public class DropCommand implements CommandExecutor {

    private final PeriDrop plugin;

    public DropCommand(PeriDrop plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        LangAPI lang = this.plugin.getLang();
        if (!(sender instanceof Player)) {
            lang.sendSimpleMessage(sender, "errors.noplayer");
            return true;
        }
        Player player = (Player) sender;

        if (!player.hasPermission("peridrop.cmd.drop")) {
            lang.sendMessage(sender, "errors.noperm", new Replacement("{PERMISSION}", "peridrop.cmd.drop"));
            return true;
        }
        this.plugin.getInventoryManager().getMenuInventory().open(player);
        return true;
    }

    public void registerCommand() {
        PluginCommand command = this.plugin.getCommand("drop");
        command.setExecutor(this);
    }

}
