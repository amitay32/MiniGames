package me.amitay.minigames.listeners;

import me.amitay.minigames.MiniGames;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class PlayerDamagePlayerListener implements Listener {
    private MiniGames pl;

    public PlayerDamagePlayerListener(MiniGames pl) {
        this.pl = pl;
    }

    @EventHandler
    public void playerDamageEntity(EntityDamageByEntityEvent e) {
        if (pl.gamesManager.getCurrentlyPlaying().contains(e.getDamager())) {
            if (pl.getSumoPlayerData().getCurrentlyFighting().contains(e.getDamager())) {
                if (e.getDamager() instanceof Player) {
                    Player p = (Player) e.getDamager();
                    p.setHealth(20);
                }
                if (e.getEntity() instanceof Player) {
                    Player p = (Player) e.getEntity();
                    p.setHealth(20);
                }
                return;
            }
            if (pl.gamesManager.getRedroverGame().currentlyRunning()) {
                if (pl.getRedroverPlayerData().getKiller().equals(e.getDamager())) {
                    return;
                }
                if (pl.gamesManager.getRedroverGame().isCanPlayersHitEachOther()) {
                    return;
                }
            }
            if (pl.gamesManager.getSpleefGame().currentlyRunning()) {
                if (pl.gamesManager.getSpleefGame().isCanPlayersHitEachOther() && pl.gamesManager.getSpleefGame().isCanBreakBlocks()) {
                    if (e.getDamager() instanceof Player) {
                        Player p = (Player) e.getDamager();
                        p.setHealth(20);
                    }
                    if (e.getEntity() instanceof Player) {
                        Player p1 = (Player) e.getEntity();
                        p1.setHealth(20);
                    }
                    return;
                }
            }
            if (pl.gamesManager.getLmsGame().currentlyRunning())
                if(pl.getLmsPlayerData().getAlive().contains(e.getDamager()) && pl.getLmsPlayerData().getAlive().contains(e.getEntity())){
                    return;
                }
            e.setCancelled(true);
        }
    }
}
