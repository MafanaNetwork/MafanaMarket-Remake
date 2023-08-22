package me.TahaCheji.data.menu;

import de.rapha149.signgui.SignGUI;
import de.rapha149.signgui.SignGUIAction;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import me.TahaCheji.MafanaMarketRentables;
import me.TahaCheji.data.list.MarketRenting;
import me.TahaCheji.util.NBTUtils;
import me.tahacheji.mafananetwork.util.NBTUtil;
import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class MarketListItemMenu implements Listener {

    public Gui getMarketListItemGUI(Player player) {
        Gui gui = Gui.gui()
                .title(Component.text(ChatColor.GOLD + "Rent Market Item"))
                .rows(4)
                .disableAllInteractions()
                .create();
        gui.setItem(13, new GuiItem(Material.AIR));
        gui.setItem(31, new GuiItem(itemInfo(null)));

        ItemStack listItem = new ItemStack(Material.GOLD_INGOT);
        ItemMeta listItemMeta = listItem.getItemMeta();
        listItemMeta.setDisplayName(ChatColor.YELLOW + "Click To List Item 1");
        listItem.setItemMeta(listItemMeta);

        ItemStack setPrice = new ItemStack(Material.NAME_TAG);
        ItemMeta setPriceMeta = setPrice.getItemMeta();
        setPriceMeta.setDisplayName(ChatColor.YELLOW + "Click To Set Price And Days");
        setPrice.setItemMeta(setPriceMeta);

        gui.setItem(28, new GuiItem(setPrice, event -> {
            gui.getInventory().setItem(1, new ItemStack(Material.OAK_SIGN));
            openPriceSign(player, player.getOpenInventory().getTopInventory().getItem(13));
        }));

        ItemStack greystainedglass = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        ItemMeta newmeta = greystainedglass.getItemMeta();
        newmeta.setDisplayName(ChatColor.GRAY + " ");
        newmeta.setLore(new ArrayList<>());
        greystainedglass.setItemMeta(newmeta);
        greystainedglass = NBTUtil.setString(greystainedglass, "NONCLICKABLE", "");

        gui.getFiller().fill(ItemBuilder.from(greystainedglass).asGuiItem());
        return gui;
    }

    public Gui getMarketListItemGUI(Player player, ItemStack itemStack, int i, int x) {
        Gui gui = Gui.gui()
                .title(Component.text(ChatColor.GOLD + "Rent Market Item"))
                .rows(4)
                .disableAllInteractions()
                .create();
        ItemStack item = itemInfo(itemStack, i, x);
        gui.setItem(13, new GuiItem(itemStack));
        gui.setItem(31, new GuiItem(item));

        ItemStack listItem = new ItemStack(Material.GOLD_INGOT);
        ItemMeta listItemMeta = listItem.getItemMeta();
        listItemMeta.setDisplayName(ChatColor.YELLOW + "Click To List Item");
        listItem.setItemMeta(listItemMeta);

        ItemStack setPrice = new ItemStack(Material.NAME_TAG);
        ItemMeta setPriceMeta = setPrice.getItemMeta();
        setPriceMeta.setDisplayName(ChatColor.YELLOW + "Set Price: " + i + " Set Days: " + x);
        setPrice.setItemMeta(setPriceMeta);

        gui.setItem(34, new GuiItem(listItem, event -> {
            if (gui.getGuiItem(13).getItemStack().getItemMeta() != null) {
                MarketRenting listing = new MarketRenting(player, itemStack, i, x);
                listing.saveListing();
                player.sendMessage(ChatColor.GOLD + "MafanaMarket: " + ChatColor.WHITE + "You have created a listing");
            }
            gui.setItem(13, new GuiItem(Material.AIR));
            gui.getInventory().setItem(1, new ItemStack(Material.OAK_SIGN));
            player.closeInventory();
        }));

        gui.setItem(28, new GuiItem(setPrice, event -> {
            gui.getInventory().setItem(1, new ItemStack(Material.OAK_SIGN));
            openPriceSign(player, player.getOpenInventory().getTopInventory().getItem(13));
        }));

        ItemStack greystainedglass = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        ItemMeta newmeta = greystainedglass.getItemMeta();
        newmeta.setDisplayName(ChatColor.GRAY + " ");
        newmeta.setLore(new ArrayList<>());
        greystainedglass.setItemMeta(newmeta);
        greystainedglass = NBTUtil.setString(greystainedglass, "NONCLICKABLE", "");

        gui.getFiller().fill(ItemBuilder.from(greystainedglass).asGuiItem());
        return gui;
    }

    public Gui getMarketListItemGUI(Player player, ItemStack itemStack) {
        Gui gui = Gui.gui()
                .title(Component.text(ChatColor.GOLD + "Rent Market Item"))
                .rows(4)
                .disableAllInteractions()
                .create();
        gui.setItem(13, new GuiItem(itemStack));
        gui.setItem(31, new GuiItem(itemInfo(itemStack)));

        ItemStack listItem = new ItemStack(Material.GOLD_INGOT);
        ItemMeta listItemMeta = listItem.getItemMeta();
        listItemMeta.setDisplayName(ChatColor.YELLOW + "Click To List Item");
        listItem.setItemMeta(listItemMeta);

        ItemStack setPrice = new ItemStack(Material.NAME_TAG);
        ItemMeta setPriceMeta = setPrice.getItemMeta();
        setPriceMeta.setDisplayName(ChatColor.YELLOW + "Click To Set Price And Days");
        setPrice.setItemMeta(setPriceMeta);

        gui.setItem(28, new GuiItem(setPrice, event -> {
            gui.getInventory().setItem(1, new ItemStack(Material.OAK_SIGN));
            openPriceSign(player, player.getOpenInventory().getTopInventory().getItem(13));
        }));
        ItemStack greystainedglass = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        ItemMeta newmeta = greystainedglass.getItemMeta();
        newmeta.setDisplayName(ChatColor.GRAY + " ");
        newmeta.setLore(new ArrayList<>());
        greystainedglass.setItemMeta(newmeta);
        greystainedglass = NBTUtil.setString(greystainedglass, "NONCLICKABLE", "");

        gui.getFiller().fill(ItemBuilder.from(greystainedglass).asGuiItem());
        return gui;
    }


    @EventHandler
    public void onClickEvent(InventoryClickEvent event) {
        Inventory clickedInventory = event.getClickedInventory();
        if (clickedInventory != null && clickedInventory.getType() == InventoryType.PLAYER &&
                event.getView().getTitle().equals(ChatColor.GOLD + "Rent Market Item")) {
            event.setCancelled(true);

            Player player = (Player) event.getWhoClicked();
            ItemStack clickedItem = event.getCurrentItem();
            if (clickedItem != null && !clickedItem.getType().equals(Material.AIR)) {
                Inventory targetGui = player.getOpenInventory().getTopInventory();
                ItemStack itemInSlot13 = targetGui.getItem(13);

                if (itemInSlot13 != null && !itemInSlot13.getType().equals(Material.AIR)) {
                    player.getInventory().addItem(itemInSlot13);
                }
                targetGui.setItem(13, clickedItem);
                targetGui.setItem(31, itemInfo(clickedItem));
                player.getInventory().remove(clickedItem);
                player.updateInventory();
            }
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (event.getView().getTitle().equals(ChatColor.GOLD + "Rent Market Item")) {
            Player player = (Player) event.getPlayer();

            Inventory targetGui = event.getInventory();
            ItemStack itemInSlot13 = targetGui.getItem(13);
            if (itemInSlot13 != null && !itemInSlot13.getType().equals(Material.AIR)) {
                if (targetGui.getItem(1).getType() != Material.OAK_SIGN) {
                    player.getInventory().addItem(itemInSlot13);
                    player.updateInventory();
                }
            }
        }
    }


    public ItemStack itemInfo(ItemStack itemStack) {
        if (itemStack != null) {
            ItemStack item = new ItemStack(Material.PAPER);
            ItemMeta meta = item.getItemMeta();
            List<String> lore = new ArrayList<>();
            meta.setDisplayName(ChatColor.DARK_PURPLE + "Item Price Info");
            lore.add("--------------------------");
            lore.add(ChatColor.GOLD + "Average sell price: " + MafanaMarketRentables.getInstance().getListingData().getAveragePrice(itemStack));
            lore.add(ChatColor.GOLD + "Average high sell price: " + MafanaMarketRentables.getInstance().getListingData().getAverageHighestPrice(itemStack));
            lore.add(ChatColor.GOLD + "Average low sell price: " + MafanaMarketRentables.getInstance().getListingData().getAverageLowestPrice(itemStack));
            lore.add("--------------------------");
            lore.add(ChatColor.GOLD + "Highest price sold: " + MafanaMarketRentables.getInstance().getListingData().getHighestPrice(itemStack));
            lore.add(ChatColor.GOLD + "Lowest price sold: " + MafanaMarketRentables.getInstance().getListingData().getLowestPrice(itemStack));
            lore.add("--------------------------");
            lore.add(ChatColor.GOLD + "Amount of times sold: " + MafanaMarketRentables.getInstance().getTransactionData().getNumberOfTimesSold(itemStack));
            lore.add(ChatColor.GOLD + "How many are listed on the market: " + MafanaMarketRentables.getInstance().getListingData().getAmountOfListedItems(itemStack));
            lore.add("--------------------------");
            meta.setLore(lore);
            item.setItemMeta(meta);
            item = NBTUtils.setInt(item, "PRICE", MafanaMarketRentables.getInstance().getListingData().getAveragePrice(itemStack));
            return item;
        }
        ItemStack item = new ItemStack(Material.BARRIER);
        ItemMeta meta = item.getItemMeta();
        List<String> lore = new ArrayList<>();
        meta.setDisplayName(ChatColor.DARK_PURPLE + "Item Price Info");
        lore.add("--------------------------");
        lore.add(ChatColor.GOLD + "Add a item to see info.");
        lore.add("--------------------------");
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    public ItemStack itemInfo(ItemStack itemStack, int i, int x) {
        if (itemStack != null) {
            ItemStack item = new ItemStack(Material.PAPER);
            ItemMeta meta = item.getItemMeta();
            List<String> lore = new ArrayList<>();
            meta.setDisplayName(ChatColor.DARK_PURPLE + "Item Price Info");
            lore.add("--------------------------");
            lore.add(ChatColor.GOLD + "Average sell price: " + MafanaMarketRentables.getInstance().getListingData().getAveragePrice(itemStack));
            lore.add(ChatColor.GOLD + "Average high sell price: " + MafanaMarketRentables.getInstance().getListingData().getAverageHighestPrice(itemStack));
            lore.add(ChatColor.GOLD + "Average low sell price: " + MafanaMarketRentables.getInstance().getListingData().getAverageLowestPrice(itemStack));
            lore.add("--------------------------");
            lore.add(ChatColor.GOLD + "Highest price sold: " + MafanaMarketRentables.getInstance().getListingData().getHighestPrice(itemStack));
            lore.add(ChatColor.GOLD + "Lowest price sold: " + MafanaMarketRentables.getInstance().getListingData().getLowestPrice(itemStack));
            lore.add("--------------------------");
            lore.add(ChatColor.GOLD + "Amount of times sold: " + MafanaMarketRentables.getInstance().getTransactionData().getNumberOfTimesSold(itemStack));
            lore.add(ChatColor.GOLD + "How many are listed on the market: " + MafanaMarketRentables.getInstance().getListingData().getAmountOfListedItems(itemStack));
            lore.add("--------------------------");
            meta.setLore(lore);
            item.setItemMeta(meta);
            item = NBTUtils.setInt(item, "PRICE", i);
            item = NBTUtils.setInt(item, "DAYS", x);
            return item;
        }
        ItemStack item = new ItemStack(Material.BARRIER);
        ItemMeta meta = item.getItemMeta();
        List<String> lore = new ArrayList<>();
        meta.setDisplayName(ChatColor.DARK_PURPLE + "Item Price Info");
        lore.add("--------------------------");
        lore.add(ChatColor.GOLD + "Add a item to see info.");
        lore.add("--------------------------");
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }


    public void openPriceSign(Player player, ItemStack itemStack) {
        SignGUI.builder()
                .setLines(null, "-^P---------vD-", null, "MafanaMarket") // set lines
                .setType(Material.DARK_OAK_SIGN) // set the sign type
                .setHandler((p, result) -> { // set the handler/listener (called when the player finishes editing)
                    String x = result.getLineWithoutColor(0);
                    String i = result.getLineWithoutColor(2);
                    if (itemStack == null || itemStack.getType() == Material.AIR ||
                            itemStack.getItemMeta() == null || itemStack.getItemMeta().getDisplayName() == null) {
                        return List.of(SignGUIAction.run(() -> getMarketListItemGUI(player).open(player)));
                    }
                    if (x.isEmpty() || Integer.parseInt(x) >= 100000 || i.isEmpty() || Integer.parseInt(i) >= 1000) {
                        return List.of(SignGUIAction.run(() -> getMarketListItemGUI(player, itemStack).open(player)));
                    }
                    return List.of(SignGUIAction.run(() -> getMarketListItemGUI(player, itemStack, Integer.parseInt(x), Integer.parseInt(i)).open(player)));
                }).callHandlerSynchronously(MafanaMarketRentables.getInstance()).build().open(player);
    }
}
