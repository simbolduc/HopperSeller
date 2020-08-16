package ca.coulis.HopperSeller;

import ca.coulis.HopperSeller.adapter.LocationAdapter;
import ca.coulis.HopperSeller.commands.SellingCommand;
import ca.coulis.HopperSeller.listeners.ChunkEvent;
import ca.coulis.HopperSeller.listeners.PlayerOpenHopperEvent;
import ca.coulis.HopperSeller.storage.StorageManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Location;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;

public class HopperSeller extends JavaPlugin {

    private static HopperSeller instance;
    public static Gson GSON;
    public static String chatPrefix = "&6[HS] ";
    private Economy econ = null;

    static {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Location.class, new LocationAdapter());
        GSON = builder.create();
    }


    @Override
    public void onEnable() {
        instance = this;
        if (!setupEconomy() ) {
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        try {
            StorageManager.getInstance().load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.getCommand("hopperseller").setExecutor(new SellingCommand());
        this.getServer().getPluginManager().registerEvents(new PlayerOpenHopperEvent(), this);
        this.getServer().getPluginManager().registerEvents(new ChunkEvent(), this);
    }

    @Override
    public void onDisable() {
        try {
            StorageManager.getInstance().save();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) return false;
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) return false;
        econ = rsp.getProvider();
        return true;
    }

    public static HopperSeller getInstance(){
        return instance;
    }

    public Economy getEcon() {
        return this.econ;
    }

}
