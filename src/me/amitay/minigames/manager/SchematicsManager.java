package me.amitay.minigames.manager;

import com.sk89q.worldedit.CuboidClipboard;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.schematic.MCEditSchematicFormat;
import me.amitay.minigames.MiniGames;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.io.File;

public class SchematicsManager {
    private MiniGames pl;
    public SchematicsManager(MiniGames pl){
        this.pl = pl;
    }

    public void loadSchematic(Location location) {
        WorldEditPlugin worldEditPlugin = (WorldEditPlugin) Bukkit.getPluginManager().getPlugin("WorldEdit");
        File schematic = new File(worldEditPlugin.getDataFolder() + File.separator + "/schematics/spleef.schematic");
        EditSession session = worldEditPlugin.getWorldEdit().getEditSessionFactory().getEditSession(new BukkitWorld(location.getWorld()), 10000);
        try {
            CuboidClipboard clipboard = MCEditSchematicFormat.getFormat(schematic).load(schematic);
            clipboard.rotate2D(90);
            clipboard.paste(session, new Vector(location.getX(), location.getY(), location.getZ()), false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
