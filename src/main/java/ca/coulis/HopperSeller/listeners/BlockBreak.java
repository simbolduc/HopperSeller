package ca.coulis.HopperSeller.listeners;

import ca.coulis.HopperSeller.data.HopperData;
import ca.coulis.HopperSeller.storage.StorageManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BlockBreak implements Listener {

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        HopperData hopper = StorageManager.getInstance().getHopperByLocation(event.getBlock().getLocation());
        if(hopper != null) StorageManager.getInstance().removeHopper(hopper);
    }

}
