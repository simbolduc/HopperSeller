package ca.coulis.HopperSeller.commands;

import ca.coulis.HopperSeller.HopperSeller;
import ca.coulis.HopperSeller.SellingItem;
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

import java.util.List;

public class AddSellingCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(sender instanceof Player) {
            Player p = (Player) sender;

            if(!p.hasPermission("hopperseller.admin") && !p.isOp()) {
                p.sendMessage("You don't have permission to do that");
                return true;
            }

            if(args.length == 0) {
                p.sendMessage("HopperSeller - Help");
                // TODO: Finish the help menu
                return true;
            }

            if(args[0].equalsIgnoreCase("browse"))
                return this.Browse(p, args);

            if(args[0].equalsIgnoreCase("add") && args[1] != null)
                return this.AddSelling(p, args);

        }

        return true;
    }

    private boolean Browse(Player p, String[] args) {
        Gui gui = new Gui(HopperSeller.getInstance(), 6, "Browser");

        PaginatedPane pane = new PaginatedPane(0, 0, 9, 5);

        List<SellingItem> items = StorageManager.getInstance().getSellings();
        for(int i = 0; i <= (int)Math.ceil(items.size() / 45); i++) {
            StaticPane aPage = new StaticPane(0, 0, 9, 5);
            for(int j = (i * 45); j < Math.min((i * 45 + 45), items.size()); j++) {

                aPage.addItem(new GuiItem(new ItemStack(items.get(j).getItem()), event -> {
                    event.getWhoClicked().sendMessage("Item has been removed");
                    event.setCancelled(true);
                }), j % 9, (int)Math.floor(j / 9) - i * 5);
            }

            pane.addPane(i, aPage);
        }

        gui.addPane(pane);

        // Page selection
        StaticPane back = new StaticPane(2, 5, 1, 1);
        StaticPane forward = new StaticPane(6, 5, 1, 1);

        back.addItem(new GuiItem(new ItemStack(Material.ARROW), event -> {
            pane.setPage(pane.getPage() - 1);

            if (pane.getPage() == 0) {
                back.setVisible(false);
            }

            forward.setVisible(true);
            gui.update();
        }), 0, 0);

        back.setVisible(false);

        forward.addItem(new GuiItem(new ItemStack(Material.ARROW), event -> {
            pane.setPage(pane.getPage() + 1);

            if (pane.getPage() == pane.getPages() - 1) {
                forward.setVisible(false);
            }

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

    private boolean AddSelling(Player p, String[] args) {
        Material material = p.getInventory().getItemInMainHand().getType();

        if(material.isAir()) {
            p.sendMessage("You cannot add air as a selling item");
            return true;
        }

        try {
            double price = Double.parseDouble(args[1]);
            p.sendMessage("Price : " + price);
            boolean res = StorageManager.getInstance().addSelling(material, price);
            if(res) p.sendMessage("The item has been added to the selling list");
            else p.sendMessage("This item is already in sellable");
        } catch (Exception e){
            e.printStackTrace();
            p.sendMessage("The price must be a number");
            return true;
        }
        return true;
    }

}
