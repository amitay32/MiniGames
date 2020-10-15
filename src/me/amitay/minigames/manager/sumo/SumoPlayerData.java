package me.amitay.minigames.manager.sumo;

import me.amitay.minigames.MiniGames;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class SumoPlayerData {
    private MiniGames pl;
    private List<Player> availableForFight = new CopyOnWriteArrayList<>();
    private List<Player> unAvailableForFight = new CopyOnWriteArrayList<>();
    private List<Player> spectators = new CopyOnWriteArrayList<>();
    private List<Player> currentlyFighting = new CopyOnWriteArrayList<>();

    public SumoPlayerData(MiniGames pl) {
        this.pl = pl;
    }

    public List<Player> getSpectators() {
        return spectators;
    }

    public List<Player> getPlayerAvailableForFight() {
        return availableForFight;
    }

    public List<Player> getUnavailableForFight() {
        return unAvailableForFight;
    }
    public List<Player> getCurrentlyFighting() {
        return currentlyFighting;
    }


}
