package ca.coulis.HopperSeller.commands;

import ca.coulis.HopperSeller.HopperSeller;
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
        Gui gui = new Gui(HopperSeller.getInstance(), 6, "");

        PaginatedPane pane = new PaginatedPane(0, 0, 9, 5);

//page one
        StaticPane pageOne = new StaticPane(0, 0, 9, 5);
        pageOne.addItem(new GuiItem(new ItemStack(Material.BONE), event -> event.getWhoClicked().sendMessage("Bone")), 0, 0);
        pane.addPane(0, pageOne);

//page two
        StaticPane pageTwo = new StaticPane(0, 0, 9, 5);
        pageTwo.addItem(new GuiItem(new ItemStack(Material.GLASS), event -> event.getWhoClicked().sendMessage("Glass")), 0, 0);
        pane.addPane(1, pageTwo);

//page three
        StaticPane pageThree = new StaticPane(0, 0, 9, 5);
        pageThree.addItem(new GuiItem(new ItemStack(Material.BLAZE_ROD), event -> event.getWhoClicked().sendMessage("Blaze rod")), 0, 0);
        pane.addPane(2, pageThree);

        gui.addPane(pane);

//page selection
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
            StorageManager.getInstance().addSelling(material, price);
        } catch (Exception e){
            e.printStackTrace();
            p.sendMessage("The price must be a number");
            return true;
        }

        p.sendMessage("The item has been added to the selling list");
        return true;
    }

}
