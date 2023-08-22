package me.TahaCheji.data.buyingListedItems;

import me.TahaCheji.data.list.MarketRenting;
import me.TahaCheji.util.NBTUtils;
import me.tahacheji.mafananetwork.MafanaBank;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class BuyListedItem_GUI implements InventoryHolder {

    Inventory gui;

    public ItemStack getInfo(MarketRenting listing) {
        ItemStack item = new ItemStack(Material.PAPER);
        ItemMeta meta = item.getItemMeta();
        List<String> lore = new ArrayList<>();
        meta.setDisplayName(ChatColor.DARK_PURPLE + "Listing Info");
        lore.add("--------------------------");
        lore.add(ChatColor.GOLD + "Price: $" + listing.getPrice());
        lore.add(ChatColor.GOLD + "Days: " + listing.getDays());
        lore.add(ChatColor.GOLD + "Seller: " + listing.getPlayer().getDisplayName());
        lore.add(ChatColor.GOLD + "Listing UUID: " + listing.getUuid().toString());
        lore.add("--------------------------");
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    public ItemStack getCancelItem() {
        ItemStack item = new ItemStack(Material.BARRIER);
        ItemMeta meta = item.getItemMeta();
        List<String> lore = new ArrayList<>();
        meta.setDisplayName(ChatColor.RED + "Cancel");
        lore.add("--------------------------");
        lore.add(ChatColor.GOLD + "Click to go back to all listed items");
        lore.add("--------------------------");
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    public BuyListedItem_GUI(MarketRenting listing, Player player) {
        gui = Bukkit.createInventory(null, 54, ChatColor.GRAY + "" + ChatColor.BOLD + listing.getItem().getItemMeta().getDisplayName() + " Listing_R");
        List<String> lore1 = new ArrayList<>();
        ItemStack newItem = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta newmeta = newItem.getItemMeta();
        newmeta.setDisplayName(ChatColor.GRAY + "");
        newmeta.setLore(lore1);
        newItem.setItemMeta(newmeta);
        for (int emptySlot = 0; emptySlot < gui.getSize(); emptySlot++) {
            if (gui.getItem(emptySlot) == null || gui.getItem(emptySlot).getType().equals(Material.AIR)) {
                gui.setItem(emptySlot, newItem);
            }
        }

        MafanaBank mafanaBank = MafanaBank.getInstance();
        gui.setItem(13, listing.getItem());
        gui.setItem(31, getInfo(listing));
        gui.setItem(42, getCancelItem());
        if(mafanaBank.getGamePlayerCoins().getCoins(player) >= listing.getPrice()) {
            ItemStack item = new ItemStack(Material.GREEN_CONCRETE);
            ItemMeta meta = item.getItemMeta();
            List<String> lore = new ArrayList<>();
            meta.setDisplayName(ChatColor.GREEN + "Accept");
            lore.add("--------------------------");
            lore.add(ChatColor.GOLD + "Click to buy this item");
            lore.add("--------------------------");
            meta.setLore(lore);
            item.setItemMeta(meta);
            item = NBTUtils.setString(item, "ListUUID", listing.getUuid().toString());
            gui.setItem(38, item);
        } else  {
            ItemStack item = new ItemStack(Material.RED_CONCRETE);
            ItemMeta meta = item.getItemMeta();
            List<String> lore = new ArrayList<>();
            meta.setDisplayName(ChatColor.RED + "Declined");
            lore.add("--------------------------");
            lore.add(ChatColor.GOLD + "You do not have the coins to rent this item.");
            lore.add("--------------------------");
            meta.setLore(lore);
            item.setItemMeta(meta);
            gui.setItem(38, item);
        }
    }


    @Override
    public Inventory getInventory() {
        return gui;
    }


}
