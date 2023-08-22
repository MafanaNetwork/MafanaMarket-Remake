package me.TahaCheji.data.list;

import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;

import java.time.LocalDate;

public class MarketTransaction {
    private OfflinePlayer buyer;
    private OfflinePlayer seller;
    private String itemName;
    private ItemStack item;
    private int price;
    private int days;
    private String listingUUID;
    private LocalDate listingTime;

    public MarketTransaction(OfflinePlayer buyer, OfflinePlayer seller, String itemName, ItemStack item, int price, int days, String listingUUID, LocalDate listingTime) {
        this.buyer = buyer;
        this.seller = seller;
        this.itemName = itemName;
        this.item = item;
        this.days = days;
        this.price = price;
        this.listingUUID = listingUUID;
        this.listingTime = listingTime;
    }

    public OfflinePlayer getBuyer() {
        return buyer;
    }

    public OfflinePlayer getSeller() {
        return seller;
    }

    public String getItemName() {
        return itemName;
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

    public String getListingUUID() {
        return listingUUID;
    }

    public LocalDate getListingTime() {
        return listingTime;
    }
}
