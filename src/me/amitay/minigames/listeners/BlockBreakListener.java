package me.amitay.minigames.listeners;

import me.amitay.minigames.MiniGames;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BlockBreakListener implements Listener {
    private MiniGames pl;

    public BlockBreakListener(MiniGames pl) {
        this.pl = pl;
    }

    @EventHandler
    public void blockBreakEvent(BlockBreakEvent e) {
        if (pl.gamesManager.getCurrentlyPlaying().contains(e.getPlayer())) {
            if (pl.getSpleefPlayerData().getAlive().contains(e.getPlayer())){
                if (pl.gamesManager.getSpleefGame().isCanBreakBlocks()){
                    if (e.getBlock().getType().equals(Material.SNOW_BLOCK))
                    return;
                }
            }
            e.setCancelled(true);
        }
    }
}

