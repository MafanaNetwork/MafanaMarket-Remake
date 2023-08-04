package me.TahaCheji.command;

import me.TahaCheji.MafanaMarket;
import me.TahaCheji.data.list.MarketListing;
import me.TahaCheji.data.market.ItemType;
import me.TahaCheji.data.market.MarketShop_GUI;
import me.TahaCheji.data.menu.MarketShopMenu;
import me.TahaCheji.data.shop.GamePlayerMarketShop;
import me.TahaCheji.data.shop.GamePlayerMarketShop_GUI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class MainCommand implements CommandExecutor {


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (label.equalsIgnoreCase("MafanaMarket")) {
            Player player = (Player) sender;
            if(args[0].equalsIgnoreCase("admin")) {
                if(player.isOp()) {
                    MafanaMarket.getInstance().getListingData().saveAdminListing(player.getItemInHand());
                }
                return true;
            }
            if(args[0].equalsIgnoreCase("price")) {
                if(player.getItemInHand() != null && args.length == 1) {
                    player.sendMessage(ChatColor.GOLD + "MafanaMarket: " + ChatColor.WHITE + "The average price of this item: $" + MafanaMarket.getInstance().getListingData().getAveragePrice(player.getItemInHand()));
                    return true;
                } else if(args[1].equalsIgnoreCase("High")) {
                    player.sendMessage(ChatColor.GOLD + "MafanaMarket: " + ChatColor.WHITE + "The highest price of this item: $" + MafanaMarket.getInstance().getListingData().getHighestPrice(player.getItemInHand()));
                    return true;
                } else if (args[1].equalsIgnoreCase("Lowest")) {
                    player.sendMessage(ChatColor.GOLD + "MafanaMarket: " + ChatColor.WHITE + "The lowest price of this item: $" + MafanaMarket.getInstance().getListingData().getLowestPrice(player.getItemInHand()));
                }
                return true;
            }
            if(args[0].equalsIgnoreCase("priceSold")) {
                if(player.getItemInHand() != null && args.length == 1) {
                    player.sendMessage(ChatColor.GOLD + "MafanaMarket: " + ChatColor.WHITE + "The average price of this item sold: $" + MafanaMarket.getInstance().getTransactionData().getAverageSoldPrice(player.getItemInHand()));
                    return true;
                } else if(args[1].equalsIgnoreCase("High")) {
                    player.sendMessage(ChatColor.GOLD + "MafanaMarket: " + ChatColor.WHITE + "The highest price of this item sold: $" + MafanaMarket.getInstance().getTransactionData().getHighestSoldPrice(player.getItemInHand()));
                    return true;
                } else if (args[1].equalsIgnoreCase("Lowest")) {
                    player.sendMessage(ChatColor.GOLD + "MafanaMarket: " + ChatColor.WHITE + "The lowest price of this item sold: $" + MafanaMarket.getInstance().getTransactionData().getLowestSoldPrice(player.getItemInHand()));
                }
                return true;
            }
            if (args[0].equalsIgnoreCase("menu")) {
                player.openInventory(new MarketShopMenu(player).getInventory());
                return true;
            }
            if (args[0].equalsIgnoreCase("all")) {
                if (args.length == 2) {
                    new MarketShop_GUI().getMarketShopGui().open(player);
                    return true;
                }
                if (args[1].equalsIgnoreCase("sword")) {
                    new MarketShop_GUI().getMarketShopGui(ItemType.SWORD).open(player);
                }
                if (args[1].equalsIgnoreCase("material")) {
                    new MarketShop_GUI().getMarketShopGui(ItemType.MATERIAL).open(player);
                }
                if (args[1].equalsIgnoreCase("tool")) {
                    new MarketShop_GUI().getMarketShopGui(ItemType.TOOL).open(player);
                }
                if (args[1].equalsIgnoreCase("bow")) {
                    new MarketShop_GUI().getMarketShopGui(ItemType.BOW).open(player);
                }
                if (args[1].equalsIgnoreCase("item")) {
                    new MarketShop_GUI().getMarketShopGui(ItemType.ITEM).open(player);
                }
                if (args[1].equalsIgnoreCase("armor")) {
                    new MarketShop_GUI().getMarketShopGui(ItemType.ARMOR).open(player);
                }
                if (args[1].equalsIgnoreCase("boots")) {
                    new MarketShop_GUI().getMarketShopGui(ItemType.BOOTS).open(player);
                }
                if (args[1].equalsIgnoreCase("leggings")) {
                    new MarketShop_GUI().getMarketShopGui(ItemType.LEGGGINGS).open(player);
                }
                if (args[1].equalsIgnoreCase("chestplate")) {
                    new MarketShop_GUI().getMarketShopGui(ItemType.CHESTPLATE).open(player);
                }
                if (args[1].equalsIgnoreCase("spell")) {
                    new MarketShop_GUI().getMarketShopGui(ItemType.SPELL).open(player);
                }
                if (args[1].equalsIgnoreCase("helmet")) {
                    new MarketShop_GUI().getMarketShopGui(ItemType.HELMET).open(player);
                }
            }
            if(args[0].equalsIgnoreCase("shop")) {
                Player newPlayer = Bukkit.getPlayer(args[1]);
                if (newPlayer == null) {
                    return true;
                }
                if (MafanaMarket.getInstance().getListingData().getAllPlayerListings(newPlayer) != null) {
                    player.openInventory(new GamePlayerMarketShop_GUI(MafanaMarket.getInstance().getListingData().getPlayerShop(newPlayer), player).getInventory());
                } else {
                    player.sendMessage(ChatColor.GOLD + "MafanaMarket: " + ChatColor.RED + "That player does not have a shop");
                }
            }
            if (args[0].equalsIgnoreCase("create")) {
                if (args[1].equalsIgnoreCase("listing")) {
                    if (args.length == 2) {
                        return true;
                    }
                    if (MafanaMarket.getInstance().getListingData().getPlayerShop(player).getItems().size() == 28) {
                        player.sendMessage(ChatColor.GOLD + "MafanaMarket: " + ChatColor.RED + "You hit the max amount of listings");
                        return true;
                    }
                    int coins = Integer.parseInt(args[2]);
                    ItemStack item = player.getItemInHand();
                    MarketListing listing = new MarketListing(player, item, coins);
                    player.setItemInHand(new ItemStack(Material.AIR));
                    GamePlayerMarketShop shop = MafanaMarket.getInstance().getListingData().getPlayerShop(player);
                    shop.saveListing(listing);
                    player.sendMessage(ChatColor.GOLD + "MafanaMarket: " + ChatColor.WHITE + "You have created a listing");
                }
            }
        }
        return false;
    }
}
