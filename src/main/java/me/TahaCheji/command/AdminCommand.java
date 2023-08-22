package me.TahaCheji.command;

import me.TahaCheji.data.list.MarketRentedItem;
import me.TahaCheji.data.menu.MarketShopMenu;
import me.TahaCheji.util.NBTUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class AdminCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(label.equalsIgnoreCase("mmr")) {
            Player player = (Player) sender;
            if(args[0].equalsIgnoreCase("rent")) {
                //MarketRentedItem rentedItem = new MarketRentedItem(player, player, player.getItemInHand(), 2);
                //rentedItem.setRentedItem();
                ItemStack itemStack = player.getItemInHand();
                ItemMeta itemMeta = itemStack.getItemMeta();
                List<String> lore = itemMeta.getLore();
                lore.add("");
                lore.add(ChatColor.DARK_RED + "*MafanaMarket Rentables*");
                itemMeta.setLore(lore);
                itemStack.setItemMeta(itemMeta);
                itemStack = NBTUtils.setBoolean(itemStack, "RENTED", true);
                player.setItemInHand(null);
                player.getInventory().addItem(itemStack);
            }
            if(args[0].equalsIgnoreCase("open")) {
                player.openInventory(new MarketShopMenu(player).getInventory());
            }
        }
        return false;
    }
}
