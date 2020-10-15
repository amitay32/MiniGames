package me.amitay.minigames.commands;

import me.amitay.minigames.MiniGames;
import me.amitay.minigames.utils.GameMode;
import me.amitay.minigames.utils.Utils;
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
            sender.sendMessage(ChatColor.RED + "This plugin is for players only!");
            return true;
        }
        Player p = (Player) sender;
        if (args.length == 0) {
            p.sendMessage(Utils.getFormattedText("&cCorrect usage: /hostgame [sumo] / [redrover]"));
            return true;
        }
        if (args.length == 1) {
            if (!p.hasPermission("minigames.host." + args[0].toLowerCase())) {
                p.sendMessage(Utils.getFormattedText("&cYou don't have the permissions to host this event"));
                return true;
            }
            if (pl.gamesManager.getAvailable().contains(args[0])) {
                pl.gamesManager.startGame(GameMode.valueOf(args[0].toUpperCase()), p);
            } else {
                p.sendMessage(Utils.getFormattedText("&cThis game is not available, or you didn't set it up yet."));
            }
        }
        return true;
    }
}

