package ca.coulis.HopperSeller.storage;

import ca.coulis.HopperSeller.HopperSeller;
import ca.coulis.HopperSeller.data.HopperData;
import ca.coulis.HopperSeller.data.SellingItem;
import com.google.gson.reflect.TypeToken;
import org.bukkit.Location;
import org.bukkit.Material;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class StorageManager {

    private final String[] configFiles = {"sellings", "hoppers"};
    private static StorageManager storageManager = null;
    private final File dataFolder;
    private final Map<String, File> configs = new HashMap<>();

    private List<SellingItem> sellings = null;
    private List<HopperData> hoppers = null;

    public StorageManager() {
        dataFolder = HopperSeller.getInstance().getDataFolder();
    }

    public static StorageManager getInstance() {
        if(storageManager == null)
            storageManager = new StorageManager();
        return storageManager;
    }

    public void load() throws IOException {
        if(!dataFolder.exists())
            dataFolder.mkdir();

        for (String configFile : configFiles) {
            configs.put(configFile, new File(dataFolder, configFile + ".json"));
            if(!configs.get(configFile).exists()) {
                configs.get(configFile).createNewFile();
                FileWriter writer = new FileWriter(configs.get(configFile), false);
                writer.write("[]");
                writer.close();
            }
            FileReader reader = new FileReader(configs.get(configFile));
            if(configFile.equals("sellings"))
                sellings = HopperSeller.GSON.fromJson(reader, new TypeToken<List<SellingItem>>(){}.getType());
            if(configFile.equals("hoppers"))
                hoppers = HopperSeller.GSON.fromJson(reader, new TypeToken<List<HopperData>>(){}.getType());

        }
    }

    public void save() throws IOException {
        if(!dataFolder.exists())
            dataFolder.mkdir();

        for (String configFile : configFiles) {
            if(!configs.get(configFile).exists())
                configs.get(configFile).createNewFile();
            FileWriter writer = new FileWriter(configs.get(configFile), false);
            if(configFile.equals("sellings"))
                writer.write(HopperSeller.GSON.toJson(sellings));
            if(configFile.equals("hoppers"))
                writer.write(HopperSeller.GSON.toJson(hoppers));
            writer.close();
        }
    }

    public HopperData getHopperByLocation(Location location) {
        for(int i = 0; i < this.hoppers.size(); i++) {
            if (this.hoppers.get(i).getLocation().getX() == location.getX() &&
                this.hoppers.get(i).getLocation().getY() == location.getY() &&
                this.hoppers.get(i).getLocation().getZ() == location.getZ() &&
                this.hoppers.get(i).getLocation().getWorld().getName().equals(location.getWorld().getName())
            )
                return this.hoppers.get(i);
        }
        return null;
    }

    public void removeHopper(HopperData hopper) {
        this.hoppers.remove(hopper);
    }

    public void updateHopper(HopperData hopper) {
        HopperData data = this.getHopperByLocation(hopper.getLocation());
        if(data != null) this.hoppers.remove(data);
        this.hoppers.add(hopper);
    }

    public List<HopperData> getHoppers() {
        return this.hoppers;
    }

    public List<SellingItem> getSellings() {
        return this.sellings;
    }

    public void removeSelling(Material material) {
        if(this.sellings != null)
            this.sellings.removeIf(item -> item.getItem() == material);
    }

    public boolean addSelling(Material material, double price) {
        if(this.sellings != null) {
            for (SellingItem selling : this.sellings)
                if (selling.getItem() == material)
                    return false;
            this.sellings.add(new SellingItem(material, price));
            return true;
        }
        return false;
    }

    public boolean canSell(Material material) {
        for(SellingItem item : this.sellings)
            if(item.getItem() == material)
                return true;
        return false;
    }

    public double getSellPrice(Material material) {
        for(SellingItem item : this.sellings)
            if(item.getItem() == material)
                return item.getPrice();
        return 0;
    }

}
