package me.amitay.minigames;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import me.amitay.minigames.commands.HostCommands;
import me.amitay.minigames.commands.MiniGameCommands;
import me.amitay.minigames.commands.PlayCommand;
import me.amitay.minigames.commands.gamescommands.*;
import me.amitay.minigames.config.ConfigManager;
import me.amitay.minigames.listeners.*;
import me.amitay.minigames.manager.GamesManager;
import me.amitay.minigames.manager.SchematicsManager;
import me.amitay.minigames.manager.lms.LmsPlayerData;
import me.amitay.minigames.manager.redrover.RedroverPlayerData;
import me.amitay.minigames.manager.spleef.SpleefPlayerData;
import me.amitay.minigames.manager.sumo.SumoPlayerData;
import me.amitay.minigames.manager.waterdrop.WaterdropPlayerData;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;


public class MiniGames extends JavaPlugin {

    public GamesManager gamesManager;
    private SumoPlayerData sumoPlayerData;
    private RedroverPlayerData redroverPlayerData;
    private SpleefPlayerData spleefPlayerData;
    private LmsPlayerData lmsPlayerData;
    private WaterdropPlayerData waterdropPlayerData;
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
        getCommand("waterdrop").setExecutor(new WaterDropCommands(this));
        getCommand("play").setExecutor(new PlayCommand(this));
        getCommand("hostgame").setExecutor(new HostCommands(this));
        getCommand("minigames").setExecutor(new MiniGameCommands(this));
    }
    private void loadManagers(){
        configManager = new ConfigManager(this);
        schematicsManager = new SchematicsManager(this);
        gamesManager = new GamesManager(this);
        sumoPlayerData = new SumoPlayerData(this);
        redroverPlayerData = new RedroverPlayerData(this);
        spleefPlayerData = new SpleefPlayerData(this);
        lmsPlayerData = new LmsPlayerData(this);
        waterdropPlayerData = new WaterdropPlayerData(this);
    }
    private void loadEvents() {
        getServer().getPluginManager().registerEvents(new QuitListener(this), this);
        getServer().getPluginManager().registerEvents(new BlockBreakListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerDamagePlayerListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerDieListener(this), this);
        getServer().getPluginManager().registerEvents(new CommandsListener(this), this);
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
    public WaterdropPlayerData getWaterdropPlayerData(){
        return waterdropPlayerData;
    }
}
