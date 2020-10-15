package me.amitay.minigames.manager.waterdrop;

import me.amitay.minigames.MiniGames;
import me.amitay.minigames.manager.Game;
import me.amitay.minigames.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class WaterDrop extends Game {
    private List<Location> waterDropArena = new ArrayList<>();
    private int timeToJump, yLevel, maxPlayTime;

    public WaterDrop(MiniGames pl) {
        super(false, false, false, "minigames.games.waterdrop.min-players", "minigames.games.waterdrop.max-players",
                "minigames.games.waterdrop.time-to-start",
                "minigames.games.waterdrop.rewards", "minigames.games.waterdrop.spawn", "minigames.games.waterdrop.spectators-spawn", pl);
        try {
            timeToJump = pl.getConfig().getInt("minigames.games.waterdrop.time_to_jump");
            yLevel = pl.getConfig().getInt("minigames.games.waterdrop.y_level");
            waterDropArena = (List<Location>) pl.getConfig().getList("minigames.games.waterdrop.stages");
            maxPlayTime = pl.getConfig().getInt("minigames.games.lms.max-play-time");
            if (minPlayers != 0 && maxPlayers != 0 && timeToStart != 0 && timeToJump != 0 && waterDropArena != null) {
                info = true;
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Waterdrop is not playable, you must check that everything is filled in the config.");
            info = false;
        }
    }

    public void startCountDown() {
        countDown = timeToStart;
        status = true;
        countDownID = new BukkitRunnable() {
            @Override
            public void run() {
                if (countDown == 0) {
                    status = false;
                    startGame();
                }
                if (countDown > 0 && countDown < 6)
                    Bukkit.broadcastMessage(Utils.getFormattedText("&eA waterdrop game will start in " + countDown + " second! &a/play waterdrop &eto join!"));
                else if (countDown % 15 == 0 && countDown != 0)
                    Bukkit.broadcastMessage(Utils.getFormattedText("&eA waterdrop game will start in " + countDown + " seconds! &a/play waterdrop &eto join!"));
                countDown--;
            }
        }.runTaskTimer(pl, 0, 20);
    }

    public void forceEndGame() {
        pl.getWaterdropPlayerData().getPlayerMap().forEach((player, v) -> {
            player.teleport(pl.gamesManager.getMainSpawn());
            pl.getWaterdropPlayerData().getPlayerMap().remove(player);
            player.getInventory().clear();
        });
        pl.gamesManager.getCurrentlyPlaying().removeAll(temp);
        Bukkit.broadcastMessage(Utils.getFormattedText("&aThe waterdrop game is now over!"));
        joinedPlayers.clear();
        temp.clear();
        currentlyRunning = false;
    }

    public void removePlayerFromGame(Player p) {
        pl.getWaterdropPlayerData().getPlayerMap().remove(p);
        temp.remove(p);
        p.teleport(pl.gamesManager.getMainSpawn());
        p.getInventory().clear();
    }

    public void startGame() {
        if (joinedPlayers.size() <= minPlayers) {
            Bukkit.broadcastMessage(Utils.getFormattedText("&eThe waterdrop game did not start because not enough people have joined it."));
            countDownID.cancel();
            return;
        }
        currentlyRunning = true;
        countDownID.cancel();
        pl.gamesManager.getCurrentlyPlaying().addAll(joinedPlayers);
        temp = joinedPlayers;
        joinedPlayers.forEach(player -> {
            pl.getWaterdropPlayerData().getPlayerMap().put(player, 1);
            player.teleport(waterDropArena.get(0));
        });
        gameTask = new WaterDropGameTask(this, pl).runTaskTimer(pl, 0, 3);
    }

    public List<Location> getWaterDropArena() {
        return waterDropArena;
    }

    public int getYLevel() {
        return yLevel;
    }

    @Override
    public void endGame(Player p) {
        pl.getWaterdropPlayerData().getPlayerMap().forEach((player, v) -> player.teleport(pl.gamesManager.getMainSpawn()));
        pl.getWaterdropPlayerData().getPlayerMap().clear();
        temp.forEach(player -> {
            player.getInventory().clear();
        });
        pl.gamesManager.getCurrentlyPlaying().removeAll(temp);
        try {
            rewards.forEach(string -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), string.replace("%player%", p.getName())));
        } catch (Exception e) {
            e.printStackTrace();
        }
        p.teleport(pl.gamesManager.getMainSpawn());
        Bukkit.broadcastMessage(Utils.getFormattedText("&aThe waterdrop game is now over! the winner was &a" + p.getName() + "&e."));
        joinedPlayers.clear();
        temp.clear();
        currentlyRunning = false;
    }
}
