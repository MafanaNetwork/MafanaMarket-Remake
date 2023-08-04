package me.TahaCheji.data.list;

import me.TahaCheji.MafanaMarket;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class MarketListing {

    private final OfflinePlayer player;
    private final ItemStack item;
    private final int price;
    private final UUID uuid;

    public MarketListing(OfflinePlayer player, ItemStack item, int price) {
        this.player = player;
        this.item = item;
        this.price = price;
        UUID uuid = UUID.randomUUID();
        this.uuid = uuid;
    }

    public MarketListing(OfflinePlayer player, ItemStack item, int price, String uuid) {
        this.player = player;
        this.item = item;
        this.price = price;
        this.uuid = UUID.fromString(uuid);
    }

    public void saveListing(){
        MafanaMarket.getInstance().getListingData().saveListing(this);
    }


    public void setPlayerListing(Player player) {
        player.sendMessage(ChatColor.GOLD + "You have listed " + item.getItemMeta().getDisplayName() + ChatColor.GOLD  + " for a price of $" + price);
    }

    public void removeListing() {
        MafanaMarket.getInstance().getListingData().removeListing(this);
    }


    public UUID getUuid() {
        return uuid;
    }


    public OfflinePlayer getOfflinePlayer() {
        return player;
    }
    public Player getPlayer() {
        return player.getPlayer();
    }

    public ItemStack getItem() {
        return item;
    }

    public int getPrice() {
        return price;
    }

}
