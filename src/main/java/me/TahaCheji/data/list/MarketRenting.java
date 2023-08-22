package me.TahaCheji.data.list;

import me.TahaCheji.MafanaMarketRentables;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class MarketRenting {

    private final OfflinePlayer player;
    private final ItemStack item;
    private final int price;

    private final int days;
    private final UUID uuid;

    public MarketRenting(OfflinePlayer player, ItemStack item, int price, int days) {
        this.player = player;
        this.item = item;
        this.price = price;
        this.days = days;
        UUID uuid = UUID.randomUUID();
        this.uuid = uuid;
    }

    public MarketRenting(OfflinePlayer player, ItemStack item, int price, int days, String uuid) {
        this.player = player;
        this.item = item;
        this.price = price;
        this.days = days;
        this.uuid = UUID.fromString(uuid);
    }

    public void saveListing(){
        MafanaMarketRentables.getInstance().getListingData().saveRentedListing(this);
    }


    public void setPlayerListing(Player player) {
        player.sendMessage(ChatColor.GOLD + "You have listed " + item.getItemMeta().getDisplayName() + ChatColor.GOLD  + " for a price of $" + price +" for " + days + " days.");
    }

    public void removeListing() {
        MafanaMarketRentables.getInstance().getListingData().removeRentedListing(this);
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

    public int getDays() {
        return days;
    }
}
