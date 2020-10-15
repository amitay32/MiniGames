package me.amitay.minigames.listeners;

import me.amitay.minigames.MiniGames;
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
            if (pl.gamesManager.getRedroverGame().inGame(p)) {
                pl.gamesManager.getRedroverGame().removePlayerFromGame(p);
                return;
            }
            if (pl.getRedroverPlayerData().getKiller().equals(p)) {
                pl.gamesManager.getRedroverGame().killerDisconnect(p);
                return;
            }
            pl.gamesManager.playerQuitMiniGame(pl.gamesManager.getCurrentlyPlayedGamemode(p), p);
        }
    }
}

