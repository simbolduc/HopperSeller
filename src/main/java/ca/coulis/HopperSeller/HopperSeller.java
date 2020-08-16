package ca.coulis.HopperSeller;

import ca.coulis.HopperSeller.adapter.LocationAdapter;
import ca.coulis.HopperSeller.commands.SellingCommand;
import ca.coulis.HopperSeller.listeners.PlayerOpenHopperEvent;
import ca.coulis.HopperSeller.storage.StorageManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;

public class HopperSeller extends JavaPlugin {

    private static HopperSeller instance;
    public static Gson GSON;
    public static String chatPrefix = "&6[HS] ";

    static {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Location.class, new LocationAdapter());
        GSON = builder.create();
    }


    @Override
    public void onEnable() {
        instance = this;

        try {
            StorageManager.getInstance().load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.getCommand("hopperseller").setExecutor(new SellingCommand());
        this.getServer().getPluginManager().registerEvents(new PlayerOpenHopperEvent(), this);
    }

    @Override
    public void onDisable() {
        try {
            StorageManager.getInstance().save();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static HopperSeller getInstance(){
        return instance;
    }

}
