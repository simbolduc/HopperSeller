package ca.coulis.HopperSeller.data;

import org.bukkit.Material;

public class SellingItem {

    private final Material item;
    private final double price;

    public SellingItem(Material item, double price) {
        this.item = item;
        this.price = price;
    }

    public Material getItem() {
        return item;
    }

    public double getPrice(){
        return price;
    }


}
