package me.amitay.minigames.commands;

import me.amitay.minigames.MiniGames;
import me.amitay.minigames.utils.GameMode;
import me.amitay.minigames.utils.Utils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PlayCommand implements CommandExecutor {
    private MiniGames pl;
    private boolean ingame;

    public PlayCommand(MiniGames pl) {
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
            sendMessages(p);
            return true;
        }
        if (args.length == 1) {
            if (!pl.gamesManager.getAvailable().contains(args[0])) {
                p.sendMessage(Utils.getFormattedText("&cThis game does not exist or was not added by the server owner"));
                return true;
            }
            if (!Utils.inventoryEmpty(p)) {
                p.sendMessage(Utils.getFormattedText("&cYou must have an empty inventory to join a minigame event"));
                return true;
            }
            if (pl.gamesManager.getGamemodes().containsKey(GameMode.valueOf(args[0].toUpperCase()))) {
                pl.gamesManager.getGamemodes().forEach((game, v) -> {
                    if (v.getJoinedPlayers().contains(p)) {
                        p.sendMessage(Utils.getFormattedText("&eYou are already in this game!"));
                        ingame = true;
                    }
                });
                if (!ingame)
                    pl.gamesManager.joinGame(GameMode.valueOf(args[0].toUpperCase()), p);
            }
        }
        return true;
    }

    private void sendMessages(Player p) {
        p.sendMessage(Utils.getFormattedText("&eAvailable games: &a" + pl.gamesManager.getAvailable()));
        p.sendMessage(Utils.getFormattedText("&eUnavailable games: &c" + pl.gamesManager.getUnavailable()));
    }
}
