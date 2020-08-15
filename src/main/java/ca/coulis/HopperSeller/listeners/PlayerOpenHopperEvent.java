package ca.coulis.HopperSeller.listeners;

import org.bukkit.block.Hopper;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerOpenHopperEvent implements Listener {

    @EventHandler(priority = EventPriority.LOW)
    public void onOpen(final PlayerInteractEvent event) {
        Player p = event.getPlayer();

        if(!p.isSneaking())
            return;

        if(event.getAction() != Action.RIGHT_CLICK_BLOCK)
            return;

        if(event.getClickedBlock() == null)
            return;

        if(!(event.getClickedBlock().getState() instanceof Hopper))
            return;


        event.setCancelled(true);
    }

}
