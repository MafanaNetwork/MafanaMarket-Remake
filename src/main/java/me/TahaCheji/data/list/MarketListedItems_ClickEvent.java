package me.TahaCheji.data.list;

import de.tr7zw.nbtapi.NBTItem;
import me.TahaCheji.MafanaMarket;
import me.TahaCheji.data.buyingListedItems.BuyListedItem_GUI;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class MarketListedItems_ClickEvent implements Listener {


    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (!e.getView().getTitle().contains("All Listings")) {
            return;
        }
        if (e.getCurrentItem() == null) {
            return;
        }
        if (e.getCurrentItem().getItemMeta() == null) {
            return;
        }
        if (!new NBTItem(e.getCurrentItem()).hasKey("ListUUID")) {
            e.setCancelled(true);
            return;
        }
        for (MarketListing listing : MafanaMarket.getInstance().getListingData().getAllListings()) {
            if (new NBTItem(e.getCurrentItem()).getString("ListUUID").equalsIgnoreCase(listing.getUuid().toString())) {
                Player player = (Player) e.getWhoClicked();
                if(listing.getPlayer().getUniqueId().toString().equalsIgnoreCase(player.getUniqueId().toString())) {
                    e.setCancelled(true);
                    return;
                }
                player.openInventory(new BuyListedItem_GUI(listing, player).getInventory());
            }
        }
    }


}
