package me.amitay.mini_games;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import me.amitay.mini_games.commands.HostCommands;
import me.amitay.mini_games.commands.PlayCommand;
import me.amitay.mini_games.commands.games_commands.LmsCommands;
import me.amitay.mini_games.commands.games_commands.RedroverCommands;
import me.amitay.mini_games.commands.games_commands.SpleefCommands;
import me.amitay.mini_games.commands.games_commands.SumoCommands;
import me.amitay.mini_games.config.ConfigManager;
import me.amitay.mini_games.listeners.BlockBreakListener;
import me.amitay.mini_games.listeners.PlayerDamagePlayerListener;
import me.amitay.mini_games.listeners.PlayerDieListener;
import me.amitay.mini_games.listeners.QuitListener;
import me.amitay.mini_games.manager.GamesManager;
import me.amitay.mini_games.manager.SchematicsManager;
import me.amitay.mini_games.manager.lms.LmsPlayerData;
import me.amitay.mini_games.manager.redrover.RedroverPlayerData;
import me.amitay.mini_games.manager.spleef.SpleefPlayerData;
import me.amitay.mini_games.manager.sumo.SumoPlayerData;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;


public class MiniGames extends JavaPlugin {

    public GamesManager gamesManager;
    private SumoPlayerData sumoPlayerData;
    private RedroverPlayerData redroverPlayerData;
    private SpleefPlayerData spleefPlayerData;
    private LmsPlayerData lmsPlayerData;
    private WorldGuardPlugin worldGuardPlugin;
    private ConfigManager configManager;
    private SchematicsManager schematicsManager;
    public void onEnable() {
        loadUtils();
    }

    public void loadCommands() {
        getCommand("sumo").setExecutor(new SumoCommands(this));
        getCommand("redrover").setExecutor(new RedroverCommands(this));
        getCommand("spleef").setExecutor(new SpleefCommands(this));
        getCommand("lms").setExecutor(new LmsCommands(this));
        getCommand("play").setExecutor(new PlayCommand(this));
        getCommand("hostgame").setExecutor(new HostCommands(this));
    }
    private void loadManagers(){
        configManager = new ConfigManager(this);
        schematicsManager = new SchematicsManager(this);
        gamesManager = new GamesManager(this);
        sumoPlayerData = new SumoPlayerData(this);
        redroverPlayerData = new RedroverPlayerData(this);
        spleefPlayerData = new SpleefPlayerData(this);
        lmsPlayerData = new LmsPlayerData(this);
    }
    private void loadEvents() {
        getServer().getPluginManager().registerEvents(new QuitListener(this), this);
        getServer().getPluginManager().registerEvents(new BlockBreakListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerDamagePlayerListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerDieListener(this), this);
        //getServer().getPluginManager().registerEvents(new PlayerInteractListener(this), this);
        //getServer().getPluginManager().registerEvents(new CommandListener(this), this);
    }
    public void loadUtils() {
        saveDefaultConfig();
        loadManagers();
        loadCommands();
        loadEvents();
        worldGuardPlugin = getWorldGuard();
        //prefix = getConfig().getString(ChatColor.translateAlternateColorCodes('&', "Prefix"));
        //loadItemsClass.loadItems();
        // = new MySql(this);
    }
    public SumoPlayerData getSumoPlayerData() {
        return sumoPlayerData;
    }
    public RedroverPlayerData getRedroverPlayerData() {
        return redroverPlayerData;
    }

    public WorldGuardPlugin getWorldGuard() {
        Plugin plugin = this.getServer().getPluginManager().getPlugin("WorldGuard");

        if (plugin == null || !(plugin instanceof WorldGuardPlugin)) {
            return null;
        }

        return (WorldGuardPlugin) plugin;
    }
    public ConfigManager getCustomConfigs(){
        return configManager;
    }
    public SchematicsManager getSchematicsManager(){
        return schematicsManager;
    }

    public SpleefPlayerData getSpleefPlayerData() {
        return spleefPlayerData;
    }
    public LmsPlayerData getLmsPlayerData(){
        return lmsPlayerData;
    }
}
