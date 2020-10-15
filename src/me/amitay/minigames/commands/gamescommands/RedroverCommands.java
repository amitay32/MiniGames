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

public class RedroverCommands implements CommandExecutor {
    private MiniGames pl;

    public RedroverCommands(MiniGames pl) {
        this.pl = pl;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "This plugin is for players only!");
            return true;
        }
        Player p = (Player) sender;
        if (!p.hasPermission("minigames.redrover")) {
            p.sendMessage(Utils.getFormattedText("&cYou don't have the permission to use this command. Use &e/play redrover &cif you want to join a game."));
            return true;
        }
        if (args.length == 0) {
            p.sendMessage(Utils.getFormattedText("&aredrover help menu"));
            return true;
        }
        if (args.length == 1) {
            p.sendMessage(Utils.getFormattedText("&cCorrect usage: /redrover set [area1] / [area2] / [runnersspawn] / [attackerspawn] / [attackerskit] / [runnerkit] / [spectators] / [minplayers] / [maxplayers] / [timetostart]"));
            return true;
        }
        if (args.length == 2){
            if (args[0].equalsIgnoreCase("set")){
                if (args[1].equalsIgnoreCase("runnerspawn")){
                    pl.getConfig().set("minigames.games.redrover.runners-spawn", p.getLocation());
                    pl.saveConfig();
                    p.sendMessage(Utils.getFormattedText("&eRunners spawn position was set successfully!"));
                    return true;
                }
                if (args[1].equalsIgnoreCase("attackerspawn")){
                    pl.getConfig().set("minigames.games.redrover.attacker-spawn", p.getLocation());
                    pl.saveConfig();
                    p.sendMessage(Utils.getFormattedText("&eAttacker spawn position was set successfully!"));
                    return true;
                }
                if (args[1].equalsIgnoreCase("spectators")){
                    pl.getConfig().set("minigames.games.redrover.spectators-spawn", p.getLocation());
                    pl.saveConfig();
                    p.sendMessage(Utils.getFormattedText("&eSpectator Spawn position was set successfully!"));
                    return true;
                }
                if (args[1].equalsIgnoreCase("attackerskit")){
                    List<ItemStack> list = new ArrayList<>();
                    for (ItemStack item : p.getInventory().getContents()) {
                        if (item != null && !item.equals(new ItemStack(Material.AIR))){
                            list.add(item);
                        }
                    }
                    pl.getConfig().set("minigames.games.redrover.attackers-kit", list);
                    pl.saveConfig();
                    p.sendMessage(Utils.getFormattedText("&eThe attacker kit was successfully set to the items in your inventory! &c(This feature is optional you can delete it from the config if you wish so)"));
                    return true;
                }
                if (args[1].equalsIgnoreCase("runnerkit")){
                    List<ItemStack> list = new ArrayList<>();
                    for (ItemStack item : p.getInventory().getContents()) {
                        if (item != null && !item.equals(new ItemStack(Material.AIR))){
                            list.add(item);
                        }
                    }
                    pl.getConfig().set("minigames.games.redrover.runners-kit", list);
                    pl.saveConfig();
                    p.sendMessage(Utils.getFormattedText("&eThe runners kit was successfully set to the items in your inventory! &c(This feature is optional you can delete it from the config if you wish so)"));
                    return true;
                }
                p.sendMessage(Utils.getFormattedText("&cCorrect usage: /redrover set [area1] / [area2] / [runnersspawn] / [attackerspawn] / [attackerskit] / [runnerkit] / [spectators] / [minplayers] / [maxplayers] / [timetostart] / [timetorun]"));
                return true;
            }
            p.sendMessage(Utils.getFormattedText("&aRedrover help menu"));
            return true;
        }
        if (args.length == 3){
            if (args[0].equalsIgnoreCase("set")){
                if (args[1].equalsIgnoreCase("minplayers")){
                    if (!Utils.isInteger(args[2])){
                        p.sendMessage(Utils.getFormattedText("&cThe minimum players value must be an integer."));
                        return true;
                    }
                    pl.getConfig().set("minigames.games.redrover.min-players", Integer.parseInt(args[2]));
                    pl.saveConfig();
                    p.sendMessage(Utils.getFormattedText("&eThe minimum players for a redrover game to start was set to " + args[2]));
                    return true;
                }
                if (args[1].equalsIgnoreCase("maxplayers")){
                    if (!Utils.isInteger(args[2])){
                        p.sendMessage(Utils.getFormattedText("&cThe maximum players value must be an integer."));
                        return true;
                    }
                    pl.getConfig().set("minigames.games.redrover.max-players", Integer.parseInt(args[2]));
                    pl.saveConfig();
                    p.sendMessage(Utils.getFormattedText("&eThe maximum players for a redrover game to start was set to " + args[2]));
                    return true;
                }
                if (args[1].equalsIgnoreCase("timetostart")){
                    if (!Utils.isInteger(args[2])){
                        p.sendMessage(Utils.getFormattedText("&cThe time to start value must be an integer."));
                        return true;
                    }
                    pl.getConfig().set("minigames.games.redrover.time-to-start", Integer.parseInt(args[2]));
                    pl.saveConfig();
                    p.sendMessage(Utils.getFormattedText("&eThe time before a redrover game starts was set to " + args[2]));
                    return true;
                }
                if (args[1].equalsIgnoreCase("area1")){
                    pl.getConfig().set("minigames.games.redrover.area1", args[2]);
                    pl.saveConfig();
                    p.sendMessage(Utils.getFormattedText("&eArea 1 was set successfully!"));
                    return true;
                }
                if (args[1].equalsIgnoreCase("area2")){
                    pl.getConfig().set("minigames.games.redrover.area2", args[2]);
                    pl.saveConfig();
                    p.sendMessage(Utils.getFormattedText("&eArea 2 was set successfully!"));
                    return true;
                }
                p.sendMessage(Utils.getFormattedText("&cCorrect usage: /redrover set [area1] / [area2] / [areaattacker] / [attackerskit] / [runnerkit] / [spectators] / [minplayers] / [maxplayers] / [timetostart] / [timetorun]"));
                return true;
            }
            p.sendMessage(Utils.getFormattedText("&aRedrover help menu"));
            return true;
        }
        p.sendMessage(Utils.getFormattedText("&aRedrover help menu"));
        return true;
    }
}
