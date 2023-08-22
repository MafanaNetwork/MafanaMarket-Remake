package me.TahaCheji.data.menu;

import de.rapha149.signgui.SignGUI;
import de.rapha149.signgui.SignGUIAction;
import me.TahaCheji.MafanaMarket;
import me.TahaCheji.data.market.ItemType;
import me.TahaCheji.data.market.MarketShop_GUI;
import me.TahaCheji.data.shop.GamePlayerMarketShop_GUI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class MarketShopMenu_GUI_ClickEvent implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (!e.getView().getTitle().contains("MarketMenu")) {
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
        if(e.getSlot() == 21) {
            //items
            new MarketShop_GUI().getMarketShopGui(ItemType.ITEM).open(player);
        }
        if(e.getSlot() == 28) {
            //all
            new MarketShop_GUI().getMarketShopGui().open(player);
        }
        if(e.getSlot() == 39) {
            new MarketShop_GUI().getMarketShopGui().open(player);
        }
        if(e.getSlot() == 49) {
            player.closeInventory();
        }
        if(e.getSlot() == 25) {
            if(MafanaMarket.getInstance().getListingData().getAllPlayerListings(player) != null) {
                player.openInventory(new GamePlayerMarketShop_GUI(MafanaMarket.getInstance().getListingData().getPlayerShop(player), player).getInventory());
            } else {
                player.sendMessage(ChatColor.GOLD + "MafanaMarket: " +  ChatColor.RED + "Error contact a admin if this is not correct");
            }
        }
        if(e.getSlot() == 34) {
            player.closeInventory();
            new MarketListItemMenu().getMarketListItemGUI(player).open(player);
        }
        if(e.getSlot() == 16) {
            openSearchSign(player);
        }

    }

    public void openSearchSign(Player player) {
        SignGUI.builder()
                .setLines(null, "---------------", "Search", "MafanaMarket") // set lines
                .setType(Material.DARK_OAK_SIGN) // set the sign type
                .setHandler((p, result) -> { // set the handler/listener (called when the player finishes editing)
                    String x = result.getLineWithoutColor(0);
                    Player newPlayer = Bukkit.getPlayer(x);
                    if (newPlayer == null) {
                        return List.of(SignGUIAction.run(() -> player.openInventory(new MarketShopMenu(player).getInventory())),
                                SignGUIAction.run(() -> player.sendMessage(ChatColor.RED + "MafanaMarket: PLAYER_NOT_FOUND")));
                    }
                    return List.of(SignGUIAction.run(() -> player.openInventory(new GamePlayerMarketShop_GUI(MafanaMarket.getInstance().getListingData().getPlayerShop(newPlayer), player).getInventory())));
                }).callHandlerSynchronously(MafanaMarket.getInstance()).build().open(player);
    }

}
