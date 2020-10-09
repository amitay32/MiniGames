package me.amitay.mini_games.manager;

import me.amitay.mini_games.MiniGames;
import me.amitay.mini_games.manager.redrover.Redrover;
import me.amitay.mini_games.manager.spleef.Spleef;
import me.amitay.mini_games.manager.sumo.Sumo;
import me.amitay.mini_games.utils.Gamemode;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GamesManager {
    private MiniGames pl;
    private Map<Gamemode, Object> gamemodes = new HashMap<>();
    private List<Player> currentlyPlaying = new ArrayList<>();

    public GamesManager(MiniGames pl) {
        this.pl = pl;
        Sumo sumo = new Sumo(pl);
        Redrover redrover = new Redrover(pl);
        Spleef spleef = new Spleef(pl);
        if (sumo.getinfo())
            gamemodes.put(Gamemode.SUMO, new Sumo(pl));
        if (redrover.isInfo())
            gamemodes.put(Gamemode.REDROVER, new Redrover(pl));
        if (spleef.isInfo())
            gamemodes.put(Gamemode.SPLEEF, new Spleef(pl));
    }

    public void startGame(Gamemode game) {
        if (gamemodes.containsKey(game)) {
            if (game == Gamemode.SUMO) {
                Sumo sumo = (Sumo) gamemodes.get(game);
                sumo.startCountDown();
            }
            if (game == Gamemode.REDROVER) {
                Redrover redrover = (Redrover) gamemodes.get(game);
                redrover.startCountDown();
            }
            if (game == Gamemode.SPLEEF) {
                Spleef spleef = (Spleef) gamemodes.get(game);
                spleef.startCountDown();
            }
        }
    }

    public Sumo getSumoGame() {
        return (Sumo) gamemodes.get(Gamemode.SUMO);
    }

    public Map<Gamemode, Object> getGamemodes() {
        return gamemodes;
    }

    public List<Player> getCurrentlyPlaying() {
        return currentlyPlaying;
    }

    public Redrover getRedroverGame() {
        return (Redrover) gamemodes.get(Gamemode.REDROVER);
    }

    public Spleef getSpleefGame() {
        return (Spleef) gamemodes.get(Gamemode.SPLEEF);
    }
}
