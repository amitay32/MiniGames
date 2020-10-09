package me.amitay.mini_games.manager.sumo;

import me.amitay.mini_games.MiniGames;
import me.amitay.mini_games.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class Sumo {
    private MiniGames pl;
    List<Player> joinedPlayers = new ArrayList<>();
    List<Player> temp = new ArrayList<>();
    List<String> rewards = new ArrayList<>();
    Location p1, p2, lobby, spectators;
    int minPlayers, maxPlayers, timeToStart;
    BukkitTask countDownID, gameTask;
    Player currentP1, currentP2;
    Random rand = new Random();
    boolean status, info, fighting = false;

    public Sumo(MiniGames pl) {
        this.pl = pl;
        try {
            p1 = (Location) pl.getConfig().get("minigames.games.sumo.player-location-1");
            p2 = (Location) pl.getConfig().get("minigames.games.sumo.player-location-2");
            lobby = (Location) pl.getConfig().get("minigames.games.sumo.lobby-spawn");
            spectators = (Location) pl.getConfig().get("minigames.games.sumo.spectators-spawn");
            minPlayers = pl.getConfig().getInt("minigames.games.sumo.min-players");
            maxPlayers = pl.getConfig().getInt("minigames.games.sumo.max-players");
            timeToStart = pl.getConfig().getInt("minigames.games.sumo.time-to-start");
            rewards = pl.getConfig().getStringList("minigames.games.sumo.rewards");
            status = false;
            if (p1 != null && p2 != null && spectators != null && minPlayers != 0 && maxPlayers != 0 && timeToStart != 0 && lobby != null) {
                info = true;
            }
        } catch (NullPointerException e) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Sumo is not playable, you must check that everything is filled in the config.");
            info = false;
        }
    }

    public boolean getinfo(){
        return info;
    }
    public void addToList(Player p){
        joinedPlayers.add(p);
    }
    public boolean enoughSpace(){
        return joinedPlayers.size() < maxPlayers;
    }
    public void startCountDown() {
        status = true;
        countDownID = new BukkitRunnable() {
            int countDown = timeToStart;
            @Override
            public void run() {
                if (countDown == 0) {
                    status = false;
                    startGame(joinedPlayers);
                }
                if (countDown > 0 && countDown < 6)
                    Bukkit.broadcastMessage(Utils.getFormattedText("&eA sumo game will start in " + countDown + " second! &a/play sumo &eto join!"));
                else if (countDown % 15 == 0 && countDown != 0)
                    Bukkit.broadcastMessage(Utils.getFormattedText("&eA sumo game will start in " + countDown + " seconds! &a/play sumo &eto join!"));
                countDown--;
            }
        }.runTaskTimer(pl, 0, 20);
    }

    private void startGame(List<Player> list) {
        if (list.size() < minPlayers){
            Bukkit.broadcastMessage(Utils.getFormattedText("&eThe sumo game did not start because not enough people have joined it."));
            countDownID.cancel();
            return;
        }
        countDownID.cancel();
        pl.gamesManager.getCurrentlyPlaying().addAll(list);
        temp = list;
        pl.getSumoPlayerData().getPlayerAvailableForFight().addAll(list);
        list.forEach(p -> {
            p.teleport(lobby);
            p.sendMessage(Utils.getFormattedText("&eThe sumo game will begin in 5 seconds."));
        });
        gameTask = new SumoGameTask(this, pl, list).runTaskTimer(pl, 0, 10);
    }
    public void freezePlayerSumo(Player p){
        new BukkitRunnable() {
            int count = 100;
            @Override
            public void run() {
                count--;
                if (count == 0)
                    cancel();
                if (count % 20 == 0)
                    p.sendMessage(Utils.getFormattedText("&eYour match will start in " + count/20));
                if (p.equals(currentP1) && p.getLocation() != p1) {
                    p.teleport(p1);
                }
                if (p.equals(currentP2) && p.getLocation() != p2) {
                    p.teleport(p2);
                }
            }
        }.runTaskTimer(pl, 0, 1);
    }
    public void playerLose(Player p, List<Player> list){
        pl.getSumoPlayerData().getSpectators().add(p);
        p.sendMessage(Utils.getFormattedText("&eYou've lost the sumo event, you can now spectate the rest of the game here or return to the hub."));
        p.teleport(spectators);
        pl.getSumoPlayerData().getCurrentlyFighting().clear();
        list.remove(p);
    }
    public void playerWin(Player p, List<Player> list){
        if (list.size() == 1){
            return;
        }
        pl.getSumoPlayerData().getCurrentlyFighting().clear();
        pl.getSumoPlayerData().getUnavailableForFight().add(p);
        p.sendMessage(Utils.getFormattedText("&eYou've won this round, fresh up for your next fight!"));
        p.teleport(lobby);

    }
    public void endGame(Player p){
        pl.getSumoPlayerData().getPlayerAvailableForFight().forEach(player -> {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "spawn " + player.getName());
            pl.getSumoPlayerData().getPlayerAvailableForFight().remove(player);
        });
        pl.getSumoPlayerData().getUnavailableForFight().forEach(player -> {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "spawn " + player.getName());
            pl.getSumoPlayerData().getUnavailableForFight().remove(player);
        });
        pl.getSumoPlayerData().getSpectators().forEach(player -> {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "spawn " + player.getName());
            pl.getSumoPlayerData().getSpectators().remove(player);
        });
        pl.gamesManager.getCurrentlyPlaying().removeAll(temp);
        try{
            rewards.forEach(string -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), string.replace("%player%", p.getName())));
        } catch (Exception e){
            e.printStackTrace();
        }
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "spawn " + p.getName());
    }
    public boolean getStatus(){
        return status;
    }
    public List<Player> getJoinedPlayers(){
        return joinedPlayers;
    }
    public boolean inGame(Player p){
        return joinedPlayers.contains(p);
    }
    public boolean isFighting(Player p){
        return pl.getSumoPlayerData().getCurrentlyFighting().contains(p);
    }
}
