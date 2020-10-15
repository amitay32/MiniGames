package me.amitay.minigames.manager.redrover;

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

public class Redrover extends Game {
    List<String> rewardsp = new ArrayList<>();
    List<ItemStack> kitAttacker = new ArrayList<>();
    List<ItemStack> kitRunner = new ArrayList<>();
    String area1, area2;
    Location attackerSpawn;
    int timeToRun;
    boolean canPlayersHitEachOther = false;

    public Redrover(MiniGames pl) {
        super(false, false, false, "minigames.games.redrover.min-players", "minigames.games.redrover.max-players",
                "minigames.games.redrover.time-to-start", "minigames.games.redrover.rewards",
                "minigames.games.redrover.runners-spawn", "minigames.games.redrover.spectators-spawn", pl);
        try {
            area1 = pl.getConfig().getString("minigames.games.redrover.area1");
            area2 = pl.getConfig().getString("minigames.games.redrover.area2");
            attackerSpawn = (Location) pl.getConfig().get("minigames.games.redrover.attacker-spawn");
            kitAttacker = (List<ItemStack>) pl.getConfig().getList("minigames.games.redrover.attackers-kit");
            kitRunner = (List<ItemStack>) pl.getConfig().getList("minigames.games.redrover.runners-kit");
            timeToRun = pl.getConfig().getInt("minigames.games.redrover.time-to-run");
            rewardsp = pl.getConfig().getStringList("minigames.games.redrover.killer-rewards");
            canPlayersHitEachOther = pl.getConfig().getBoolean("minigames.games.redrover.allow-runners-to-hit-each-other");
            if (area1 != null && area2 != null && spectators != null && minPlayers != 0 && maxPlayers != 0 && timeToStart != 0 && spawn != null
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
                    startGame();
                }
                if (countDown > 0 && countDown < 6)
                    Bukkit.broadcastMessage(Utils.getFormattedText("&eA redrover game will start in " + countDown + " second! &a/play redrover &eto join!"));
                else if (countDown % 15 == 0 && countDown != 0)
                    Bukkit.broadcastMessage(Utils.getFormattedText("&eA redrover game will start in " + countDown + " seconds! &a/play redrover &eto join!"));
                countDown--;
            }
        }.runTaskTimer(pl, 0, 20);
    }

    public void forceEndGame() {
        pl.getRedroverPlayerData().getSpectators().forEach(player -> {
            player.teleport(pl.gamesManager.getMainSpawn());
            pl.getRedroverPlayerData().getSpectators().remove(player);
        });
        pl.getRedroverPlayerData().getAlive().forEach(player -> {
            player.teleport(pl.gamesManager.getMainSpawn());
            pl.getRedroverPlayerData().getAlive().remove(player);
        });
        temp.forEach(player -> {
            player.getInventory().clear();
        });
        pl.gamesManager.getCurrentlyPlaying().removeAll(temp);
        pl.getRedroverPlayerData().getKiller().teleport(pl.gamesManager.getMainSpawn());
        pl.getRedroverPlayerData().getKiller().getInventory().clear();
        Bukkit.broadcastMessage(Utils.getFormattedText("&aThe redrover game is now over."));
        pl.getRedroverPlayerData().setKiller(null);
        joinedPlayers.clear();
        temp.clear();
        currentlyRunning = false;
    }

    public void removePlayerFromGame(Player p) {
        pl.getRedroverPlayerData().getAlive().remove(p);
        pl.getRedroverPlayerData().getSpectators().remove(p);
        temp.remove(p);
        p.teleport(pl.gamesManager.getMainSpawn());
        p.getInventory().clear();
    }
    public void killerDisconnect(Player p){
        if (pl.getRedroverPlayerData().getAlive().size() > 1) {
            Player player = pl.getRedroverPlayerData().getAlive().get(rand.nextInt(pl.getRedroverPlayerData().getAlive().size()));
            pl.getRedroverPlayerData().setKiller(player);
            player.sendMessage(Utils.getFormattedText("&eThe current killer has disconnected, you were randomly chosen to be the new one. (the killer get rewards at the end of the game)"));
            player.teleport(pl.gamesManager.getRedroverGame().getAttackerSpawn());
            pl.getRedroverPlayerData().getAlive().remove(player);
            player.getInventory().clear();
            pl.gamesManager.getRedroverGame().getKitAttacker().forEach(itemStack -> {
                player.getInventory().addItem(itemStack);
            });
        }
    }

    public void startGame() {
        if (joinedPlayers.size() <= minPlayers) {
            Bukkit.broadcastMessage(Utils.getFormattedText("&eThe redrover game did not start because not enough people have joined it."));
            countDownID.cancel();
            return;
        }
        currentlyRunning = true;
        countDownID.cancel();
        pl.gamesManager.getCurrentlyPlaying().addAll(joinedPlayers);
        temp = joinedPlayers;
        Player attacker = joinedPlayers.remove(rand.nextInt(joinedPlayers.size()));
        pl.getRedroverPlayerData().setKiller(attacker);
        pl.getRedroverPlayerData().getAlive().addAll(joinedPlayers);
        attacker.teleport(attackerSpawn);
        if (kitAttacker != null) {
            kitAttacker.forEach(item -> {
                attacker.getInventory().setItem(attacker.getInventory().firstEmpty(), item);
            });
        }
        attacker.sendMessage(Utils.getFormattedText("&eYou were chosen to be the attacker, kill everyone to gain a reward!"));
        joinedPlayers.forEach(p -> {
            if (kitRunner != null)
                kitRunner.forEach(item -> {
                    p.getInventory().addItem(item);
                });
            p.teleport(spawn);
        });
        gameTask = new RedroverGameTask(this, pl).runTaskTimer(pl, 0, 20);
    }

    public void endGame(Player p) {
        pl.getRedroverPlayerData().getSpectators().forEach(player -> {
            player.teleport(pl.gamesManager.getMainSpawn());
            pl.getRedroverPlayerData().getSpectators().remove(player);
        });
        pl.getRedroverPlayerData().getAlive().forEach(player -> {
            player.teleport(pl.gamesManager.getMainSpawn());
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
        p.teleport(pl.gamesManager.getMainSpawn());
        pl.getRedroverPlayerData().getKiller().teleport(pl.gamesManager.getMainSpawn());
        pl.getRedroverPlayerData().getKiller().getInventory().clear();
        Bukkit.broadcastMessage(Utils.getFormattedText("&aThe redrover game is now over! the winner was &a" + p.getName() + " &eand the killer was &a" + pl.getRedroverPlayerData().getKiller().getName() + "&e."));
        pl.getRedroverPlayerData().setKiller(null);
        joinedPlayers.clear();
        temp.clear();
        currentlyRunning = false;
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
