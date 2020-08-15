package ca.coulis.HopperSeller;

import ca.coulis.HopperSeller.commands.AddSellingCommand;
import ca.coulis.HopperSeller.listeners.PlayerOpenHopperEvent;
import com.google.gson.Gson;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;

public class HopperSeller extends JavaPlugin {

    private static HopperSeller instance;
    public static Gson GSON = new Gson();

    @Override
    public void onEnable() {
        instance = this;

        try {
            StorageManager.getInstance().load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.getCommand("hopperseller").setExecutor(new AddSellingCommand());
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
