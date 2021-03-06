package me.amitay.minigames.config;

import me.amitay.minigames.MiniGames;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class CustomConfig {

    private MiniGames plugin;
    private String name;
    public CustomConfig(MiniGames pl, String name){
        plugin = pl;
        this.name = name;
        setup();
        reloadCustomConfig();
    }

    // Files & File Configs Here
    private FileConfiguration playerscfg;
    private File playersfile;
    // --------------------------

    public void setup() {
        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdir();
        }

        playersfile = new File(plugin.getDataFolder(), name);

        if (!playersfile.exists()) {
            try {
                playersfile.createNewFile();
            } catch (IOException e) {
                Bukkit.getServer().getConsoleSender()
                        .sendMessage(ChatColor.RED + "Could not create the " + name +  " file");
            }
        }

        playerscfg = YamlConfiguration.loadConfiguration(playersfile);
    }

    public FileConfiguration getCustomConfig() {
        return playerscfg;
    }

    public void saveCustomConfig() {
        try {
            playerscfg.save(playersfile);

        } catch (IOException e) {
            Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED + "Could not save the " + name + " file");
        }
    }

    public void reloadCustomConfig() {
        playerscfg = YamlConfiguration.loadConfiguration(playersfile);
    }
}
