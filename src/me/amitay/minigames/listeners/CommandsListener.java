package me.amitay.minigames.listeners;

import me.amitay.minigames.MiniGames;
import me.amitay.minigames.utils.Utils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class CommandsListener implements Listener {
    private MiniGames pl;

    public CommandsListener(MiniGames pl) {
        this.pl = pl;
    }

    @EventHandler
    public void playerExecuteCommandEvent(PlayerCommandPreprocessEvent e) {
        if (pl.gamesManager.getCurrentlyPlaying().contains(e.getPlayer())) {
            if (!pl.gamesManager.getAvailableCommands().contains(e.getMessage())){
                e.getPlayer().sendMessage(Utils.getFormattedText("&cYou can not use this command inside a minigame."));
                e.setCancelled(true);
            }
        }
    }
}
