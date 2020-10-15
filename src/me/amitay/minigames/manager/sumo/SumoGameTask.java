package me.amitay.minigames.manager.sumo;

import me.amitay.minigames.MiniGames;
import me.amitay.minigames.utils.Utils;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class SumoGameTask extends BukkitRunnable {
    private MiniGames pl;
    private Sumo sumo;
    private List<Player> list;
    private List<Player> players;

    public SumoGameTask(Sumo sumo, MiniGames pl, List<Player> list) {
        this.pl = pl;
        this.sumo = sumo;
        this.list = list;
    }

    @Override
    public void run() {
        players = pl.getSumoPlayerData().getPlayerAvailableForFight();
        int countDown = list.size();
        if (countDown == 1) {
            cancel();
            sumo.endGame(list.get(0));
            list.get(0).sendMessage(Utils.getFormattedText("&aYou've won the sumo event! well done."));
            list.remove(list.get(0));
            sumo.getJoinedPlayers().clear();
        }
        if (!sumo.fighting && players.size() > 1) {
            sumo.currentP1 = players.remove(sumo.getRand().nextInt(players.size()));
            sumo.currentP2 = players.remove(sumo.getRand().nextInt(players.size()));
            sumo.currentP1.teleport(sumo.p1);
            sumo.currentP2.teleport(sumo.p2);
            pl.getSumoPlayerData().getCurrentlyFighting().add(sumo.currentP1);
            pl.getSumoPlayerData().getCurrentlyFighting().add(sumo.currentP2);
            sumo.freezePlayerSumo(sumo.currentP1);
            sumo.freezePlayerSumo(sumo.currentP2);
            sumo.fighting = true;
        }
        if (sumo.fighting && sumo.currentP1.getLocation().getY() < sumo.p1.getY()) {
            sumo.playerLose(sumo.currentP1, list);
            sumo.playerWin(sumo.currentP2, list);
            sumo.currentP1 = null;
            sumo.currentP2 = null;
            sumo.fighting = false;
        } else if (sumo.fighting && sumo.currentP2.getLocation().getY() < sumo.p1.getY()) {
            sumo.playerLose(sumo.currentP2, list);
            sumo.playerWin(sumo.currentP1, list);
            sumo.currentP1 = null;
            sumo.currentP2 = null;
            sumo.fighting = false;
        }
        if (!sumo.fighting && countDown != 1 && players.size() < 2) {
            if (players.size() == 1) {
                players.get(0).sendMessage(Utils.getFormattedText("&eWe did not find anyone to set you up with, therefor you were automatically advanced to the next round!"));
            }
            players.addAll(pl.getSumoPlayerData().getUnavailableForFight());
        }
    }

}

