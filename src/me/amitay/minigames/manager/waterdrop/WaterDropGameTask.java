package me.amitay.minigames.manager.waterdrop;

import me.amitay.minigames.MiniGames;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Map;

public class WaterDropGameTask extends BukkitRunnable {
    private MiniGames pl;
    private WaterDrop waterDrop;
    private Map<Player, Integer> players;
    private int length, sec, lastMatch;
    private boolean shouldEnd = false;
    private Player winner;

    public WaterDropGameTask(WaterDrop waterDrop, MiniGames pl) {
        this.pl = pl;
        this.waterDrop = waterDrop;
        players = pl.getWaterdropPlayerData().getPlayerMap();
    }

    @Override
    public void run() {
        players = pl.getWaterdropPlayerData().getPlayerMap();
        players.replaceAll((p, v) -> {
            if (!p.isDead())
                if (p.getLocation().getY() <= waterDrop.getYLevel()) {
                    if (waterDrop.getWaterDropArena().size() > v) {
                        p.teleport(waterDrop.getWaterDropArena().get(v));
                        return v + 1;
                    } else {
                        winner = p;
                        shouldEnd = true;
                    }
                }
            return v;
        });
        if (shouldEnd) {
            waterDrop.endGame(winner);
            cancel();
        }
    }
}
