package me.amitay.mini_games.manager.redrover;

import me.amitay.mini_games.MiniGames;
import me.amitay.mini_games.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Redrover {
    private MiniGames pl;
    List<Player> joinedPlayersRedrover = new ArrayList<>();
    List<Player> temp = new ArrayList<>();
    List<String> rewards, rewardsp = new ArrayList<>();
    List<ItemStack> kitAttacker = new ArrayList<>();
    List<ItemStack> kitRunner = new ArrayList<>();
    String area1, area2, attackerArea;
    Location spectators, runnersSpawn, attackerSpawn;
    int minPlayers, maxPlayers, timeToStart, timeToRun, countDown;
    boolean status;
    boolean info, canPlayersHitEachOther, currentlyRunning = false;
    BukkitTask countDownID, gameTask;
    Random rand = new Random();

    public Redrover(MiniGames pl) {
        this.pl = pl;
        try {
            area1 = pl.getConfig().getString("minigames.games.redrover.area1");
            area2 = pl.getConfig().getString("minigames.games.redrover.area2");
            runnersSpawn = (Location) pl.getConfig().get("minigames.games.redrover.runners-spawn");
            attackerSpawn = (Location) pl.getConfig().get("minigames.games.redrover.attacker-spawn");
            spectators = (Location) pl.getConfig().get("minigames.games.redrover.spectators-spawn");
            kitAttacker = (List<ItemStack>) pl.getConfig().getList("minigames.games.redrover.attackers-kit");
            kitRunner = (List<ItemStack>) pl.getConfig().getList("minigames.games.redrover.runners-kit");
            minPlayers = pl.getConfig().getInt("minigames.games.redrover.min-players");
            maxPlayers = pl.getConfig().getInt("minigames.games.redrover.max-players");
            timeToStart = pl.getConfig().getInt("minigames.games.redrover.time-to-start");
            timeToRun = pl.getConfig().getInt("minigames.games.redrover.time-to-run");
            rewards = pl.getConfig().getStringList("minigames.games.redrover.runner-rewards");
            rewardsp = pl.getConfig().getStringList("minigames.games.redrover.killer-rewards");
            canPlayersHitEachOther = pl.getConfig().getBoolean("minigames.games.redrover.allow-runners-to-hit-each-other");
            if (area1 != null && area2 != null && spectators != null && minPlayers != 0 && maxPlayers != 0 && timeToStart != 0 && runnersSpawn != null
                    && attackerSpawn != null && timeToRun != 0) {
                info = true;
            }
        } catch (NullPointerException e) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Redrover is not playable, you must check that everything is filled in the config.");
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
                    startGame(joinedPlayersRedrover);
                }
                if (countDown > 0 && countDown < 6)
                    Bukkit.broadcastMessage(Utils.getFormattedText("&eA redrover game will start in " + countDown + " second! &a/play redrover &eto join!"));
                else if (countDown % 15 == 0 && countDown != 0)
                    Bukkit.broadcastMessage(Utils.getFormattedText("&eA redrover game will start in " + countDown + " seconds! &a/play redrover &eto join!"));
                countDown--;
            }
        }.runTaskTimer(pl, 0, 20);
    }

    private void startGame(List<Player> list) {
        if (list.size() <= minPlayers) {
            Bukkit.broadcastMessage(Utils.getFormattedText("&eThe redrover game did not start because not enough people have joined it."));
            countDownID.cancel();
            return;
        }
        currentlyRunning = true;
        countDownID.cancel();
        pl.gamesManager.getCurrentlyPlaying().addAll(list);
        temp = list;
        Player attacker = list.remove(rand.nextInt(list.size()));
        pl.getRedroverPlayerData().setKiller(attacker);
        pl.getRedroverPlayerData().getAlive().addAll(list);
        attacker.teleport(attackerSpawn);
        if (kitAttacker != null) {
            System.out.println("not null");
            kitAttacker.forEach(item -> {
                attacker.getInventory().setItem(attacker.getInventory().firstEmpty(), item);
            });
        }
        attacker.sendMessage(Utils.getFormattedText("&eYou were chosen to be the attacker, kill everyone to gain a reward!"));
        list.forEach(p -> {
            if (kitRunner != null)
                kitRunner.forEach(item -> {
                    p.getInventory().addItem(item);
                });
            p.teleport(runnersSpawn);
        });
        gameTask = new RedroverGameTask(this, pl).runTaskTimer(pl, 0, 20);
    }

    public boolean isInfo() {
        return info;
    }

    public void endGame(Player p) {
        pl.getRedroverPlayerData().getSpectators().forEach(player -> {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "spawn " + player.getName());
            pl.getRedroverPlayerData().getSpectators().remove(player);
        });
        pl.getRedroverPlayerData().getAlive().forEach(player -> {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "spawn " + player.getName());
            pl.getRedroverPlayerData().getAlive().remove(player);
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
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "spawn " + p.getName());
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "spawn " + pl.getRedroverPlayerData().getKiller().getName());
        pl.getRedroverPlayerData().getKiller().getInventory().clear();
        Bukkit.broadcastMessage(Utils.getFormattedText("&aThe redrover game is now over! the winner was &a" + p.getName() + " &eand the killer was &a" + pl.getRedroverPlayerData().getKiller().getName() + "&e."));
        pl.getRedroverPlayerData().setKiller(null);
        joinedPlayersRedrover.clear();
        temp.clear();
        currentlyRunning = false;
    }

    public boolean getStatus() {
        return status;
    }

    public void addToList(Player p) {
        joinedPlayersRedrover.add(p);
    }

    public boolean enoughSpace() {
        return joinedPlayersRedrover.size() < maxPlayers;
    }

    public List<Player> getjoinedPlayersRedrover() {
        return joinedPlayersRedrover;
    }

    public boolean inGame(Player p) {
        return joinedPlayersRedrover.contains(p);
    }

    public Location getAttackerSpawn() {
        return attackerSpawn;
    }

    public List<ItemStack> getKitAttacker() {
        return kitAttacker;
    }

    public boolean isCanPlayersHitEachOther() {
        return canPlayersHitEachOther;
    }

    public Location getSpectatorsSpawn() {
        return spectators;
    }

    public boolean currentlyRunning() {
        return currentlyRunning;
    }
}
