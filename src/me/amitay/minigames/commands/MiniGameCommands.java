package me.amitay.minigames.commands;

import me.amitay.minigames.MiniGames;
import me.amitay.minigames.utils.GameMode;
import me.amitay.minigames.utils.Utils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MiniGameCommands implements CommandExecutor {
    private MiniGames pl;

    public MiniGameCommands(MiniGames pl) {
        this.pl = pl;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "This plugin is for players only!");
            return true;
        }
        Player p = (Player) sender;
        if (!p.hasPermission("minigames.admin")) {
            p.sendMessage(Utils.getFormattedText("&cYou don't have the permission to use this command. Use &e/play (game) &cif you want to join a game."));
            return true;
        }
        if (args.length == 0) {
            p.sendMessage(Utils.getFormattedText("&cCorrect usage /minigames setmainspawn"));
            return true;
        }
        if (args.length == 1) {
            if (!args[0].equalsIgnoreCase("setmainspawn")) {
                p.sendMessage(Utils.getFormattedText("&aMinigames admin help menu"));
                return true;
            }
            p.sendMessage(Utils.getFormattedText("&eMain spawn for this plugin was successfully set"));
            pl.getConfig().set("minigames.main_spawn_location", p.getLocation());
            pl.saveConfig();
        }
        if (args.length == 2) {
            if (args[0].equalsIgnoreCase("close")) {
                if (pl.gamesManager.getAvailable().contains(args[1])) {
                    pl.gamesManager.endGame(GameMode.valueOf(args[1].toUpperCase()), p);
                } else {
                    p.sendMessage(Utils.getFormattedText("&cThis game is not available, or you didn't set it up yet."));
                }

            }
        }
        return true;
    }
}
