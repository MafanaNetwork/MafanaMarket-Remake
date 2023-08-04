package me.TahaCheji.data.shop;

import me.TahaCheji.MafanaMarket;
import me.TahaCheji.data.list.MarketListing;
import me.TahaCheji.util.NBTUtils;
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

public class GamePlayerMarketShop_GUI implements InventoryHolder {

    Inventory gui;
    private List<ItemStack> itemStacks = new ArrayList<>();

    public ItemStack getCloseShop() {
        ItemStack item = new ItemStack(Material.BARRIER);
        ItemMeta meta = item.getItemMeta();
        List<String> lore = new ArrayList<>();
        meta.setDisplayName(ChatColor.RED + "Close");
        lore.add("--------------------------");
        lore.add(ChatColor.GOLD + "Click to close the menu");
        lore.add("--------------------------");
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    public ItemStack shopInfo(GamePlayerMarketShop shops) {
            ItemStack item = new ItemStack(Material.PAPER);
            ItemMeta meta = item.getItemMeta();
            List<String> lore = new ArrayList<>();
            meta.setDisplayName(ChatColor.DARK_PURPLE + "Shop Info");
            lore.add("--------------------------");
            lore.add(ChatColor.GOLD + "Shop Owner: " + shops.getOwner().getDisplayName());
            lore.add(ChatColor.GOLD + "Shop UUID: " + shops.getUuid().toString());
            lore.add("--------------------------");
            meta.setLore(lore);
            item.setItemMeta(meta);
            return item;
    }

    public GamePlayerMarketShop_GUI(GamePlayerMarketShop shops, Player player) {
        if(shops.getOwner().getUniqueId().toString().contains(player.getUniqueId().toString())) {
            gui = Bukkit.createInventory(null, 54, ChatColor.GOLD + "" + ChatColor.BOLD + "Your Shop");
            List<String> lore = new ArrayList<>();
            ItemStack newItem = new ItemStack(Material.GREEN_STAINED_GLASS_PANE);
            ItemMeta newmeta = newItem.getItemMeta();
            newmeta.setDisplayName(ChatColor.GRAY + "");
            newmeta.setLore(lore);
            newItem.setItemMeta(newmeta);
            for(MarketListing listing : MafanaMarket.getInstance().getListingData().getAllPlayerListings(shops.getOwner())) {
                ItemStack item = listing.getItem();
                ItemMeta itemMeta = item.getItemMeta();
                List<String> itemLore = new ArrayList<>();
                for(String string : itemMeta.getLore()){
                    itemLore.add(string);
                }
                itemLore.add(ChatColor.DARK_GRAY + "");
                itemLore.add("------------------------");
                itemLore.add(ChatColor.DARK_GRAY + "Price: $" + listing.getPrice());
                if(shops.getOwner().isOnline()) {
                    itemLore.add(ChatColor.DARK_GRAY + "Seller: " + listing.getPlayer().getDisplayName() + " " + ChatColor.GREEN + "[ONLINE]");
                } else {
                    itemLore.add(ChatColor.DARK_GRAY + "Seller: " + listing.getPlayer().getDisplayName() + " " + ChatColor.RED + "[OFFLINE]");
                }
                itemLore.add(ChatColor.DARK_GRAY + "Listing UUID: " + listing.getUuid().toString());
                itemLore.add(ChatColor.DARK_GRAY + "");
                itemLore.add(ChatColor.DARK_GRAY + "Click to cancel!");
                itemLore.add("------------------------");
                itemMeta.setLore(itemLore);
                item.setItemMeta(itemMeta);
                item = NBTUtils.setString(item, "ListUUID", listing.getUuid().toString());
                item = NBTUtils.setBoolean(item, "Cancel", true);
                itemStacks.add(item);
                for(int i = 10; i < 43; i++) {
                    if(i == 17 || i == 26 || i == 35 || i == 18 || i == 27 || i == 36 || i == 40) {
                        continue;
                    }
                    if(i >= itemStacks.size() + 10) {
                        break;
                    }
                    gui.setItem(i, itemStacks.get(i - 10));
                }
            }
            gui.setItem(49, getCloseShop());
            gui.setItem(40, shopInfo(shops));

            for (int emptySlot = 0; emptySlot < gui.getSize(); emptySlot++) {
                if (gui.getItem(emptySlot) == null || gui.getItem(emptySlot).getType().equals(Material.AIR)) {
                    gui.setItem(emptySlot, newItem);
                }
            }
        } else  {
            gui = Bukkit.createInventory(null, 54, ChatColor.GOLD + "" + ChatColor.BOLD + shops.getOwner().getDisplayName() + "'s Shop");
            List<String> lore = new ArrayList<>();
            ItemStack newItem = new ItemStack(Material.GREEN_STAINED_GLASS_PANE);
            ItemMeta newmeta = newItem.getItemMeta();
            newmeta.setDisplayName(ChatColor.GRAY + "");
            newmeta.setLore(lore);
            newItem.setItemMeta(newmeta);
            for(MarketListing listing : shops.getItems()) {
                ItemStack item = listing.getItem();
                ItemMeta itemMeta = item.getItemMeta();
                List<String> itemLore = new ArrayList<>();
                for(String string : itemMeta.getLore()){
                    itemLore.add(string);
                }
                itemLore.add(ChatColor.DARK_GRAY + "");
                itemLore.add("------------------------");
                itemLore.add(ChatColor.DARK_GRAY + "Price: $" + listing.getPrice());
                if(shops.getOwner().isOnline()) {
                    itemLore.add(ChatColor.DARK_GRAY + "Seller: " + listing.getPlayer().getDisplayName() + " " + ChatColor.GREEN + "[ONLINE]");
                } else {
                    itemLore.add(ChatColor.DARK_GRAY + "Seller: " + listing.getPlayer().getDisplayName() + " " + ChatColor.RED + "[OFFLINE]");
                }
                itemLore.add(ChatColor.DARK_GRAY + "Listing UUID: " + listing.getUuid().toString());
                itemLore.add(ChatColor.DARK_GRAY + "");
                itemLore.add(ChatColor.DARK_GRAY + "Click to buy!");
                itemLore.add("------------------------");
                itemMeta.setLore(itemLore);
                item.setItemMeta(itemMeta);
                item = NBTUtils.setString(item, "ListUUID", listing.getUuid().toString());
                itemStacks.add(item);
                for(int i = 10; i < 43; i++) {
                    if(i == 17 || i == 26 || i == 35 || i == 18 || i == 27 || i == 36 || i == 40) {
                        continue;
                    }
                    if(i >= itemStacks.size() + 10) {
                        break;
                    }
                    gui.setItem(i, itemStacks.get(i - 10));
                }
            }
            gui.setItem(49, getCloseShop());
            gui.setItem(40, shopInfo(shops));

            for (int emptySlot = 0; emptySlot < gui.getSize(); emptySlot++) {
                if (gui.getItem(emptySlot) == null || gui.getItem(emptySlot).getType().equals(Material.AIR)) {
                    gui.setItem(emptySlot, newItem);
                }
            }
        }

    }


    @Override
    public Inventory getInventory() {
        return gui;
    }

}
