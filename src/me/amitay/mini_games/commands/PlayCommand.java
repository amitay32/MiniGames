package me.amitay.mini_games.commands;

import me.amitay.mini_games.MiniGames;
import me.amitay.mini_games.manager.redrover.Redrover;
import me.amitay.mini_games.manager.spleef.Spleef;
import me.amitay.mini_games.manager.sumo.Sumo;
import me.amitay.mini_games.utils.Gamemode;
import me.amitay.mini_games.utils.Utils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class PlayCommand implements CommandExecutor {
    private MiniGames pl;
    private List<String> available = new ArrayList<>();
    private List<String> unavailable = new ArrayList<>();
    public PlayCommand(MiniGames pl) {
        this.pl = pl;
        for (Gamemode gamemode : Gamemode.values()) {
            if (pl.gamesManager.getGamemodes().get(gamemode) != null){
                available.add(gamemode.name().toLowerCase());
                continue;
            }
            unavailable.add(gamemode.name());
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "This plugin is for Players only!");
            return true;
        }
        Player p = (Player) sender;
        if (args.length == 0){
            sendMessages(p);
            return true;
        }
        if (args.length == 1){
            if (!available.contains(args[0])){
                p.sendMessage(Utils.getFormattedText("&cThis game does not exist or was not added by the server owner"));
                return true;
            }
            if (!Utils.inventoryEmpty(p)){
                p.sendMessage(Utils.getFormattedText("&eYou must have an empty inventory to join a minigame event"));
                return true;
            }
            if (args[0].toLowerCase().equalsIgnoreCase("sumo")){
                Sumo sumo = pl.gamesManager.getSumoGame();
                if (!sumo.getStatus()){
                    p.sendMessage(Utils.getFormattedText("&eThis minigame has never started or it is already running"));
                    return true;
                }
                if (sumo.enoughSpace()) {
                    sumo.addToList(p);
                    p.sendMessage(Utils.getFormattedText("&eYou were successfully added to the sumo game, good luck!"));
                    return true;
                }
                p.sendMessage(Utils.getFormattedText("&eThis game is already full."));
                return true;
            }
            if (args[0].toLowerCase().equalsIgnoreCase("redrover")){
                Redrover redrover = pl.gamesManager.getRedroverGame();
                if (!redrover.getStatus()){
                    p.sendMessage(Utils.getFormattedText("&eThis minigame has never started or it is already running"));
                    return true;
                }
                if (redrover.enoughSpace()) {
                    redrover.addToList(p);
                    p.sendMessage(Utils.getFormattedText("&eYou were successfully added to the redrover game, good luck!"));
                    return true;
                }
                p.sendMessage(Utils.getFormattedText("&eThis game is already full."));
                return true;
            }
            if (args[0].toLowerCase().equalsIgnoreCase("spleef")){
                Spleef spleef = pl.gamesManager.getSpleefGame();
                if (!spleef.getStatus()){
                    p.sendMessage(Utils.getFormattedText("&eThis minigame has never started or it is already running"));
                    return true;
                }
                if (spleef.enoughSpace()) {
                    spleef.addToList(p);
                    p.sendMessage(Utils.getFormattedText("&eYou were successfully added to the spleef game, good luck!"));
                    return true;
                }
                p.sendMessage(Utils.getFormattedText("&eThis game is already full."));
                return true;
            }
        }
        return true;
    }
    private void sendMessages(Player p){
        p.sendMessage(Utils.getFormattedText("&eAvailable games: &a" + available));
        p.sendMessage(Utils.getFormattedText("&eUnavailable games: &c" + unavailable));
    }
}
