package me.TahaCheji.data.menu;

import me.TahaCheji.data.market.ItemType;
import me.TahaCheji.data.market.MarketShop_GUI;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class MarketShopMenu_GUI_ClickEvent implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (!e.getView().getTitle().contains("MarketMenu") && !e.getView().getTitle().contains("Rentables")) {
            return;
        }
        if (e.getCurrentItem() == null) {
            return;
        }
        if (e.getCurrentItem().getItemMeta() == null) {
            return;
        }
        Player player = (Player) e.getWhoClicked();
        e.setCancelled(true);
        if(e.getSlot() == 10) {
            //sword
            new MarketShop_GUI().getMarketShopGui(ItemType.SWORD).open(player);
        }
        if(e.getSlot() == 11) {
            new MarketShop_GUI().getMarketShopGui(ItemType.STAFF).open(player);
        }
        if(e.getSlot() == 12) {
            //bow
            new MarketShop_GUI().getMarketShopGui(ItemType.BOW).open(player);
        }
        if(e.getSlot() == 19) {
            //armor
            new MarketShop_GUI().getMarketShopGui(ItemType.ARMOR).open(player);
        }
        if(e.getSlot() == 20) {
            //Material
            new MarketShop_GUI().getMarketShopGui(ItemType.MATERIAL).open(player);
        }
        if(e.getSlot() == 28) {
            //all
            new MarketShop_GUI().getMarketShopGui().open(player);
        }
        if(e.getSlot() == 49) {
            player.closeInventory();
        }
        if(e.getSlot() == 34) {
            player.closeInventory();
            new MarketListItemMenu().getMarketListItemGUI(player).open(player);
        }
    }

}
