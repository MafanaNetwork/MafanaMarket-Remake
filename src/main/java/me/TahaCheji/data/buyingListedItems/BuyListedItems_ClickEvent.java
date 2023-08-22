package me.TahaCheji.data.buyingListedItems;

import de.tr7zw.nbtapi.NBTItem;
import me.TahaCheji.MafanaMarketRentables;
import me.TahaCheji.data.list.MarketRentedItem;
import me.TahaCheji.data.list.MarketRenting;

import me.TahaCheji.data.menu.MarketShopMenu;
import me.TahaCheji.util.NBTUtils;
import me.tahacheji.mafananetwork.MafanaBank;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.IOException;
import java.util.List;

public class BuyListedItems_ClickEvent implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent e) throws IOException {
        if (!e.getView().getTitle().contains("Listing_R")) {
            return;
        }
        if (e.getCurrentItem() == null) {
            return;
        }
        if (e.getCurrentItem().getItemMeta() == null) {
            return;
        }
        e.setCancelled(true);
        MafanaBank mafanaBank = MafanaBank.getInstance();
        Player player = (Player) e.getWhoClicked();
        if (e.getSlot() == 42) {
            player.openInventory(new MarketShopMenu(player).getInventory());
            return;
        }
        if (e.getSlot() == 38) {
            if (e.getCurrentItem().getItemMeta().getDisplayName().contains("Accept")) {
                for (MarketRenting listing : MafanaMarketRentables.getInstance().getListingData().getAllListings()) {
                    if (new NBTItem(e.getCurrentItem()).getString("ListUUID").contains(listing.getUuid().toString())) {
                        mafanaBank.getGamePlayerCoins().removeCoins(player, listing.getPrice());
                        mafanaBank.getGamePlayerBank().depositIntoAccount(listing.getOfflinePlayer(), listing.getPrice());
                        ItemStack itemStack = listing.getItem();
                        MarketRentedItem rentedItem = new MarketRentedItem(player, listing.getOfflinePlayer() , itemStack, listing.getDays());
                        rentedItem.setRentedItem();
                        ItemMeta itemMeta = itemStack.getItemMeta();
                        List<String> lore = itemMeta.getLore();
                        lore.add("");
                        lore.add(ChatColor.DARK_RED + "'MafanaMarket Rentables'");
                        itemMeta.setLore(lore);
                        itemStack.setItemMeta(itemMeta);
                        itemStack = NBTUtils.setBoolean(itemStack, "RENTED", true);

                        player.getInventory().addItem(itemStack);
                        MafanaMarketRentables.getInstance().getTransactionData().addListingTransaction(player, listing);
                        listing.removeListing();
                        player.sendMessage(ChatColor.GOLD + "MafanaMarket: " + ChatColor.GOLD + "You have bought " + listing.getItem().getItemMeta().getDisplayName() + ChatColor.GOLD + " from " + listing.getPlayer().getDisplayName() +
                                " for " + listing.getDays() + " days.");
                        player.closeInventory();
                    }
                }
            }

        }

    }
}
