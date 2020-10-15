package me.amitay.minigames.manager.lms;

import me.amitay.minigames.MiniGames;
import me.amitay.minigames.manager.Game;
import me.amitay.minigames.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class Lms extends Game {
    List<String> onKill = new ArrayList<>();
    List<ItemStack> kit = new ArrayList<>();
    int healHp, maxPlayTime;
    boolean canFight = false;

    public Lms(MiniGames pl) {
        super(false, false, false, "minigames.games.lms.min-players", "minigames.games.lms.max-players", "minigames.games.lms.time-to-start",
                "minigames.games.lms.rewards", "minigames.games.lms.spawn", "minigames.games.lms.spectators-spawn", pl);
        try {
            kit = (List<ItemStack>) pl.getConfig().getList("minigames.games.lms.kit");
            maxPlayTime = pl.getConfig().getInt("minigames.games.lms.max-play-time");
            healHp = pl.getConfig().getInt("minigames.games.lms.heal-on-kill");
            onKill = pl.getConfig().getStringList("minigames.games.lms.on-kill-commands");
            if (spawn != null && spectators != null && minPlayers != 0 && maxPlayers != 0 && maxPlayTime != 0 && timeToStart != 0 && kit != null) {
                info = true;
            }

        } catch (Exception e) {
            e.printStackTrace();
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "lms is not playable, you must check that everything is filled in the config.");
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
                    Bukkit.broadcastMessage(Utils.getFormattedText("&eA lms game will start in " + countDown + " second! &a/play lms &eto join!"));
                else if (countDown % 15 == 0 && countDown != 0)
                    Bukkit.broadcastMessage(Utils.getFormattedText("&eA lms game will start in " + countDown + " seconds! &a/play lms &eto join!"));
                countDown--;
            }
        }.runTaskTimer(pl, 0, 20);
    }

    public void forceEndGame() {
        pl.getLmsPlayerData().getSpectators().forEach(player -> {
            player.teleport(pl.gamesManager.getMainSpawn());
            pl.getLmsPlayerData().getSpectators().remove(player);
        });
        pl.getLmsPlayerData().getAlive().forEach(player -> {
            player.teleport(pl.gamesManager.getMainSpawn());
            pl.getLmsPlayerData().getAlive().remove(player);
        });
        temp.forEach(player -> {
            player.getInventory().clear();
        });
        pl.gamesManager.getCurrentlyPlaying().removeAll(temp);
        Bukkit.broadcastMessage(Utils.getFormattedText("&aThe lms game is now over."));
        joinedPlayers.clear();
        temp.clear();
        currentlyRunning = false;
        canFight = false;
    }

    public void removePlayerFromGame(Player p) {
        pl.getLmsPlayerData().getAlive().remove(p);
        pl.getLmsPlayerData().getSpectators().remove(p);
        pl.gamesManager.getLmsGame().getTemp().remove(p);
        p.teleport(pl.gamesManager.getMainSpawn());
        p.getInventory().clear();
    }

    public void startGame() {
        if (joinedPlayers.size() <= minPlayers) {
            Bukkit.broadcastMessage(Utils.getFormattedText("&eThe lms game did not start because not enough people have joined it."));
            countDownID.cancel();
            return;
        }
        currentlyRunning = true;
        countDownID.cancel();
        pl.gamesManager.getCurrentlyPlaying().addAll(joinedPlayers);
        temp = joinedPlayers;
        pl.getLmsPlayerData().getAlive().addAll(joinedPlayers);
        pl.getLmsPlayerData().getAlive().forEach(player -> {
            player.teleport(spawn);
        });
        coolDownLms();
        gameTask = new LmsGameTask(this, pl).runTaskTimer(pl, 0, 20);
    }

    public void endGame(Player p) {
        pl.getLmsPlayerData().getSpectators().forEach(player -> {
            player.teleport(pl.gamesManager.getMainSpawn());
            pl.getLmsPlayerData().getSpectators().remove(player);
        });
        pl.getLmsPlayerData().getAlive().forEach(player -> {
            player.teleport(pl.gamesManager.getMainSpawn());
            pl.getLmsPlayerData().getAlive().remove(player);
        });
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
        Bukkit.broadcastMessage(Utils.getFormattedText("&aThe lms game is now over! the winner was &a" + p.getName() + "&e."));
        joinedPlayers.clear();
        temp.clear();
        currentlyRunning = false;
        canFight = false;
    }
    public void endGameNoReward(){
        pl.getLmsPlayerData().getSpectators().forEach(player -> {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "spawn " + player.getName());
            pl.getLmsPlayerData().getSpectators().remove(player);
        });
        pl.getLmsPlayerData().getAlive().forEach(player -> {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "spawn " + player.getName());
            pl.getLmsPlayerData().getAlive().remove(player);
        });
        temp.forEach(player -> {
            player.getInventory().clear();
        });
        pl.gamesManager.getCurrentlyPlaying().removeAll(temp);
        joinedPlayers.clear();
        temp.clear();
        currentlyRunning = false;
        canFight = false;
    }

    public void coolDownLms() {
        new BukkitRunnable() {
            int count = 301;

            @Override
            public void run() {
                count--;
                if (count == 0) {
                    pl.getLmsPlayerData().getAlive().forEach(p -> p.sendMessage(Utils.getFormattedText("&aThe lms game has started, good luck!")));
                    canFight = true;
                    cancel();
                }
                if (count <= 300 && count >= 200 && count % 20 == 0 && count - 200 != 0)
                    pl.getLmsPlayerData().getAlive().forEach(p -> p.sendMessage(Utils.getFormattedText("&eYou will get your kits in " + (count - 200) / 20)));
                if (count == 200)
                    joinedPlayers.forEach(p -> {
                        kit.forEach(itemStack -> {
                            p.getInventory().addItem(itemStack);
                        });
                    });
                if (count <= 100 && count % 20 == 0 && count != 0)
                    pl.getLmsPlayerData().getAlive().forEach(p -> p.sendMessage(Utils.getFormattedText("&ePvp will be enabled in " + count/20)));
            }
        }.runTaskTimer(pl, 0, 1);
    }

    public boolean getStatus() {
        return status;
    }

    public void addToList(Player p) {
        joinedPlayers.add(p);
    }

    public List<Player> getjoinedPlayers() {
        return joinedPlayers;
    }

    public Location getSpectatorsSpawn() {
        return spectators;
    }

    public boolean currentlyRunning() {
        return currentlyRunning;
    }


    public int getHealHp() {
        return healHp;
    }

    public List<String> getOnKill() {
        if (onKill != null)
            return onKill;
        return null;
    }
}

