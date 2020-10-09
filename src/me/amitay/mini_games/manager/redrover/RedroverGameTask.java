package me.amitay.mini_games.manager.redrover;

import me.amitay.mini_games.MiniGames;
import me.amitay.mini_games.utils.Utils;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class RedroverGameTask extends BukkitRunnable {
    private MiniGames pl;
    private Redrover redrover;
    private int newCountDown, count = 0;
    private List<Player> players;

    public RedroverGameTask(Redrover redrover, MiniGames pl) {
        this.redrover = redrover;
        this.pl = pl;
        players = pl.getRedroverPlayerData().getAlive();
        newCountDown = this.redrover.timeToRun;
    }

    @Override
    public void run() {
        players = pl.getRedroverPlayerData().getAlive();
        if (players.size() == 1) {
            finishGame();
            return;
        }
        if (newCountDown == redrover.timeToRun)
            players.forEach(p -> {
                p.sendMessage(Utils.getFormattedText("&eYou have " + newCountDown + " seconds to get the the other side!"));
            });
        newCountDown--;
        if (newCountDown == 0) {
            players.forEach(p -> {
                if (count % 2 == 1)
                    if (!Utils.isInRegion(p, redrover.area1)) {
                        if (players.size() == 1) {
                            finishGame();
                            return;
                        }
                        playerLoss(p);
                    }
                if (count % 2 == 0)
                    if (!Utils.isInRegion(p, redrover.area2)) {
                        if (players.size() == 1) {
                            finishGame();
                            return;
                        }
                        playerLoss(p);
                    }
            });
            newCountDown = redrover.timeToRun;
            count++;
        }
        if (newCountDown > 0 && newCountDown < 6)
            players.forEach(p -> {
                p.sendMessage(Utils.getFormattedText("&eYou have " + newCountDown + " seconds to get the the other side!"));
            });
    }
    private void finishGame() {
        players.get(0).sendMessage(Utils.getFormattedText("&aYou've won the redrover event! well done."));
        redrover.endGame(players.get(0));
        redrover.joinedPlayersRedrover.clear();
        cancel();
    }
    private void playerLoss(Player p){
        p.getInventory().clear();
        players.remove(p);
        pl.getRedroverPlayerData().getSpectators().add(p);
        p.teleport(redrover.spectators);
        p.sendMessage(Utils.getFormattedText("&eYou've lost the redrover event, you can now spectate the rest of the game here or return to the hub."));
    }
}


