package me.amitay.minigames.config;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import me.amitay.minigames.MiniGames;

public class ConfigManager {

    private MiniGames plugin;
    private BiMap<String, CustomConfig> configMap = HashBiMap.create();

    public ConfigManager(MiniGames pl){
        plugin = pl;
        configMap.put("schem", new CustomConfig(pl, "schematic.yml"));
        addDefaultsSchematics();
    }
    public CustomConfig getSchemConfig(){
        return configMap.get("schem");
    }
    private void addDefaultsSchematics(){
        CustomConfig schemConfig = getSchemConfig();
        schemConfig.getCustomConfig().options().copyDefaults(true);
        schemConfig.getCustomConfig().addDefault("spleef.schematic_location.world", "");
        schemConfig.getCustomConfig().addDefault("spleef.schematic_location.x", "");
        schemConfig.getCustomConfig().addDefault("spleef.schematic_location.y", "");
        schemConfig.getCustomConfig().addDefault("spleef.schematic_location.z", "");
        schemConfig.getCustomConfig().options().copyDefaults(true);
        schemConfig.saveCustomConfig();
    }
}

