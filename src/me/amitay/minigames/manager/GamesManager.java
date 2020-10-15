package me.amitay.minigames.manager;

import me.amitay.minigames.MiniGames;
import me.amitay.minigames.manager.lms.Lms;
import me.amitay.minigames.manager.redrover.Redrover;
import me.amitay.minigames.manager.spleef.Spleef;
import me.amitay.minigames.manager.sumo.Sumo;
import me.amitay.minigames.manager.waterdrop.WaterDrop;
import me.amitay.minigames.utils.GameMode;
import me.amitay.minigames.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GamesManager {
    private MiniGames pl;
    private Location mainSpawn;
    private List<String> availableCommands = new ArrayList<>();
    private Map<GameMode, Game> gamemodes = new HashMap<>();
    private List<Player> currentlyPlaying = new ArrayList<>();
    private List<String> available = new ArrayList<>();
    private List<String> unavailable = new ArrayList<>();
    private Sumo sumo;
    private Redrover redrover;
    private Spleef spleef;
    private Lms lms;
    private WaterDrop waterDrop;
    private Game playedGame;

    public GamesManager(MiniGames pl) {
        this.pl = pl;
        sumo = new Sumo(pl);
        redrover = new Redrover(pl);
        spleef = new Spleef(pl);
        lms = new Lms(pl);
        waterDrop = new WaterDrop(pl);
        availableCommands = pl.getConfig().getStringList("minigames.available_commands_while_in_game");
        mainSpawn = (Location) pl.getConfig().get("minigames.main_spawn_location");
        if (mainSpawn == null) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "The plugin was not enabled because you forgot to set up the main spawn. /minigames setmainspawn to fix the issue");
            return;
        }
        if (sumo.getInfo())
            gamemodes.put(GameMode.SUMO, sumo);
        if (redrover.getInfo())
            gamemodes.put(GameMode.REDROVER, redrover);
        if (spleef.getInfo())
            gamemodes.put(GameMode.SPLEEF, spleef);
        if (lms.getInfo())
            gamemodes.put(GameMode.LMS, lms);
        if (waterDrop.getInfo()) {
            gamemodes.put(GameMode.WATERDROP, waterDrop);
        }
        for (GameMode gamemode : GameMode.values()) {
            if (getGamemodes().get(gamemode) != null) {
                available.add(gamemode.name().toLowerCase());
                continue;
            }
            unavailable.add(gamemode.name());
        }
    }

    public void startGame(GameMode gamemode, Player p) {
        if (gamemodes.containsKey(gamemode)) {
            Game game = gamemodes.get(gamemode);
            game.startCountDown();
            p.sendMessage(Utils.getFormattedText("&eYou have successfully started a " + gamemode.toString().toLowerCase() + " game!"));
        }
    }

    public void endGame(GameMode gamemode, Player p) {
        if (gamemodes.containsKey(gamemode)) {
            Game game = gamemodes.get(gamemode);
            if (game.currentlyRunning)
                game.forceEndGame();
            p.sendMessage(Utils.getFormattedText("&eThat game is not currently running"));
        } else {
            p.sendMessage(Utils.getFormattedText("&eThat game does not exist, or you didn't set it up yet."));
        }
    }

    public void joinGame(GameMode gamemode, Player p) {
        if (gamemodes.containsKey(gamemode)) {
            Game game = gamemodes.get(gamemode);
            if (!game.getStatus()) {
                p.sendMessage(Utils.getFormattedText("&eThis minigame has never started or it is already running"));
                return;
            }
            if (game.enoughSpace()) {
                game.addToList(p);
                p.sendMessage(Utils.getFormattedText("&eYou were successfully added to the " + gamemode.toString().toLowerCase() + " good luck!"));
                return;
            }
            p.sendMessage(Utils.getFormattedText("&eThis game is already full."));
        }
    }
    public void playerQuitMiniGame(Game game, Player p){
        game.removePlayerFromGame(p);
    }

    public Sumo getSumoGame() {
        return sumo;
    }
    public Game getCurrentlyPlayedGamemode(Player p){
        gamemodes.forEach((game1,v) -> {
            if (v.temp.contains(p)){
                playedGame = v;
            }
        });
        return playedGame;
    }

    public Map<GameMode, Game> getGamemodes() {
        return gamemodes;
    }

    public List<Player> getCurrentlyPlaying() {
        return currentlyPlaying;
    }

    public Redrover getRedroverGame() {
        return redrover;
    }

    public Spleef getSpleefGame() {
        return spleef;
    }

    public Lms getLmsGame() {

        return lms;
    }

    public List<String> getAvailableCommands() {
        return availableCommands;
    }

    public Location getMainSpawn() {
        return mainSpawn;
    }

    public WaterDrop getWaterDropGame() {
        return waterDrop;
    }

    public List<String> getAvailable() {
        return available;
    }
    public List<String> getUnavailable() {
        return unavailable;
    }
}
