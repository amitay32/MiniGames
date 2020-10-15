package me.amitay.minigames.manager.sumo;

import me.amitay.minigames.MiniGames;
import me.amitay.minigames.manager.Game;
import me.amitay.minigames.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.Random;


public class Sumo extends Game {
    Location p1, p2;
    Player currentP1, currentP2;
    boolean fighting = false;

    public Sumo(MiniGames pl) {
        super(false, false, false, "minigames.games.sumo.min-players", "minigames.games.sumo.max-players",
                "minigames.games.sumo.time-to-start",
                "minigames.games.sumo.rewards", "minigames.games.sumo.spawn", "minigames.games.sumo.spectators-spawn", pl);
        try {
            p1 = (Location) pl.getConfig().get("minigames.games.sumo.player-location-1");
            p2 = (Location) pl.getConfig().get("minigames.games.sumo.player-location-2");
            status = false;
            if (p1 != null && p2 != null && spectators != null && minPlayers != 0 && maxPlayers != 0 && timeToStart != 0 && spawn != null) {
                info = true;
            }
        } catch (NullPointerException e) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Sumo is not playable, you must check that everything is filled in the config.");
            info = false;
        }
    }

    public void addToList(Player p) {
        joinedPlayers.add(p);
    }

    public boolean enoughSpace() {
        return joinedPlayers.size() < maxPlayers;
    }

    public void startCountDown() {
        status = true;
        countDown = timeToStart;
        countDownID = new BukkitRunnable() {
            @Override
            public void run() {
                if (countDown == 0) {
                    status = false;
                    startGame();
                }
                if (countDown > 0 && countDown < 6)
                    Bukkit.broadcastMessage(Utils.getFormattedText("&eA sumo game will start in " + countDown + " second! &a/play sumo &eto join!"));
                else if (countDown % 15 == 0 && countDown != 0)
                    Bukkit.broadcastMessage(Utils.getFormattedText("&eA sumo game will start in " + countDown + " seconds! &a/play sumo &eto join!"));
                countDown--;
            }
        }.runTaskTimer(pl, 0, 20);
    }

    public void forceEndGame() {
        pl.getSumoPlayerData().getPlayerAvailableForFight().forEach(player -> {
            player.teleport(pl.gamesManager.getMainSpawn());
            pl.getSumoPlayerData().getPlayerAvailableForFight().remove(player);
        });
        pl.getSumoPlayerData().getUnavailableForFight().forEach(player -> {
            player.teleport(pl.gamesManager.getMainSpawn());
            pl.getSumoPlayerData().getUnavailableForFight().remove(player);
        });
        pl.getSumoPlayerData().getSpectators().forEach(player -> {
            player.teleport(pl.gamesManager.getMainSpawn());
            pl.getSumoPlayerData().getSpectators().remove(player);
        });
        pl.gamesManager.getCurrentlyPlaying().removeAll(temp);
        currentlyRunning = false;
        joinedPlayers.clear();
        temp.clear();
    }

    public void removePlayerFromGame(Player p) {
        pl.gamesManager.getCurrentlyPlaying().remove(p);
        p.getInventory().clear();
        p.teleport(pl.gamesManager.getMainSpawn());
        if (isFighting(p)) {
            playerWin(pl.getSumoPlayerData().getCurrentlyFighting().get(0), joinedPlayers);
            playerLose(p, joinedPlayers);
        }
        pl.getSumoPlayerData().getSpectators().remove(p);
        pl.getSumoPlayerData().getUnavailableForFight().remove(p);
        pl.getSumoPlayerData().getPlayerAvailableForFight().remove(p);
        temp.remove(p);
        joinedPlayers.remove(p);
        pl.gamesManager.getCurrentlyPlaying().remove(p);
        p.getInventory().clear();
        p.teleport(pl.gamesManager.getMainSpawn());

    }


    public void startGame() {
        if (joinedPlayers.size() < minPlayers) {
            Bukkit.broadcastMessage(Utils.getFormattedText("&eThe sumo game did not start because not enough people have joined it."));
            countDownID.cancel();
            return;
        }
        currentlyRunning = true;
        countDownID.cancel();
        pl.gamesManager.getCurrentlyPlaying().addAll(joinedPlayers);
        temp = joinedPlayers;
        pl.getSumoPlayerData().getPlayerAvailableForFight().addAll(joinedPlayers);
        joinedPlayers.forEach(p -> {
            p.teleport(spawn);
            p.sendMessage(Utils.getFormattedText("&eThe sumo game will begin in 5 seconds."));
        });
        gameTask = new SumoGameTask(this, pl, joinedPlayers).runTaskTimer(pl, 0, 10);
    }

    public void freezePlayerSumo(Player p) {
        new BukkitRunnable() {
            int count = 100;

            @Override
            public void run() {
                count--;
                if (count == 0)
                    cancel();
                if (count % 20 == 0)
                    p.sendMessage(Utils.getFormattedText("&eYour match will start in " + count / 20));
                if (p.equals(currentP1) && p.getLocation() != p1) {
                    p.teleport(p1);
                }
                if (p.equals(currentP2) && p.getLocation() != p2) {
                    p.teleport(p2);
                }
            }
        }.runTaskTimer(pl, 0, 1);
    }

    public void playerLose(Player p, List<Player> list) {
        pl.getSumoPlayerData().getSpectators().add(p);
        p.sendMessage(Utils.getFormattedText("&eYou've lost the sumo event, you can now spectate the rest of the game here or return to the hub."));
        p.teleport(spectators);
        pl.getSumoPlayerData().getCurrentlyFighting().clear();
        list.remove(p);
    }

    public void playerWin(Player p, List<Player> list) {
        if (list.size() == 1) {
            return;
        }
        pl.getSumoPlayerData().getCurrentlyFighting().clear();
        pl.getSumoPlayerData().getUnavailableForFight().add(p);
        p.sendMessage(Utils.getFormattedText("&eYou've won this round, fresh up for your next fight!"));
        p.teleport(spawn);

    }

    public void endGame(Player p) {
        pl.getSumoPlayerData().getPlayerAvailableForFight().forEach(player -> {
            player.teleport(pl.gamesManager.getMainSpawn());
            pl.getSumoPlayerData().getPlayerAvailableForFight().remove(player);
        });
        pl.getSumoPlayerData().getUnavailableForFight().forEach(player -> {
            player.teleport(pl.gamesManager.getMainSpawn());
            pl.getSumoPlayerData().getUnavailableForFight().remove(player);
        });
        pl.getSumoPlayerData().getSpectators().forEach(player -> {
            player.teleport(pl.gamesManager.getMainSpawn());
            pl.getSumoPlayerData().getSpectators().remove(player);
        });
        pl.gamesManager.getCurrentlyPlaying().removeAll(temp);
        try {
            rewards.forEach(string -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), string.replace("%player%", p.getName())));
        } catch (Exception e) {
            e.printStackTrace();
        }
        p.teleport(pl.gamesManager.getMainSpawn());
        currentlyRunning = false;
        joinedPlayers.clear();
    }

    public boolean getStatus() {
        return status;
    }

    public List<Player> getJoinedPlayers() {
        return joinedPlayers;
    }

    public boolean isFighting(Player p) {
        return pl.getSumoPlayerData().getCurrentlyFighting().contains(p);
    }

    public Random getRand() {
        return rand;
    }
}
