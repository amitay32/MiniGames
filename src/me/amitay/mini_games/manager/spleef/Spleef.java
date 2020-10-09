package me.amitay.mini_games.manager.spleef;

import me.amitay.mini_games.MiniGames;
import me.amitay.mini_games.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Spleef {
    private MiniGames pl;
    double x, y, z;
    List<Player> temp = new ArrayList<>();
    List<Player> joinedPlayersSpleef = new ArrayList<>();
    List<String> rewards = new ArrayList<>();
    int minPlayers, maxPlayers, timeToStart, deathY, countDown;
    String world;
    Location locSpleef, spawn, spectators;
    boolean status, info, canBreakBlocks, canPlayersHitEachOther, currentlyRunning = false;
    BukkitTask countDownID, gameTask;
    private Random rand = new Random();

    public Spleef(MiniGames pl) {
        this.pl = pl;
        try {
            world = pl.getCustomConfigs().getSchemConfig().getCustomConfig().getString("spleef.schematic_location.world");
            x = pl.getCustomConfigs().getSchemConfig().getCustomConfig().getDouble("spleef.schematic_location.x");
            y = pl.getCustomConfigs().getSchemConfig().getCustomConfig().getDouble("spleef.schematic_location.y");
            z = pl.getCustomConfigs().getSchemConfig().getCustomConfig().getDouble("spleef.schematic_location.z");
            locSpleef = new Location(Bukkit.getWorld(world), x, y, z);
            spawn = (Location) pl.getConfig().get("minigames.games.spleef.spawn");
            spectators = (Location) pl.getConfig().get("minigames.games.spleef.spectators-spawn");
            minPlayers = pl.getConfig().getInt("minigames.games.spleef.min-players");
            maxPlayers = pl.getConfig().getInt("minigames.games.spleef.max-players");
            timeToStart = pl.getConfig().getInt("minigames.games.spleef.time-to-start");
            deathY = pl.getConfig().getInt("minigames.games.spleef.death-y");
            rewards = pl.getConfig().getStringList("minigames.games.spleef.rewards");
            canPlayersHitEachOther = pl.getConfig().getBoolean("minigames.games.spleef.allow-players-to-hit-each-other");
            if (locSpleef != null && spawn != null && spectators != null && minPlayers != 0 && maxPlayers != 0 && timeToStart != 0 && deathY != 0) {
                info = true;
            }

        } catch (Exception e) {
            e.printStackTrace();
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Spleef is not playable, you must check that everything is filled in the config.");
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
                    startGame(joinedPlayersSpleef);
                }
                if (countDown > 0 && countDown < 6)
                    Bukkit.broadcastMessage(Utils.getFormattedText("&eA spleef game will start in " + countDown + " second! &a/play spleef &eto join!"));
                else if (countDown % 15 == 0 && countDown != 0)
                    Bukkit.broadcastMessage(Utils.getFormattedText("&eA spleef game will start in " + countDown + " seconds! &a/play spleef &eto join!"));
                countDown--;
            }
        }.runTaskTimer(pl, 0, 20);
    }

    private void startGame(List<Player> list) {
        if (list.size() <= minPlayers) {
            Bukkit.broadcastMessage(Utils.getFormattedText("&eThe spleef game did not start because not enough people have joined it."));
            countDownID.cancel();
            return;
        }
        currentlyRunning = true;
        pl.getSchematicsManager().loadSchematic(locSpleef);
        countDownID.cancel();
        pl.gamesManager.getCurrentlyPlaying().addAll(list);
        temp = list;
        pl.getSpleefPlayerData().getAlive().addAll(list);
        breakCoolDown();
        list.forEach(p -> {
                    p.getInventory().addItem(new ItemStack(Material.DIAMOND_SPADE));
                    Utils.freezePlayer(spawn, p, 5);
                }
        );

        gameTask = new SpleefGameTask(this, pl).runTaskTimer(pl, 0, 5);
    }

    public boolean isInfo() {
        return info;
    }

    public void endGame(Player p) {
        pl.getSpleefPlayerData().getSpectators().forEach(player -> {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "spawn " + player.getName());
            pl.getSpleefPlayerData().getSpectators().remove(player);
        });
        pl.getSpleefPlayerData().getAlive().forEach(player -> {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "spawn " + player.getName());
            pl.getSpleefPlayerData().getAlive().remove(player);
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
        Bukkit.broadcastMessage(Utils.getFormattedText("&aThe spleef game is now over! the winner was &a" + p.getName() + "&e."));
        joinedPlayersSpleef.clear();
        temp.clear();
        canBreakBlocks = false;
        currentlyRunning = false;
    }

    public void breakCoolDown() {
        new BukkitRunnable() {
            int count = 200;

            @Override
            public void run() {
                count--;
                if (count == 0) {
                    pl.getSpleefPlayerData().getAlive().forEach(p -> p.sendMessage(Utils.getFormattedText("&aThe spleef game has started, good luck!")));
                    canBreakBlocks = true;
                    cancel();
                }
                if (count < 61)
                    if (count % 20 == 0 && count != 0)
                        pl.getSpleefPlayerData().getAlive().forEach(p -> p.sendMessage(Utils.getFormattedText("&eYou will be able to break blocks in " + count / 20)));

            }
        }.runTaskTimer(pl, 0, 1);
    }

    public boolean getStatus() {
        return status;
    }

    public void addToList(Player p) {
        joinedPlayersSpleef.add(p);
    }

    public boolean enoughSpace() {
        return joinedPlayersSpleef.size()
                < maxPlayers;
    }

    public List<Player> getjoinedPlayersspleef() {
        return joinedPlayersSpleef;
    }

    public boolean isCanPlayersHitEachOther() {
        return canPlayersHitEachOther;
    }

    public Location getSpectatorsSpawn() {
        return spectators;
    }
    public boolean isCanBreakBlocks(){
        return canBreakBlocks;
    }

    public boolean currentlyRunning() {
        return currentlyRunning;
    }
    public boolean inGame(Player p) {
        return joinedPlayersSpleef.contains(p);
    }
}
