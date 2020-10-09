package me.amitay.mini_games.commands.games_commands;

import me.amitay.mini_games.MiniGames;
import me.amitay.mini_games.utils.Utils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SumoCommands implements CommandExecutor {
    private MiniGames pl;

    public SumoCommands(MiniGames pl) {
        this.pl = pl;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "This plugin is for Players only!");
            return true;
        }
        Player p = (Player) sender;
        if (!p.hasPermission("minigames.sumo")) {
            p.sendMessage(Utils.getFormattedText("&cYou don't have the permission to use this command. Use &e/play sumo &cif you want to join a game."));
            return true;
        }
        if (args.length == 0) {
            p.sendMessage(Utils.getFormattedText("&a Sumo help menu"));
            return true;
        }
        if (args.length == 1) {
            p.sendMessage(Utils.getFormattedText("&cCorrect usage: /sumo set [spawnplayer1] / [spawnplayer2] / [lobby] / [spectators] / [minplayers] / [maxplayers] / [timetostart]"));
            return true;
        }
        if (args.length == 2){
            if (args[0].equalsIgnoreCase("set")){
                if (args[1].equalsIgnoreCase("spawnplayer1")){
                    pl.getConfig().set("minigames.games.sumo.player-location-1", p.getLocation());
                    pl.saveConfig();
                    p.sendMessage(Utils.getFormattedText("&eSpawn position 1 was set successfully!"));
                    return true;
                }
                if (args[1].equalsIgnoreCase("spawnplayer2")){
                    pl.getConfig().set("minigames.games.sumo.player-location-2", p.getLocation());
                    pl.saveConfig();
                    p.sendMessage(Utils.getFormattedText("&eSpawn position 2 was set successfully!"));
                    return true;
                }
                if (args[1].equalsIgnoreCase("lobby")){
                    pl.getConfig().set("minigames.games.sumo.lobby-spawn", p.getLocation());
                    pl.saveConfig();
                    p.sendMessage(Utils.getFormattedText("&eLobby position was set successfully!"));
                    return true;
                }
                if (args[1].equalsIgnoreCase("spectators")){
                    pl.getConfig().set("minigames.games.sumo.spectators-spawn", p.getLocation());
                    pl.saveConfig();
                    p.sendMessage(Utils.getFormattedText("&eSpectator Spawn position was set successfully!"));
                    return true;
                }
                p.sendMessage(Utils.getFormattedText("&cCorrect usage: /sumo set [spawnplayer1] / [spawnplayer2] / [lobby] / [spectators] / [minplayers] / [maxplayers] / [timetostart]"));
                return true;
            }
            p.sendMessage(Utils.getFormattedText("&a Sumo help menu"));
            return true;
        }
        if (args.length == 3){
            if (args[0].equalsIgnoreCase("set")){
                if (args[1].equalsIgnoreCase("minplayers")){
                    if (!Utils.isInteger(args[2])){
                        p.sendMessage(Utils.getFormattedText("&cThe minimum players value must be an integer."));
                        return true;
                    }
                    pl.getConfig().set("minigames.games.sumo.min-players", Integer.parseInt(args[2]));
                    pl.saveConfig();
                    p.sendMessage(Utils.getFormattedText("&eThe minimum players for a sumo game to start was set to " + args[2]));
                    return true;
                }
                if (args[1].equalsIgnoreCase("maxplayers")){
                    if (!Utils.isInteger(args[2])){
                        p.sendMessage(Utils.getFormattedText("&cThe maximum players value must be an integer."));
                        return true;
                    }
                    pl.getConfig().set("minigames.games.sumo.max-players", Integer.parseInt(args[2]));
                    pl.saveConfig();
                    p.sendMessage(Utils.getFormattedText("&eThe maximum players for a sumo game to start was set to " + args[2]));
                    return true;
                }
                if (args[1].equalsIgnoreCase("timetostart")){
                    if (!Utils.isInteger(args[2])){
                        p.sendMessage(Utils.getFormattedText("&cThe time to start value must be an integer."));
                        return true;
                    }
                    pl.getConfig().set("minigames.games.sumo.time-to-start", Integer.parseInt(args[2]));
                    pl.saveConfig();
                    p.sendMessage(Utils.getFormattedText("&eThe time before a sumo game starts was set to " + args[2]));
                    return true;
                }
                p.sendMessage(Utils.getFormattedText("&cCorrect usage: /sumo set [spawnplayer1] / [spawnplayer2] / [lobby] / [spectators] / [minplayers] / [maxplayers] / [timetostart]"));
                return true;
            }
            p.sendMessage(Utils.getFormattedText("&a Sumo help menu"));
            return true;
        }
        p.sendMessage(Utils.getFormattedText("&a Sumo help menu"));
        return true;
    }
}
