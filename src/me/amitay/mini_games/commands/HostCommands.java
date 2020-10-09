package me.amitay.mini_games.commands;

import me.amitay.mini_games.MiniGames;
import me.amitay.mini_games.utils.Gamemode;
import me.amitay.mini_games.utils.Utils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HostCommands implements CommandExecutor {
    private MiniGames pl;

    public HostCommands(MiniGames pl) {
        this.pl = pl;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "This plugin is for Players only!");
            return true;
        }
        Player p = (Player) sender;
        if (args.length == 0) {
            p.sendMessage(Utils.getFormattedText("&cCorrect usage: /hostgame [sumo] / [redrover]"));
            return true;
        }
        if (args.length == 1){
            if (args[0].toLowerCase().equalsIgnoreCase("sumo")){
                if (!p.hasPermission("minigames.host.sumo")){
                    p.sendMessage(Utils.getFormattedText("&cYou don't have the permissions to host this event"));
                    return true;
                }
                if (pl.gamesManager.getGamemodes().containsKey(Gamemode.SUMO)) {
                    pl.gamesManager.startGame(Gamemode.SUMO);
                    p.sendMessage(Utils.getFormattedText("&eYou have successfully started a sumo game!"));
                    return true;
                }
            }
            if (args[0].toLowerCase().equalsIgnoreCase("redrover")){
                if (!p.hasPermission("minigames.host.redrover")){
                    p.sendMessage(Utils.getFormattedText("&cYou don't have the permissions to host this event"));
                    return true;
                }
                if (pl.gamesManager.getGamemodes().containsKey(Gamemode.REDROVER)) {
                    pl.gamesManager.startGame(Gamemode.REDROVER);
                    p.sendMessage(Utils.getFormattedText("&eYou have successfully started a redrover game!"));
                    return true;
                }
            }
            if (args[0].toLowerCase().equalsIgnoreCase("spleef")){
                if (!p.hasPermission("minigames.host.spleef")){
                    p.sendMessage(Utils.getFormattedText("&cYou don't have the permissions to host this event"));
                    return true;
                }
                if (pl.gamesManager.getGamemodes().containsKey(Gamemode.SPLEEF)) {
                    pl.gamesManager.startGame(Gamemode.SPLEEF);
                    p.sendMessage(Utils.getFormattedText("&eYou have successfully started a spleef game!"));
                    return true;
                }
            }
        }
        return false;
    }
}
