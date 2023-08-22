package me.TahaCheji.data.market;

import de.tr7zw.nbtapi.NBTItem;
import me.TahaCheji.MafanaMarketRentables;
import me.TahaCheji.data.buyingListedItems.BuyListedItem_GUI;
import me.TahaCheji.data.list.MarketRenting;
import me.TahaCheji.data.menu.MarketShopMenu;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class MarketShop_GUI_ClickEvent implements Listener {


    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (!e.getView().getTitle().contains("MafanaMarket")) {
            return;
        }
        if (e.getCurrentItem() == null) {
            return;
        }
        if (e.getCurrentItem().getItemMeta() == null) {
            return;
        }
        if(e.getSlot() == 49) {
            Player player = (Player) e.getWhoClicked();
            player.openInventory(new MarketShopMenu(player).getInventory());
        }
        if (!new NBTItem(e.getCurrentItem()).hasKey("ListUUID")) {
            e.setCancelled(true);
            return;
        }
        for (MarketRenting listing : MafanaMarketRentables.getInstance().getListingData().getAllListings()) {
            if (new NBTItem(e.getCurrentItem()).getString("ListUUID").contains(listing.getUuid().toString())) {
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
