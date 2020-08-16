package ca.coulis.HopperSeller.listeners;

import ca.coulis.HopperSeller.HopperSeller;
import ca.coulis.HopperSeller.data.HopperData;
import ca.coulis.HopperSeller.data.SellingItem;
import ca.coulis.HopperSeller.Utils;
import ca.coulis.HopperSeller.storage.StorageManager;
import com.github.stefvanschie.inventoryframework.Gui;
import com.github.stefvanschie.inventoryframework.GuiItem;
import com.github.stefvanschie.inventoryframework.pane.PaginatedPane;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import org.bukkit.Material;
import org.bukkit.block.Hopper;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PlayerOpenHopperEvent implements Listener {

    @EventHandler(priority = EventPriority.LOW)
    public void onOpen(final PlayerInteractEvent event) {
        Player p = event.getPlayer();

        if(!p.isSneaking())
            return;

        if(event.getAction() != Action.RIGHT_CLICK_BLOCK)
            return;

        if(event.getClickedBlock() == null)
            return;

        if(!(event.getClickedBlock().getState() instanceof Hopper))
            return;

        HopperData hopper = StorageManager.getInstance().getHopperByLocation(event.getClickedBlock().getLocation());
        if(hopper == null) hopper = new HopperData(event.getClickedBlock().getLocation());

        this.browse(p, hopper);

        event.setCancelled(true);
    }

    private void browse(Player p, HopperData hopper) {
        Gui gui = new Gui(HopperSeller.getInstance(), 6, Utils.toColor(HopperSeller.chatPrefix + "&eBrowser"));

        PaginatedPane pane = new PaginatedPane(0, 0, 9, 5);

        List<SellingItem> items = StorageManager.getInstance().getSellings();
        for(int i = 0; i <= (int)Math.ceil(items.size() / 45); i++) {
            StaticPane aPage = new StaticPane(0, 0, 9, 5);
            for(int j = (i * 45); j < Math.min((i * 45 + 45), items.size()); j++) {
                ItemStack item = new ItemStack(items.get(j).getItem());
                ItemMeta meta = item.getItemMeta();
                meta.setDisplayName(Utils.toColor((hopper.getSellings().contains(items.get(j)) ? "&a&l[ON]" : "&c&l[OFF]") + " &e&lClick to toggle"));
                List<String> lores = new ArrayList<>();
                lores.add(Utils.toColor("&aPrice : " + items.get(j).getPrice() + "$ each"));
                meta.setLore(lores);
                item.setItemMeta(meta);

                aPage.addItem(new GuiItem(item, event -> {
                    hopper.toggleMaterial(item.getType());
                    event.getWhoClicked().sendMessage(Utils.toColor(HopperSeller.chatPrefix + "&aThe item has been toggled"));

                    if (hopper.getSellings().isEmpty()) StorageManager.getInstance().removeHopper(hopper);
                    else StorageManager.getInstance().updateHopper(hopper);

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
    }


}
