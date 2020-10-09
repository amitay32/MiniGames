package me.amitay.mini_games.listeners;

import me.amitay.mini_games.MiniGames;
import me.amitay.mini_games.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Random;

public class QuitListener implements Listener {
    private MiniGames pl;
    private Random rand = new Random();

    public QuitListener(MiniGames pl) {
        this.pl = pl;
    }

    @EventHandler
    public void playerQuitEvent(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        if (pl.gamesManager.getCurrentlyPlaying().contains(e.getPlayer())) {
            p.getInventory().clear();
            Bukkit.dispatchCommand(Bukkit.getConsoleSender() ,"spawn " + p.getName());
            System.out.println("QuitListener.playerQuitEvent0");
            if (pl.gamesManager.getSumoGame().inGame(e.getPlayer())) {
                if (pl.gamesManager.getSumoGame().isFighting(e.getPlayer())) {
                    pl.gamesManager.getSumoGame().playerWin(pl.getSumoPlayerData().getCurrentlyFighting().get(0), pl.gamesManager.getSumoGame().getJoinedPlayers());
                    pl.gamesManager.getSumoGame().playerLose(e.getPlayer(), pl.gamesManager.getSumoGame().getJoinedPlayers());
                }
                pl.getSumoPlayerData().getSpectators().remove(p);
                pl.getSumoPlayerData().getUnavailableForFight().remove(p);
                pl.getSumoPlayerData().getPlayerAvailableForFight().remove(p);
                pl.gamesManager.getSumoGame().getJoinedPlayers().remove(p);
                pl.gamesManager.getCurrentlyPlaying().remove(p);
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "spawn " + e.getPlayer().getName());
                return;
            }
            if (pl.gamesManager.getRedroverGame().inGame(p)) {
                pl.getRedroverPlayerData().getAlive().remove(p);
            } else if (pl.getRedroverPlayerData().getKiller().equals(p)) {
                if (pl.getRedroverPlayerData().getAlive().size() > 1) {
                    Player player = pl.getRedroverPlayerData().getAlive().get(rand.nextInt(pl.getRedroverPlayerData().getAlive().size()));
                    pl.getRedroverPlayerData().setKiller(player);
                    player.sendMessage(Utils.getFormattedText("&eThe current killer has disconnected, you were randomly chosen to be the new one. (the killer get rewards at the end of the game)"));
                    player.teleport(pl.gamesManager.getRedroverGame().getAttackerSpawn());
                    pl.getRedroverPlayerData().getAlive().remove(player);
                    player.getInventory().clear();
                    pl.gamesManager.getRedroverGame().getKitAttacker().forEach(itemStack -> {
                        player.getInventory().addItem(itemStack);
                    });
                }
            }
            if (pl.gamesManager.getSpleefGame().inGame(p)){
                pl.getSpleefPlayerData().getAlive().remove(p);
            }
        }
    }
}

