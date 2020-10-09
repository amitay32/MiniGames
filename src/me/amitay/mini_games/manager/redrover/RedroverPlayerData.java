package me.amitay.mini_games.manager.redrover;

import me.amitay.mini_games.MiniGames;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class RedroverPlayerData {
    private MiniGames pl;
    private List<Player> spectators = new CopyOnWriteArrayList<>();
    private List<Player> alive = new CopyOnWriteArrayList<>();
    private Player killer;

    public RedroverPlayerData(MiniGames pl) {
        this.pl = pl;
    }

    public List<Player> getSpectators() {
        return spectators;
    }

    public List<Player> getAlive() {
        return alive;
    }
    public Player getKiller(){
        return killer;
    }
    public void setKiller(Player killer) {
        this.killer = killer;
    }
}
