package ca.coulis.HopperSeller.commands;

import ca.coulis.HopperSeller.HopperSeller;
import ca.coulis.HopperSeller.data.SellingItem;
import ca.coulis.HopperSeller.Utils;
import ca.coulis.HopperSeller.storage.StorageManager;
import com.github.stefvanschie.inventoryframework.Gui;
import com.github.stefvanschie.inventoryframework.GuiItem;
import com.github.stefvanschie.inventoryframework.pane.PaginatedPane;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class SellingCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(sender instanceof Player) {
            Player p = (Player) sender;

            if(!p.hasPermission("hopperseller.admin") && !p.isOp()) {
                p.sendMessage(Utils.toColor(HopperSeller.chatPrefix + "&cYou don't have permission to do that"));
                return true;
            }

            if(args.length == 0) {
                p.sendMessage(Utils.toColor("HopperSeller - Help"));
                return true;
            }

            if(args[0].equalsIgnoreCase("browse"))
                return this.browse(p);

            if(args[0].equalsIgnoreCase("add") && args[1] != null)
                return this.addSelling(p, args);

        }

        return true;
    }

    private boolean browse(Player p) {
        Gui gui = new Gui(HopperSeller.getInstance(), 6, Utils.toColor(HopperSeller.chatPrefix + "&eBrowser"));

        PaginatedPane pane = new PaginatedPane(0, 0, 9, 5);

        List<SellingItem> items = StorageManager.getInstance().getSellings();
        for(int i = 0; i <= (int)Math.ceil(items.size() / 45); i++) {
            StaticPane aPage = new StaticPane(0, 0, 9, 5);
            for(int j = (i * 45); j < Math.min((i * 45 + 45), items.size()); j++) {
                ItemStack item = new ItemStack(items.get(j).getItem());
                ItemMeta meta = item.getItemMeta();
                meta.setDisplayName(Utils.toColor("&e&lClick to remove from sellings"));
                List<String> lores = new ArrayList<>();
                lores.add(Utils.toColor("&aPrice : " + items.get(j).getPrice() + "$ each"));
                meta.setLore(lores);
                item.setItemMeta(meta);

                aPage.addItem(new GuiItem(item, event -> {
                    StorageManager.getInstance().removeSelling(item.getType());
                    event.getWhoClicked().sendMessage(Utils.toColor(HopperSeller.chatPrefix + "&aThe item has been removed"));
                    p.closeInventory();
                    event.setCancelled(true);
                }), j % 9, (int)Math.floor(j / 9) - i * 5);
            }

            pane.addPane(i, aPage);
        }

        gui.addPane(pane);

        // Page selection
        StaticPane back = new StaticPane(2, 5, 1, 1);
        StaticPane forward = new StaticPane(6, 5, 1, 1);

        ItemStack backArrow = new ItemStack(Material.ARROW);
        ItemMeta backMeta = backArrow.getItemMeta();
        backMeta.setDisplayName(Utils.toColor("&cPrevious page"));
        backArrow.setItemMeta(backMeta);

        back.addItem(new GuiItem(backArrow, event -> {
            pane.setPage(pane.getPage() - 1);

            if (pane.getPage() == 0)
                back.setVisible(false);

            forward.setVisible(true);
            gui.update();
        }), 0, 0);

        back.setVisible(false);

        ItemStack forwardArrow = new ItemStack(Material.ARROW);
        ItemMeta forwardMeta = forwardArrow.getItemMeta();
        forwardMeta.setDisplayName(Utils.toColor("&cNext page"));
        forwardArrow.setItemMeta(forwardMeta);

        forward.addItem(new GuiItem(forwardArrow, event -> {
            pane.setPage(pane.getPage() + 1);

            if (pane.getPage() == pane.getPages() - 1)
                forward.setVisible(false);

            back.setVisible(true);
            gui.update();
        }), 0, 0);

        if(items.size() <= 45)
            forward.setVisible(false);

        gui.addPane(back);
        gui.addPane(forward);

        gui.show(p);

        return true;
    }

    private boolean addSelling(Player p, String[] args) {
        Material material = p.getInventory().getItemInMainHand().getType();

        if(material.isAir()) {
            p.sendMessage(Utils.toColor(HopperSeller.chatPrefix + "&cYou cannot add air as a selling item"));
            return true;
        }

        try {
            double price = Double.parseDouble(args[1]);
            boolean res = StorageManager.getInstance().addSelling(material, price);
            if(res) p.sendMessage(Utils.toColor(HopperSeller.chatPrefix + "&aThe item has been added to the selling list"));
            else p.sendMessage(Utils.toColor(HopperSeller.chatPrefix + "&eThis item is already in the selling"));
        } catch (Exception e){
            e.printStackTrace();
            p.sendMessage(Utils.toColor(HopperSeller.chatPrefix + "&cThe price must be a number"));
            return true;
        }
        return true;
    }

}
