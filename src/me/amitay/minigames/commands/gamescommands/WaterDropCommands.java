package me.amitay.minigames.commands.gamescommands;

import me.amitay.minigames.MiniGames;
import me.amitay.minigames.utils.Utils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class WaterDropCommands implements CommandExecutor {

    private MiniGames pl;

    public WaterDropCommands(MiniGames pl) {
        this.pl = pl;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "This plugin is for players only!");
            return true;
        }
        Player p = (Player) sender;
        if (!p.hasPermission("minigames.waterdrop")) {
            p.sendMessage(Utils.getFormattedText("&cYou don't have the permission to use this command. Use &e/play waterdrop &cif you want to join a game."));
            return true;
        }
        if (args.length == 0) {
            p.sendMessage(Utils.getFormattedText("&awaterdrop help menu"));
            return true;
        }
        if (args.length == 1) {
            p.sendMessage(Utils.getFormattedText("&cCorrect usage: /waterdrop set [newarena] / [minplayers] / [maxplayers] / [timetostart]"));
            return true;
        }
        if (args.length == 2) {
            if (args[0].equalsIgnoreCase("set")) {
                if (args[1].equalsIgnoreCase("newarena")) {
                    pl.gamesManager.getWaterDropGame().getWaterDropArena().add(p.getLocation());
                    pl.getConfig().set("minigames.games.waterdrop.stages", pl.gamesManager.getWaterDropGame().getWaterDropArena());
                    pl.saveConfig();
                    p.sendMessage(Utils.getFormattedText("&eSuccessfully added a new arena"));
                    return true;
                }
                p.sendMessage(Utils.getFormattedText("&cCorrect usage: /waterdrop set [newarena] / [minplayers] / [maxplayers] / [timetostart]"));
                return true;
            }
            p.sendMessage(Utils.getFormattedText("&awaterdrop help menu"));
            return true;
        }
        if (args.length == 3) {
            if (args[0].equalsIgnoreCase("set")) {
                if (args[1].equalsIgnoreCase("minplayers")) {
                    if (!Utils.isInteger(args[2])) {
                        p.sendMessage(Utils.getFormattedText("&cThe minimum players value must be an integer."));
                        return true;
                    }
                    pl.getConfig().set("minigames.games.waterdrop.min-players", Integer.parseInt(args[2]));
                    pl.saveConfig();
                    p.sendMessage(Utils.getFormattedText("&eThe minimum players for a waterdrop game to start was set to " + args[2]));
                    return true;
                }
                if (args[1].equalsIgnoreCase("maxplayers")) {
                    if (!Utils.isInteger(args[2])) {
                        p.sendMessage(Utils.getFormattedText("&cThe maximum players value must be an integer."));
                        return true;
                    }
                    pl.getConfig().set("minigames.games.waterdrop.max-players", Integer.parseInt(args[2]));
                    pl.saveConfig();
                    p.sendMessage(Utils.getFormattedText("&eThe maximum players for a waterdrop game to start was set to " + args[2]));
                    return true;
                }
                if (args[1].equalsIgnoreCase("timetostart")) {
                    if (!Utils.isInteger(args[2])) {
                        p.sendMessage(Utils.getFormattedText("&cThe time to start value must be an integer."));
                        return true;
                    }
                    pl.getConfig().set("minigames.games.waterdrop.time-to-start", Integer.parseInt(args[2]));
                    pl.saveConfig();
                    p.sendMessage(Utils.getFormattedText("&eThe time before a waterdrop game starts was set to " + args[2]));
                    return true;
                }
                p.sendMessage(Utils.getFormattedText("&cCorrect usage: /waterdrop set [newarena] / [minplayers] / [maxplayers] / [timetostart]"));
                return true;
            }
            p.sendMessage(Utils.getFormattedText("&awaterdrop help menu"));
            return true;
        }
        p.sendMessage(Utils.getFormattedText("&awaterdrop help menu"));
        return true;
    }
}
