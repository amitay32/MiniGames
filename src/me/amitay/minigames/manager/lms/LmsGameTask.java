package me.amitay.minigames.manager.lms;

import me.amitay.minigames.MiniGames;
import me.amitay.minigames.utils.Utils;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class LmsGameTask extends BukkitRunnable {
    private MiniGames pl;
    private Lms lms;
    private List<Player> players;
    private int sec;

    public LmsGameTask(Lms lms, MiniGames pl) {
        this.lms = lms;
        this.pl = pl;
        players = pl.getLmsPlayerData().getAlive();
        sec = lms.maxPlayTime * 60;
    }

    @Override
    public void run() {
        sec--;
        players = pl.getLmsPlayerData().getAlive();
        if (players.size() == 1) {
            finishGame();
        }
        if (sec == 0) {
            finishGameAll();
        }
        if (sec % 300 == 0 || (sec < 100 && sec % 60 == 0) || sec <= 10)
            players.forEach(player -> {
                if (sec > 61)
                    player.sendMessage(Utils.getFormattedText("&eThe lms game will be over in " + sec / 60 + " minutes if there is not only 1 man standing by then."));
                else if (sec > 11){
                    player.sendMessage(Utils.getFormattedText("&eThe lms game will be over in " + sec / 60 + " minute if there is not only 1 man standing by then."));
                } else {
                    player.sendMessage(Utils.getFormattedText("&eThe lms game will be over in " + sec + " seconds if there is not only 1 man standing by then."));
                }
            });
    }

    private void finishGame() {
        players.get(0).sendMessage(Utils.getFormattedText("&aYou've won the lms event! well done."));
        lms.endGame(players.get(0));
        lms.getjoinedPlayers().clear();
        cancel();
    }

    private void finishGameAll() {
        players.forEach(player -> {
            player.sendMessage(Utils.getFormattedText("&eThe lms game is now over, no one won because the time was out yet there isn't only 1 man standing."));
        });
        lms.endGameNoReward();
        cancel();
    }
}
