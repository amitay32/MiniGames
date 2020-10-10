package me.amitay.mini_games.listeners;

import me.amitay.mini_games.MiniGames;
import me.amitay.mini_games.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerDieListener implements Listener {
    private MiniGames pl;

    public PlayerDieListener(MiniGames pl) {
        this.pl = pl;
    }

    @EventHandler
    public void playerDeathEvent(PlayerDeathEvent e) {
        if (pl.gamesManager.getCurrentlyPlaying().contains(e.getEntity())) {
            Player p = e.getEntity();
            Player killer = e.getEntity().getKiller();
            if (pl.getRedroverPlayerData().getAlive().contains(e.getEntity())) {
                p.getInventory().clear();
                pl.getRedroverPlayerData().getAlive().remove(p);
                pl.getRedroverPlayerData().getSpectators().add(p);
                p.teleport(pl.gamesManager.getRedroverGame().getSpectatorsSpawn());
                p.sendMessage(Utils.getFormattedText("&eYou've lost the redrover event, you can now spectate the rest of the game here or return to the hub."));
            }
            if (pl.getSpleefPlayerData().getAlive().contains(e.getEntity())) {
                p.getInventory().clear();
                pl.getSpleefPlayerData().getAlive().remove(p);
                pl.getSpleefPlayerData().getSpectators().add(p);
                p.teleport(pl.gamesManager.getSpleefGame().getSpectatorsSpawn());
                p.sendMessage(Utils.getFormattedText("&eYou've lost the spleef event, you can now spectate the rest of the game here or return to the hub."));
            }
            if (pl.getLmsPlayerData().getAlive().contains(e.getEntity())) {
                p.getInventory().clear();
                pl.getLmsPlayerData().getAlive().remove(p);
                pl.getLmsPlayerData().getSpectators().add(p);
                p.teleport(pl.gamesManager.getLmsGame().getSpectatorsSpawn());
                p.sendMessage(Utils.getFormattedText("&eYou've lost the lms event, you can now spectate the rest of the game here or return to the hub."));
            }
            if (pl.getLmsPlayerData().getAlive().contains(killer)) {
                killer.setHealth(pl.gamesManager.getLmsGame().getHealHp());
                if (pl.gamesManager.getLmsGame().getOnKill() != null)
                    pl.gamesManager.getLmsGame().getOnKill().forEach(s -> {
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), s.replace("%player", killer.getName()));
                    });
                pl.getLmsPlayerData().getAlive().forEach(player ->{
                    player.sendMessage(Utils.getFormattedText("&a" + killer.getName() + " &eHas killed &a " + p.getName() + " &e. " + pl.getLmsPlayerData().getAlive().size() + " people remain on the game."));
                });

            }
        }
    }
}
