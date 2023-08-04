package me.TahaCheji.data.shop;

import me.TahaCheji.MafanaMarket;
import me.TahaCheji.data.list.MarketListing;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public class GamePlayerMarketShop {

    private final Player owner;
    private final UUID uuid;

    public GamePlayerMarketShop(Player owner) {
        this.owner = owner;
        this.uuid = owner.getUniqueId();
    }

    public void saveListing(MarketListing listing){
        MafanaMarket.getInstance().getListingData().saveListing(listing);
    }

    public Player getOwner() {
        return owner;
    }

    public List<MarketListing> getItems() {
        return MafanaMarket.getInstance().getListingData().getAllPlayerListings(owner);
    }

    public UUID getUuid() {
        return uuid;
    }
}
