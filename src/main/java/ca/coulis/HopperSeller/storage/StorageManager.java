package ca.coulis.HopperSeller.storage;

import ca.coulis.HopperSeller.HopperSeller;
import ca.coulis.HopperSeller.SellingItem;
import com.google.gson.reflect.TypeToken;
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

            // TODO: Add hoppers
//            if(configFile.equals("hoppers"))

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
            // TODO: Add hoppers
//            if(configFile.equals("hoppers"))
//                writer.write(HopperSeller.GSON.toJson(sellings));
            writer.close();
        }
    }

    public List<SellingItem> getSellings() {
        return sellings;
    }

    public void setSellings(List<SellingItem> sellings) {
        this.sellings = sellings;
    }

    public boolean addSelling(Material material, double price) {
        if(this.sellings != null) {
            for (SellingItem selling : this.sellings)
                if (selling.getItem() == material)
                    return false;
            this.sellings.add(new SellingItem(material, price));
            return true;
        }
        System.out.println("SELLINGS ARE NULL");
        return false;
    }

}
