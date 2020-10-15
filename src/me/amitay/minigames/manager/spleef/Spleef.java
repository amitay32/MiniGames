package me.amitay.minigames.manager.spleef;

import me.amitay.minigames.MiniGames;
import me.amitay.minigames.manager.Game;
import me.amitay.minigames.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class Spleef extends Game {
    double x, y, z;
    int deathY;
    String world;
    Location locSpleef;
    boolean canBreakBlocks, canPlayersHitEachOther = false;

    public Spleef(MiniGames pl) {
        super(false, false, false, "minigames.games.spleef.min-players", "minigames.games.spleef.max-players",
                "minigames.games.spleef.time-to-start",
                "minigames.games.spleef.rewards", "minigames.games.spleef.spawn", "minigames.games.spleef.spectators-spawn", pl);
        try {
            world = pl.getCustomConfigs().getSchemConfig().getCustomConfig().getString("spleef.schematic_location.world");
            x = pl.getCustomConfigs().getSchemConfig().getCustomConfig().getDouble("spleef.schematic_location.x");
            y = pl.getCustomConfigs().getSchemConfig().getCustomConfig().getDouble("spleef.schematic_location.y");
            z = pl.getCustomConfigs().getSchemConfig().getCustomConfig().getDouble("spleef.schematic_location.z");
            locSpleef = new Location(Bukkit.getWorld(world), x, y, z);
            deathY = pl.getConfig().getInt("minigames.games.spleef.death-y");
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
                    startGame();
                }
                if (countDown > 0 && countDown < 6)
                    Bukkit.broadcastMessage(Utils.getFormattedText("&eA spleef game will start in " + countDown + " second! &a/play spleef &eto join!"));
                else if (countDown % 15 == 0 && countDown != 0)
                    Bukkit.broadcastMessage(Utils.getFormattedText("&eA spleef game will start in " + countDown + " seconds! &a/play spleef &eto join!"));
                countDown--;
            }
        }.runTaskTimer(pl, 0, 20);
    }

    public void forceEndGame() {
        pl.getSpleefPlayerData().getSpectators().forEach(player -> {
            player.teleport(pl.gamesManager.getMainSpawn());
            pl.getSpleefPlayerData().getSpectators().remove(player);
        });
        pl.getSpleefPlayerData().getAlive().forEach(player -> {
            player.teleport(pl.gamesManager.getMainSpawn());
            pl.getSpleefPlayerData().getAlive().remove(player);
        });
        temp.forEach(player -> {
            player.getInventory().clear();
        });
        pl.gamesManager.getCurrentlyPlaying().removeAll(temp);
        Bukkit.broadcastMessage(Utils.getFormattedText("&aThe spleef game is now over."));
        joinedPlayers.clear();
        temp.clear();
        canBreakBlocks = false;
        currentlyRunning = false;
    }


    public void removePlayerFromGame(Player p) {
        pl.getSpleefPlayerData().getAlive().remove(p);
        pl.getSpleefPlayerData().getSpectators().remove(p);
        temp.remove(p);
        p.teleport(pl.gamesManager.getMainSpawn());
        p.getInventory().clear();
    }


    public void startGame() {
        if (joinedPlayers.size() <= minPlayers) {
            Bukkit.broadcastMessage(Utils.getFormattedText("&eThe spleef game did not start because not enough people have joined it."));
            countDownID.cancel();
            return;
        }
        currentlyRunning = true;
        pl.getSchematicsManager().loadSchematic(locSpleef);
        countDownID.cancel();
        pl.gamesManager.getCurrentlyPlaying().addAll(joinedPlayers);
        temp = joinedPlayers;
        pl.getSpleefPlayerData().getAlive().addAll(joinedPlayers);
        breakCoolDown();
        joinedPlayers.forEach(p -> {
                    p.getInventory().addItem(new ItemStack(Material.DIAMOND_SPADE));
                    Utils.freezePlayer(spawn, p, 5);
                }
        );

        gameTask = new SpleefGameTask(this, pl).runTaskTimer(pl, 0, 5);
    }

    public void endGame(Player p) {
        pl.getSpleefPlayerData().getSpectators().forEach(player -> {
            player.teleport(pl.gamesManager.getMainSpawn());
            pl.getSpleefPlayerData().getSpectators().remove(player);
        });
        pl.getSpleefPlayerData().getAlive().forEach(player -> {
            player.teleport(pl.gamesManager.getMainSpawn());
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
        p.teleport(pl.gamesManager.getMainSpawn());
        Bukkit.broadcastMessage(Utils.getFormattedText("&aThe spleef game is now over! the winner was &a" + p.getName() + "&e."));
        joinedPlayers.clear();
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
        joinedPlayers.add(p);
    }

    public List<Player> getjoinedPlayers() {
        return joinedPlayers;
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
}
