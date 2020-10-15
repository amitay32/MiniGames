package me.amitay.minigames.manager.waterdrop;

import me.amitay.minigames.MiniGames;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class WaterdropPlayerData {
    private MiniGames pl;
    private Map<Player, Integer> playerMap = new HashMap<>();

    public WaterdropPlayerData(MiniGames pl) {
        this.pl = pl;
    }

    public Map<Player, Integer> getPlayerMap() {
        return playerMap;
    }
}
