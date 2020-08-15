package ca.coulis.HopperSeller.commands;

import ca.coulis.HopperSeller.StorageManager;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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

            if(args[0].equalsIgnoreCase("add") && args[1] != null) {
                Material material = p.getInventory().getItemInMainHand().getType();

                if(material == null) {
                    p.sendMessage("MATERIAL NULL");
                    return true;
                }

                if(material.isAir()) {
                    p.sendMessage("You cannot add air as a selling item");
                    return true;
                }

                System.out.println("MATERIAL : " + material);
//                try {
                    double price = Double.parseDouble(args[1]);
                    p.sendMessage("Price : " + price);
                    StorageManager.getInstance().addSelling(material, price);
//                } catch (Exception e){
//                    e.printStackTrace();
//                    p.sendMessage("The price must be a number");
//                    return true;
//                }

                p.sendMessage("The item has been added to the selling list");
                return true;
            }

        }

        return true;
    }

}
