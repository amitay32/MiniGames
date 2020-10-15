package me.amitay.minigames.commands.gamescommands;

import me.amitay.minigames.MiniGames;
import me.amitay.minigames.utils.Utils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class LmsCommands  implements CommandExecutor {
    private MiniGames pl;

    public LmsCommands(MiniGames pl) {
        this.pl = pl;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "This plugin is for players only!");
            return true;
        }
        Player p = (Player) sender;
        if (!p.hasPermission("minigames.lms")) {
            p.sendMessage(Utils.getFormattedText("&cYou don't have the permission to use this command. Use &e/play lms &cif you want to join a game."));
            return true;
        }
        if (args.length == 0) {
            p.sendMessage(Utils.getFormattedText("&aLms help menu"));
            return true;
        }
        if (args.length == 1) {
            p.sendMessage(Utils.getFormattedText("&cCorrect usage: /lms set [spawn] / [kit] / [spectators] / [minplayers] / [maxplayers] / [timetostart]"));
            return true;
        }
        if (args.length == 2) {
            if (args[0].equalsIgnoreCase("set")) {
                if (args[1].equalsIgnoreCase("spawn")) {
                    pl.getConfig().set("minigames.games.lms.spawn", p.getLocation());
                    pl.saveConfig();
                    p.sendMessage(Utils.getFormattedText("&eSpawn position was set successfully!"));
                    return true;
                }
                if (args[1].equalsIgnoreCase("spectators")) {
                    pl.getConfig().set("minigames.games.lms.spectators-spawn", p.getLocation());
                    pl.saveConfig();
                    p.sendMessage(Utils.getFormattedText("&eSpectator Spawn position was set successfully!"));
                    return true;
                }
                if (args[1].equalsIgnoreCase("kit")){
                    List<ItemStack> list = new ArrayList<>();
                    for (ItemStack item : p.getInventory().getContents()) {
                        if (item != null && !item.equals(new ItemStack(Material.AIR))){
                            list.add(item);
                        }
                    }
                    pl.getConfig().set("minigames.games.lms.kit", list);
                    pl.saveConfig();
                    p.sendMessage(Utils.getFormattedText("&eThe runners kit was successfully set to the items in your inventory! &c(This feature is optional you can delete it from the config if you wish so)"));
                    return true;
                }
                p.sendMessage(Utils.getFormattedText("&cCorrect usage: /lms set [spawn] / [kit] / [spectators] / [minplayers] / [maxplayers] / [timetostart]"));
                return true;
            }
            p.sendMessage(Utils.getFormattedText("&aLms help menu"));
            return true;
        }
        if (args.length == 3) {
            if (args[0].equalsIgnoreCase("set")) {
                if (args[1].equalsIgnoreCase("minplayers")) {
                    if (!Utils.isInteger(args[2])) {
                        p.sendMessage(Utils.getFormattedText("&cThe minimum players value must be an integer."));
                        return true;
                    }
                    pl.getConfig().set("minigames.games.lms.min-players", Integer.parseInt(args[2]));
                    pl.saveConfig();
                    p.sendMessage(Utils.getFormattedText("&eThe minimum players for a lms game to start was set to " + args[2]));
                    return true;
                }
                if (args[1].equalsIgnoreCase("maxplayers")) {
                    if (!Utils.isInteger(args[2])) {
                        p.sendMessage(Utils.getFormattedText("&cThe maximum players value must be an integer."));
                        return true;
                    }
                    pl.getConfig().set("minigames.games.lms.max-players", Integer.parseInt(args[2]));
                    pl.saveConfig();
                    p.sendMessage(Utils.getFormattedText("&eThe maximum players for a lms game to start was set to " + args[2]));
                    return true;
                }
                if (args[1].equalsIgnoreCase("timetostart")) {
                    if (!Utils.isInteger(args[2])) {
                        p.sendMessage(Utils.getFormattedText("&cThe time to start value must be an integer."));
                        return true;
                    }
                    pl.getConfig().set("minigames.games.lms.time-to-start", Integer.parseInt(args[2]));
                    pl.saveConfig();
                    p.sendMessage(Utils.getFormattedText("&eThe time before a lms game starts was set to " + args[2]));
                    return true;
                }
                p.sendMessage(Utils.getFormattedText("&cCorrect usage: /lms set [spawn] / [kit] / [spectators] / [minplayers] / [maxplayers] / [timetostart]"));
                return true;
            }
            p.sendMessage(Utils.getFormattedText("&alms help menu"));
            return true;
        }
        p.sendMessage(Utils.getFormattedText("&alms help menu"));
        return true;
    }
}
