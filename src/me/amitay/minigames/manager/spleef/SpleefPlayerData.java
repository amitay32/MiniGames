package me.amitay.minigames.manager.spleef;

import me.amitay.minigames.MiniGames;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class SpleefPlayerData {
    private MiniGames pl;
    private List<Player> spectators = new CopyOnWriteArrayList<>();
    private List<Player> alive = new CopyOnWriteArrayList<>();

    public SpleefPlayerData(MiniGames pl) {
        this.pl = pl;
    }

    public List<Player> getSpectators() {
        return spectators;
    }

    public List<Player> getAlive() {
        return alive;
    }

}

