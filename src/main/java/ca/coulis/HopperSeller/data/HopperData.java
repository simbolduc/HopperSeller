package ca.coulis.HopperSeller.data;

import ca.coulis.HopperSeller.storage.StorageManager;
import org.bukkit.Location;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class HopperData {

    private Location blockLocation;
    private List<SellingItem> sellings;

    public HopperData(Location location) {
        this.blockLocation = location;
        this.sellings = new ArrayList<>();
    }

    public Location getLocation() {
        return this.blockLocation;
    }

    public List<SellingItem> getSellings() {
        return this.sellings;
    }

    public void toggleMaterial(Material material) {
        Optional<SellingItem> item = StorageManager.getInstance().getSellings().stream().filter(x -> x.getItem() == material).findFirst();
        if(item.isPresent()) {
            if(this.sellings.contains(item.get()))
                this.sellings.remove(item.get());
            else this.sellings.add(item.get());
        }
    }

    public void setBlockLocation(Location location) {
        this.blockLocation = location;
    }

    public void setSellings(List<SellingItem> sellings) {
        this.sellings = sellings;
    }

}
