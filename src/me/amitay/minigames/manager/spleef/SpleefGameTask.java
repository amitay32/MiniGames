package me.amitay.minigames.manager.spleef;

import me.amitay.minigames.MiniGames;
import me.amitay.minigames.utils.Utils;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class SpleefGameTask extends BukkitRunnable {
    private MiniGames pl;
    private Spleef spleef;
    private List<Player> players;

    public SpleefGameTask(Spleef spleef, MiniGames pl) {
        this.spleef = spleef;
        this.pl = pl;
        players = pl.getSpleefPlayerData().getAlive();
    }

    @Override
    public void run() {
        players = pl.getSpleefPlayerData().getAlive();
        if (players.size() == 1) {
            finishGame();
        }

        players.forEach(p -> {
            if (p.getLocation().getY() <= spleef.deathY && spleef.canBreakBlocks) {
                playerLoss(p);
            }
        });

    }
    private void finishGame() {
        players.get(0).sendMessage(Utils.getFormattedText("&aYou've won the spleef event! well done."));
        spleef.endGame(players.get(0));
        spleef.getjoinedPlayers().clear();
        cancel();
    }
    private void playerLoss(Player p){
        p.getInventory().clear();
        players.remove(p);
        pl.getSpleefPlayerData().getSpectators().add(p);
        p.teleport(spleef.getSpectatorsSpawn());
        p.sendMessage(Utils.getFormattedText("&eYou've lost the spleef event, you can now spectate the rest of the game here or return to the hub."));
    }
}

