package ca.coulis.HopperSeller.listeners;

import ca.coulis.HopperSeller.ChunkManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;

public class ChunkEvent implements Listener {

    @EventHandler
    public void onChunkLoad(ChunkLoadEvent event) {
        ChunkManager.getInstance().addChunk(event.getChunk());
    }

    @EventHandler
    public void onChunkUnload(ChunkUnloadEvent event) {
        ChunkManager.getInstance().removeChunk(event.getChunk());
    }

}
