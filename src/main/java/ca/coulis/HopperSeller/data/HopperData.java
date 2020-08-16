package ca.coulis.HopperSeller.data;

import org.bukkit.Location;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;

public class HopperData {

    private final Location blockLocation;
    private final List<Material> sellings;
    private double bank;

    public HopperData(Location location) {
        this.blockLocation = location;
        this.sellings = new ArrayList<>();
        this.bank = 0;
    }

    public Location getLocation() {
        return this.blockLocation;
    }

    public List<Material> getSellings() {
        return this.sellings;
    }

    public void toggleMaterial(Material material) {
        Material res = null;
        for(Material selling : this.sellings)
            if(selling == material) res = selling;
        if(res == null) this.sellings.add(material);
        else this.sellings.remove(res);
    }

    public boolean isEnabled(Material material) {
        for (Material selling : this.sellings)
            if (selling == material)
                return true;
        return false;
    }

    public double getBank() {
        return this.bank;
    }

    public void setBank(double value) {
        this.bank = value;
    }

}
