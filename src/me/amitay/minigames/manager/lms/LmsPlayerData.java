package me.amitay.minigames.manager.lms;

import me.amitay.minigames.MiniGames;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class LmsPlayerData {
    private MiniGames pl;
    private List<Player> spectators = new CopyOnWriteArrayList<>();
    private List<Player> alive = new CopyOnWriteArrayList<>();

    public LmsPlayerData(MiniGames pl) {
        this.pl = pl;
    }

    public List<Player> getSpectators() {
        return spectators;
    }

    public List<Player> getAlive() {
        return alive;
    }
}
