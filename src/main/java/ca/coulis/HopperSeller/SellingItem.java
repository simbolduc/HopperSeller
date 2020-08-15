package ca.coulis.HopperSeller;

import org.bukkit.Material;

public class SellingItem {

    private Material item;
    private double price;

    public SellingItem(Material item, double price) {
        this.item = item;
        this.price = price;
    }

    public Material getItem() {
        return item;
    }

    public void setItem(Material item){
        this.item = item;
    }

    public double getPrice(){
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

}
