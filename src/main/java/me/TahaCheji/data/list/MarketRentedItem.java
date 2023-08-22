package me.TahaCheji.data.list;

import me.TahaCheji.MafanaMarketRentables;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class MarketRentedItem {

    private OfflinePlayer player;

    private OfflinePlayer seller;
    private ItemStack itemStack;
    private int days;

    private UUID uuid;

    public MarketRentedItem(OfflinePlayer player, OfflinePlayer seller, ItemStack itemStack, int days) {
        this.player = player;
        this.seller = seller;
        this.itemStack = itemStack;
        this.days = days;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public void setRentedItem() {
        MafanaMarketRentables.getInstance().getRentedItemData().setMarketRentedItem(this);
    }

    public UUID getUuid() {
        return uuid;
    }

    public OfflinePlayer getSeller() {
        return seller;
    }

    public int getDays() {
        return days;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public OfflinePlayer getPlayer() {
        return player;
    }
}
