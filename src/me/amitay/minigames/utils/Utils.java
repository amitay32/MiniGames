package me.amitay.minigames.utils;

import com.sk89q.worldedit.Vector;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import me.amitay.minigames.MiniGames;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class Utils {
    private static MiniGames pl = MiniGames.getPlugin(MiniGames.class);

    public static String getFormattedText(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }
    public static Boolean inventoryEmpty(Player p){
        for(ItemStack item : p.getInventory().getContents())
        {
            if(item != null && item != new ItemStack(Material.AIR))
                return false;
        }
        return true;
    }
    public static boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return false;
        } catch (NullPointerException e) {
            return false;
        }
        return true;
    }
    public static void freezePlayer(Location loc, Player p, int seconds){
        new BukkitRunnable() {
            int count = seconds * 20;
            @Override
            public void run() {
                count--;
                if (count == 0)
                    cancel();
                if (count % 20 == 0)
                    p.sendMessage(Utils.getFormattedText("&eYou will be able to move in " + count/20));
                if (!p.getLocation().equals(loc)){
                    p.teleport(loc);
                }
            }
        }.runTaskTimer(pl, 0, 1);
    }
    public static boolean isInRegion(Player player, String region2) {
        LocalPlayer localPlayer = pl.getWorldGuard().wrapPlayer(player);
        Vector playerVector = localPlayer.getPosition();
        RegionManager regionManager = pl.getWorldGuard().getRegionManager(player.getWorld());
        ApplicableRegionSet applicableRegionSet = regionManager.getApplicableRegions(playerVector);

        for (ProtectedRegion region : applicableRegionSet) {
            if (region == null) {
                break;
            }
            if (region2.equalsIgnoreCase(region.getId())) {
                if (region.contains(playerVector)) {
                    try {
                        return true;

                    } catch (Exception e) {
                        player.sendMessage(ChatColor.RED
                                + "Hey! An error occurred, please dm one of the owners incase you see this message");
                    }
                }

            }
        }
        return false;
    }

}
