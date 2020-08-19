package ca.coulis.HopperSeller;

import ca.coulis.HopperSeller.data.HopperData;
import ca.coulis.HopperSeller.storage.StorageManager;
import javafx.scene.transform.MatrixType;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Container;
import org.bukkit.block.Hopper;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class ChunkManager {

    private static ChunkManager instance;
    private final List<Chunk> chunks;

    public ChunkManager() {
        instance = this;
        this.chunks = new ArrayList<>();
    }

    public static ChunkManager getInstance() {
        if(instance == null)
            instance = new ChunkManager();
        return instance;
    }

    public void addChunk(Chunk chunk) {
        this.chunks.add(chunk);
    }

    public void removeChunk(Chunk chunk) {
        this.chunks.remove(chunk);
    }

    public List<Chunk> getChunks() {
        return this.chunks;
    }

    public void automaticSell(HopperSeller plugin) {
        new BukkitRunnable() {
            @Override
            public void run() {
                List<HopperData> hoppers = StorageManager.getInstance().getHoppers();
                for(int i = 0; i < hoppers.size(); i++) {
                    if(ChunkManager.getInstance().getChunks().contains(hoppers.get(i).getLocation().getChunk())) {
                        BlockState block = hoppers.get(i).getLocation().getBlock().getState();
                        if(block instanceof Hopper) {
                            Hopper hopperBlock = (Hopper) block;
                            ItemStack[] content = hopperBlock.getInventory().getContents();
                            for (int j = 0; j < content.length; j++) {
                                if(content[j] != null) {
                                    Material type = content[j].getType();
                                    if (StorageManager.getInstance().canSell(type) && hoppers.get(i).getSellings().contains(type)) {
                                        hoppers.get(i).setBank(hoppers.get(i).getBank() + content[j].getAmount() * StorageManager.getInstance().getSellPrice(type));

                                        content[j] = new ItemStack(Material.AIR);
                                        hopperBlock.getSnapshotInventory().setContents(content);

                                        hopperBlock.update();
                                        block.update();
                                    }
                                }
                            }
                        } else StorageManager.getInstance().removeHopper(hoppers.get(i)); // Hopper has been removed (location is not an hopper)
                    }
                }

                automaticSell(plugin);
            }
        }.runTaskLater(plugin, 20 * 10); // 10s (change to 5 mins)
    }

}
