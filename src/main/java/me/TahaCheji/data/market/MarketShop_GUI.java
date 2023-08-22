package me.TahaCheji.data.market;

import de.rapha149.signgui.SignGUI;
import de.rapha149.signgui.SignGUIAction;
import de.tr7zw.nbtapi.NBTItem;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import dev.triumphteam.gui.guis.PaginatedGui;
import me.TahaCheji.MafanaMarket;
import me.TahaCheji.data.list.MarketListing;
import me.TahaCheji.util.NBTUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public class MarketShop_GUI implements Listener {

    public ItemStack getCloseShop() {
        ItemStack item = new ItemStack(Material.BARRIER);
        ItemMeta meta = item.getItemMeta();
        List<String> lore = new ArrayList<>();
        meta.setDisplayName(ChatColor.RED + "Close");
        lore.add("--------------------------");
        lore.add(ChatColor.GOLD + "Click to go back to the main menu");
        lore.add("--------------------------");
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    public PaginatedGui getMarketShopGui(ItemType itemType) {
        PaginatedGui gui = Gui.paginated()
                .title(Component.text(ChatColor.GOLD + "MafanaMarket " + ChatColor.DARK_GREEN + itemType.getLore() + ChatColor.GOLD + " Listings"))
                .rows(6)
                .pageSize(28)
                .disableAllInteractions()
                .create();
        List<String> lore = new ArrayList<>();
        ItemStack greystainedglass = new ItemStack(Material.ORANGE_STAINED_GLASS_PANE);
        ItemMeta newmeta = greystainedglass.getItemMeta();
        newmeta.setDisplayName(ChatColor.GRAY + " ");
        newmeta.setLore(lore);
        greystainedglass.setItemMeta(newmeta);

        ItemStack closeShop = new ItemStack(Material.BARRIER);
        ItemMeta closeShopeta = closeShop.getItemMeta();
        closeShopeta.setDisplayName(ChatColor.GRAY + "Close Shop");
        closeShopeta.setLore(lore);
        closeShop.setItemMeta(closeShopeta);

        gui.setItem(0, new GuiItem(greystainedglass));
        gui.setItem(1, new GuiItem(greystainedglass));
        gui.setItem(2, new GuiItem(greystainedglass));
        gui.setItem(3, new GuiItem(greystainedglass));
        gui.setItem(4, new GuiItem(greystainedglass));
        gui.setItem(5, new GuiItem(greystainedglass));
        gui.setItem(6, new GuiItem(greystainedglass));
        gui.setItem(7, new GuiItem(greystainedglass));
        gui.setItem(8, new GuiItem(greystainedglass));
        gui.setItem(17, new GuiItem(greystainedglass));
        gui.setItem(26, new GuiItem(greystainedglass));
        gui.setItem(35, new GuiItem(greystainedglass));
        gui.setItem(45, new GuiItem(greystainedglass));
        gui.setItem(53, new GuiItem(greystainedglass));
        gui.setItem(52, new GuiItem(greystainedglass));
        gui.setItem(51, new GuiItem(greystainedglass));
        gui.setItem(50, new GuiItem(greystainedglass));
        gui.setItem(48, new GuiItem(greystainedglass));
        gui.setItem(47, new GuiItem(greystainedglass));
        gui.setItem(46, new GuiItem(greystainedglass));
        gui.setItem(44, new GuiItem(greystainedglass));
        gui.setItem(36, new GuiItem(greystainedglass));
        gui.setItem(27, new GuiItem(greystainedglass));
        gui.setItem(18, new GuiItem(greystainedglass));
        gui.setItem(9, new GuiItem(greystainedglass));
        gui.setItem(49, new GuiItem(getCloseShop()));
        gui.setItem(6, 3, ItemBuilder.from(Material.PAPER).setName(ChatColor.DARK_GRAY + "Previous").asGuiItem(event -> gui.previous()));
        gui.setItem(6, 7, ItemBuilder.from(Material.PAPER).setName(ChatColor.DARK_GRAY + "Next").asGuiItem(event -> gui.next()));
        for (MarketListing listing : MafanaMarket.getInstance().getListingData().getAllListings()) {
            if (itemType == ItemType.ARMOR) {
                if (new NBTItem(listing.getItem()).getString("ItemType").equalsIgnoreCase(ItemType.LEGGGINGS.getLore()) || new NBTItem(listing.getItem()).getString("ItemType").equalsIgnoreCase(ItemType.CHESTPLATE.getLore())
                || new NBTItem(listing.getItem()).getString("ItemType").equalsIgnoreCase(ItemType.HELMET.getLore()) || new NBTItem(listing.getItem()).getString("ItemType").equalsIgnoreCase(ItemType.BOOTS.getLore())) {
                    ItemStack item = listing.getItem();
                    ItemMeta itemMeta = item.getItemMeta();
                    List<String> itemLore = new ArrayList<>();
                    for (String string : itemMeta.getLore()) {
                        itemLore.add(string);
                    }
                    itemLore.add(ChatColor.DARK_GRAY + "");
                    itemLore.add("------------------------");
                    itemLore.add(ChatColor.DARK_GRAY + "Price: $" + listing.getPrice());
                    if (listing.getPlayer().isOnline()) {
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
                    gui.addItem(new GuiItem(item));
                    continue;
                }
            }
            if (!new NBTItem(listing.getItem()).getString("ItemType").contains(itemType.getLore())) {
                continue;
            }
            ItemStack item = listing.getItem();
            ItemMeta itemMeta = item.getItemMeta();
            List<String> itemLore = new ArrayList<>();
            for (String string : itemMeta.getLore()) {
                itemLore.add(string);
            }
            itemLore.add(ChatColor.DARK_GRAY + "");
            itemLore.add("------------------------");
            itemLore.add(ChatColor.DARK_GRAY + "Price: $" + listing.getPrice());
            if (listing.getPlayer().isOnline()) {
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
            gui.addItem(new GuiItem(item));
        }

        ItemStack searchItem = new ItemStack(Material.NAME_TAG);
        ItemMeta searchItemMeta = searchItem.getItemMeta();
        searchItemMeta.setDisplayName(ChatColor.YELLOW + "Search by Item Name");
        searchItem.setItemMeta(searchItemMeta);

        gui.setItem(53, new GuiItem(searchItem, event -> {
            event.getWhoClicked().closeInventory();
            openSearchSign((Player) event.getWhoClicked(), itemType);
        }));


        return gui;
    }

    public PaginatedGui getMarketShopGui(ItemType itemType, String s) {
        PaginatedGui gui = Gui.paginated()
                .title(Component.text(ChatColor.GOLD + "MafanaMarket " + ChatColor.DARK_GREEN + itemType.getLore() + ChatColor.GOLD + " Listings " + ChatColor.GRAY + "Filter: " + s))
                .rows(6)
                .pageSize(28)
                .disableAllInteractions()
                .create();
        List<String> lore = new ArrayList<>();
        ItemStack greystainedglass = new ItemStack(Material.ORANGE_STAINED_GLASS_PANE);
        ItemMeta newmeta = greystainedglass.getItemMeta();
        newmeta.setDisplayName(ChatColor.GRAY + " ");
        newmeta.setLore(lore);
        greystainedglass.setItemMeta(newmeta);

        ItemStack closeShop = new ItemStack(Material.BARRIER);
        ItemMeta closeShopeta = closeShop.getItemMeta();
        closeShopeta.setDisplayName(ChatColor.GRAY + "Close Shop");
        closeShopeta.setLore(lore);
        closeShop.setItemMeta(closeShopeta);

        gui.setItem(0, new GuiItem(greystainedglass));
        gui.setItem(1, new GuiItem(greystainedglass));
        gui.setItem(2, new GuiItem(greystainedglass));
        gui.setItem(3, new GuiItem(greystainedglass));
        gui.setItem(4, new GuiItem(greystainedglass));
        gui.setItem(5, new GuiItem(greystainedglass));
        gui.setItem(6, new GuiItem(greystainedglass));
        gui.setItem(7, new GuiItem(greystainedglass));
        gui.setItem(8, new GuiItem(greystainedglass));
        gui.setItem(17, new GuiItem(greystainedglass));
        gui.setItem(26, new GuiItem(greystainedglass));
        gui.setItem(35, new GuiItem(greystainedglass));
        gui.setItem(45, new GuiItem(greystainedglass));
        gui.setItem(53, new GuiItem(greystainedglass));
        gui.setItem(52, new GuiItem(greystainedglass));
        gui.setItem(51, new GuiItem(greystainedglass));
        gui.setItem(50, new GuiItem(greystainedglass));
        gui.setItem(48, new GuiItem(greystainedglass));
        gui.setItem(47, new GuiItem(greystainedglass));
        gui.setItem(46, new GuiItem(greystainedglass));
        gui.setItem(44, new GuiItem(greystainedglass));
        gui.setItem(36, new GuiItem(greystainedglass));
        gui.setItem(27, new GuiItem(greystainedglass));
        gui.setItem(18, new GuiItem(greystainedglass));
        gui.setItem(9, new GuiItem(greystainedglass));
        gui.setItem(49, new GuiItem(getCloseShop()));
        gui.setItem(6, 3, ItemBuilder.from(Material.PAPER).setName(ChatColor.DARK_GRAY + "Previous").asGuiItem(event -> gui.previous()));
        gui.setItem(6, 7, ItemBuilder.from(Material.PAPER).setName(ChatColor.DARK_GRAY + "Next").asGuiItem(event -> gui.next()));
        for (MarketListing listing : MafanaMarket.getInstance().getListingData().getAllListings()) {
            if (!listing.getItem().getItemMeta().getDisplayName().toLowerCase().contains(s.toLowerCase())) {
                continue;
            }
            if (new NBTItem(listing.getItem()).getString("ItemType").equalsIgnoreCase(ItemType.LEGGGINGS.getLore()) || new NBTItem(listing.getItem()).getString("ItemType").equalsIgnoreCase(ItemType.CHESTPLATE.getLore())
                    || new NBTItem(listing.getItem()).getString("ItemType").equalsIgnoreCase(ItemType.HELMET.getLore()) || new NBTItem(listing.getItem()).getString("ItemType").equalsIgnoreCase(ItemType.BOOTS.getLore())) {
                ItemStack item = listing.getItem();
                ItemMeta itemMeta = item.getItemMeta();
                List<String> itemLore = new ArrayList<>();
                for (String string : itemMeta.getLore()) {
                    itemLore.add(string);
                }
                itemLore.add(ChatColor.DARK_GRAY + "");
                itemLore.add("------------------------");
                itemLore.add(ChatColor.DARK_GRAY + "Price: $" + listing.getPrice());
                if (listing.getPlayer().isOnline()) {
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
                gui.addItem(new GuiItem(item));
                continue;
            }
            if (!new NBTItem(listing.getItem()).getString("ItemType").contains(itemType.getLore())) {
                continue;
            }
            ItemStack item = listing.getItem();
            ItemMeta itemMeta = item.getItemMeta();
            List<String> itemLore = new ArrayList<>();
            for (String string : itemMeta.getLore()) {
                itemLore.add(string);
            }
            itemLore.add(ChatColor.DARK_GRAY + "");
            itemLore.add("------------------------");
            itemLore.add(ChatColor.DARK_GRAY + "Price: $" + listing.getPrice());
            if (listing.getPlayer().isOnline()) {
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
            gui.addItem(new GuiItem(item));
        }
        ItemStack searchItem = new ItemStack(Material.NAME_TAG);
        ItemMeta searchItemMeta = searchItem.getItemMeta();
        searchItemMeta.setDisplayName(ChatColor.YELLOW + "Search by Item Name");
        searchItem.setItemMeta(searchItemMeta);

        gui.setItem(53, new GuiItem(searchItem, event -> {
            event.getWhoClicked().closeInventory();
            openSearchSign((Player) event.getWhoClicked(), itemType);
        }));

        return gui;
    }

    public PaginatedGui getMarketShopGui() {
        PaginatedGui gui = Gui.paginated()
                .title(Component.text(ChatColor.GOLD + "MafanaMarket All Listings"))
                .rows(6)
                .pageSize(28)
                .disableAllInteractions()
                .create();
        List<String> lore = new ArrayList<>();
        ItemStack greystainedglass = new ItemStack(Material.PURPLE_STAINED_GLASS_PANE);
        ItemMeta newmeta = greystainedglass.getItemMeta();
        newmeta.setDisplayName(ChatColor.GRAY + " ");
        newmeta.setLore(lore);
        greystainedglass.setItemMeta(newmeta);

        ItemStack closeShop = new ItemStack(Material.BARRIER);
        ItemMeta closeShopeta = closeShop.getItemMeta();
        closeShopeta.setDisplayName(ChatColor.GRAY + "Close Shop");
        closeShopeta.setLore(lore);
        closeShop.setItemMeta(closeShopeta);

        gui.setItem(0, new GuiItem(greystainedglass));
        gui.setItem(1, new GuiItem(greystainedglass));
        gui.setItem(2, new GuiItem(greystainedglass));
        gui.setItem(3, new GuiItem(greystainedglass));
        gui.setItem(4, new GuiItem(greystainedglass));
        gui.setItem(5, new GuiItem(greystainedglass));
        gui.setItem(6, new GuiItem(greystainedglass));
        gui.setItem(7, new GuiItem(greystainedglass));
        gui.setItem(8, new GuiItem(greystainedglass));
        gui.setItem(17, new GuiItem(greystainedglass));
        gui.setItem(26, new GuiItem(greystainedglass));
        gui.setItem(35, new GuiItem(greystainedglass));
        gui.setItem(45, new GuiItem(greystainedglass));
        gui.setItem(53, new GuiItem(greystainedglass));
        gui.setItem(52, new GuiItem(greystainedglass));
        gui.setItem(51, new GuiItem(greystainedglass));
        gui.setItem(50, new GuiItem(greystainedglass));
        gui.setItem(48, new GuiItem(greystainedglass));
        gui.setItem(47, new GuiItem(greystainedglass));
        gui.setItem(46, new GuiItem(greystainedglass));
        gui.setItem(44, new GuiItem(greystainedglass));
        gui.setItem(36, new GuiItem(greystainedglass));
        gui.setItem(27, new GuiItem(greystainedglass));
        gui.setItem(18, new GuiItem(greystainedglass));
        gui.setItem(9, new GuiItem(greystainedglass));
        gui.setItem(49, new GuiItem(closeShop));
        gui.setItem(6, 3, ItemBuilder.from(Material.PAPER).setName(ChatColor.DARK_GRAY + "Previous").asGuiItem(event -> gui.previous()));
        gui.setItem(6, 7, ItemBuilder.from(Material.PAPER).setName(ChatColor.DARK_GRAY + "Next").asGuiItem(event -> gui.next()));
        for (MarketListing listing : MafanaMarket.getInstance().getListingData().getAllListings()) {
            ItemStack item = listing.getItem();
            ItemMeta itemMeta = item.getItemMeta();
            List<String> itemLore = new ArrayList<>();
            for (String string : itemMeta.getLore()) {
                itemLore.add(string);
            }
            itemLore.add(ChatColor.DARK_GRAY + "");
            itemLore.add("------------------------");
            itemLore.add(ChatColor.DARK_GRAY + "Price: $" + listing.getPrice());
            if (listing.getPlayer().isOnline()) {
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
            gui.addItem(new GuiItem(item));
        }

        ItemStack searchItem = new ItemStack(Material.NAME_TAG);
        ItemMeta searchItemMeta = searchItem.getItemMeta();
        searchItemMeta.setDisplayName(ChatColor.YELLOW + "Search by Item Name");
        searchItem.setItemMeta(searchItemMeta);

        gui.setItem(53, new GuiItem(searchItem, event -> {
            event.getWhoClicked().closeInventory();
            openSearchSign((Player) event.getWhoClicked(), null);
        }));

        return gui;
    }

    public PaginatedGui getMarketShopGui(String s) {
        PaginatedGui gui = Gui.paginated()
                .title(Component.text(ChatColor.GOLD + "MafanaMarket All Listings " + ChatColor.GRAY + "Filter: " + s))
                .rows(6)
                .pageSize(28)
                .disableAllInteractions()
                .create();
        List<String> lore = new ArrayList<>();
        ItemStack greystainedglass = new ItemStack(Material.PURPLE_STAINED_GLASS_PANE);
        ItemMeta newmeta = greystainedglass.getItemMeta();
        newmeta.setDisplayName(ChatColor.GRAY + " ");
        newmeta.setLore(lore);
        greystainedglass.setItemMeta(newmeta);

        ItemStack closeShop = new ItemStack(Material.BARRIER);
        ItemMeta closeShopeta = closeShop.getItemMeta();
        closeShopeta.setDisplayName(ChatColor.GRAY + "Close Shop");
        closeShopeta.setLore(lore);
        closeShop.setItemMeta(closeShopeta);

        gui.setItem(0, new GuiItem(greystainedglass));
        gui.setItem(1, new GuiItem(greystainedglass));
        gui.setItem(2, new GuiItem(greystainedglass));
        gui.setItem(3, new GuiItem(greystainedglass));
        gui.setItem(4, new GuiItem(greystainedglass));
        gui.setItem(5, new GuiItem(greystainedglass));
        gui.setItem(6, new GuiItem(greystainedglass));
        gui.setItem(7, new GuiItem(greystainedglass));
        gui.setItem(8, new GuiItem(greystainedglass));
        gui.setItem(17, new GuiItem(greystainedglass));
        gui.setItem(26, new GuiItem(greystainedglass));
        gui.setItem(35, new GuiItem(greystainedglass));
        gui.setItem(45, new GuiItem(greystainedglass));
        gui.setItem(53, new GuiItem(greystainedglass));
        gui.setItem(52, new GuiItem(greystainedglass));
        gui.setItem(51, new GuiItem(greystainedglass));
        gui.setItem(50, new GuiItem(greystainedglass));
        gui.setItem(48, new GuiItem(greystainedglass));
        gui.setItem(47, new GuiItem(greystainedglass));
        gui.setItem(46, new GuiItem(greystainedglass));
        gui.setItem(44, new GuiItem(greystainedglass));
        gui.setItem(36, new GuiItem(greystainedglass));
        gui.setItem(27, new GuiItem(greystainedglass));
        gui.setItem(18, new GuiItem(greystainedglass));
        gui.setItem(9, new GuiItem(greystainedglass));
        gui.setItem(49, new GuiItem(closeShop));
        gui.setItem(6, 3, ItemBuilder.from(Material.PAPER).setName(ChatColor.DARK_GRAY + "Previous").asGuiItem(event -> gui.previous()));
        gui.setItem(6, 7, ItemBuilder.from(Material.PAPER).setName(ChatColor.DARK_GRAY + "Next").asGuiItem(event -> gui.next()));
        for (MarketListing listing : MafanaMarket.getInstance().getListingData().getAllListings()) {
            if (!listing.getItem().getItemMeta().getDisplayName().toLowerCase().contains(s.toLowerCase())) {
                continue;
            }
            ItemStack item = listing.getItem();
            ItemMeta itemMeta = item.getItemMeta();
            List<String> itemLore = new ArrayList<>();
            for (String string : itemMeta.getLore()) {
                itemLore.add(string);
            }
            itemLore.add(ChatColor.DARK_GRAY + "");
            itemLore.add("------------------------");
            itemLore.add(ChatColor.DARK_GRAY + "Price: $" + listing.getPrice());
            if (listing.getPlayer().isOnline()) {
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
            gui.addItem(new GuiItem(item));
        }
        ItemStack searchItem = new ItemStack(Material.NAME_TAG);
        ItemMeta searchItemMeta = searchItem.getItemMeta();
        searchItemMeta.setDisplayName(ChatColor.YELLOW + "Search by Item Name");
        searchItem.setItemMeta(searchItemMeta);

        gui.setItem(53, new GuiItem(searchItem, event -> {
            event.getWhoClicked().closeInventory();
            openSearchSign((Player) event.getWhoClicked(), null);
        }));

        return gui;
    }

    public void openSearchSign(Player player, ItemType itemType) {
        if (itemType != null) {
            SignGUI.builder()
                    .setLines(null, "---------------", itemType.getLore(), "MafanaMarket") // set lines
                    .setType(Material.DARK_OAK_SIGN) // set the sign type
                    .setHandler((p, result) -> { // set the handler/listener (called when the player finishes editing)
                        String x = result.getLineWithoutColor(0);
                        if (x.isEmpty()) {
                            return List.of(SignGUIAction.run(() -> getMarketShopGui(itemType).open(player)));
                        }
                        return List.of(SignGUIAction.run(() -> getMarketShopGui(itemType, x).open(player)));
                    }).callHandlerSynchronously(MafanaMarket.getInstance()).build().open(player);
        } else {
            SignGUI.builder()
                    .setLines(null, "---------------", "Everything", "MafanaMarket") // set lines
                    .setType(Material.DARK_OAK_SIGN) // set the sign type
                    .setHandler((p, result) -> { // set the handler/listener (called when the player finishes editing)
                        String x = result.getLineWithoutColor(0);
                        if (x.isEmpty()) {
                            return List.of(SignGUIAction.run(() -> getMarketShopGui().open(player)));
                        }
                        return List.of(SignGUIAction.run(() -> getMarketShopGui(x).open(player)));
                    }).callHandlerSynchronously(MafanaMarket.getInstance()).build().open(player);
        }
    }


}
