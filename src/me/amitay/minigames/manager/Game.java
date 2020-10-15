package me.amitay.minigames.manager;

import me.amitay.minigames.MiniGames;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public abstract class Game {
    protected boolean info, status, currentlyRunning;
    protected List<String> rewards;
    protected Random rand;
    protected MiniGames pl;
    protected int minPlayers, maxPlayers, timeToStart, countDown;
    protected BukkitTask countDownID, gameTask;
    protected Location spawn, spectators;
    protected List<Player> joinedPlayers = new ArrayList<>();
    protected List<Player> temp = new ArrayList<>();
    public Game(boolean info, boolean status, boolean currentlyRunning, String minPlayersPath, String maxPlayersPath, String timeToStartPath,
                String rewardsPath, String spawnPath, String spectatorsSpawnPath, MiniGames pl){
        this.pl = pl;
        this.info = info;
        this.status = status;
        this.currentlyRunning = currentlyRunning;
        rand = new Random();
        try {
            minPlayers = pl.getConfig().getInt(minPlayersPath);
            maxPlayers = pl.getConfig().getInt(maxPlayersPath);
            timeToStart = pl.getConfig().getInt(timeToStartPath);
            rewards = pl.getConfig().getStringList(rewardsPath);
            spawn = (Location) pl.getConfig().get(spawnPath);
            spectators = (Location) pl.getConfig().get(spectatorsSpawnPath);
        }catch (NullPointerException e){
            if (pl.getConfig().get("minigames.games.waterdrop.spawn") == null){
                return;
            }
            System.out.println("&cYou didn't set everything up in the config correctly.");
        }
    }
    public abstract void endGame(Player p);
    public abstract void startCountDown();
    public abstract void forceEndGame();
    public abstract void removePlayerFromGame(Player p);
    public abstract void startGame();
    public boolean getInfo(){
        return info;
    }
    public boolean getStatus(){
        return status;
    }
    public void addToList(Player p){
        joinedPlayers.add(p);
    }
    public List<Player> getJoinedPlayers(){
        return joinedPlayers;
    }
    public List<String> getRewards(){
        return rewards;
    }
    public boolean inGame(Player p){
        return joinedPlayers.contains(p);
    }
    public boolean enoughSpace() {
        return joinedPlayers.size() < maxPlayers;
    }
    public List<Player> getTemp() {
        return temp;
    }
}
