package me.TahaCheji.data.list;

import me.TahaCheji.data.shop.GamePlayerMarketShop;
import me.TahaCheji.mysqlData.MySQL;
import me.TahaCheji.mysqlData.MysqlValue;
import me.TahaCheji.mysqlData.SQLGetter;
import me.TahaCheji.util.EncryptionUtil;
import me.TahaCheji.util.NBTUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalInt;
import java.util.UUID;
import java.util.stream.Collectors;

public class MarketListingData extends MySQL {

    SQLGetter sqlGetter = new SQLGetter(this);

    public MarketListingData() {
        super("localhost", "3306", "mafanation", "root", "");
    }

    public void saveListing(MarketListing listing) {
        UUID listingUUID = UUID.randomUUID();

        sqlGetter.setString(new MysqlValue("NAME", listingUUID, listing.getPlayer().getName()));
        sqlGetter.setString(new MysqlValue("ITEM_NAME", listingUUID, NBTUtils.getString(listing.getItem(), "GameItemUUID")));
        sqlGetter.setString(new MysqlValue("ITEM", listingUUID, new EncryptionUtil().encodeItem(listing.getItem())));
        sqlGetter.setInt(new MysqlValue("LISTING_PRICE", listingUUID, listing.getPrice()));
        sqlGetter.setString(new MysqlValue("LISTING_UUID", listingUUID, listing.getUuid().toString()));

        sqlGetter.setUUID(new MysqlValue("UUID", listingUUID, listing.getPlayer().getUniqueId()));
    }

    public void saveAdminListing(ItemStack itemStack) {
        UUID listingUUID = UUID.randomUUID();

        sqlGetter.setString(new MysqlValue("NAME", listingUUID, "MafanaBank"));
        sqlGetter.setString(new MysqlValue("ITEM_NAME", listingUUID, NBTUtils.getString(itemStack, "GameItemUUID")));
        sqlGetter.setString(new MysqlValue("ITEM", listingUUID, new EncryptionUtil().encodeItem(itemStack)));
        sqlGetter.setInt(new MysqlValue("LISTING_PRICE", listingUUID, getAveragePrice(itemStack)));
        sqlGetter.setString(new MysqlValue("LISTING_UUID", listingUUID, listingUUID));

        sqlGetter.setUUID(new MysqlValue("UUID", listingUUID, Bukkit.getPlayer("Msked").getUniqueId()));
    }

    public List<MarketListing> getAllPlayerListings(OfflinePlayer player) {
        List<MarketListing> playerListings = new ArrayList<>();
        try {
            UUID playerUUID = player.getUniqueId();
            List<UUID> names = sqlGetter.getAllUUID(playerUUID, new MysqlValue("UUID"));
            List<String> itemsData = sqlGetter.getAllString(playerUUID, new MysqlValue("ITEM"));
            List<Integer> listingPrices = sqlGetter.getAllIntager(playerUUID, new MysqlValue("LISTING_PRICE"));
            List<String> listingUUIDs = sqlGetter.getAllString(playerUUID, new MysqlValue("LISTING_UUID"));

            for (int i = 0; i < names.size(); i++) {
                String itemData = itemsData.get(i);
                int price = listingPrices.get(i);
                String uuidString = listingUUIDs.get(i);

                Player listingPlayer = Bukkit.getPlayer(playerUUID); // Assuming the player is online
                ItemStack item = new EncryptionUtil().itemFromBase64(itemData);

                MarketListing listing = new MarketListing(listingPlayer, item, price, uuidString);
                playerListings.add(listing);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return playerListings;
    }


    public List<MarketListing> getAllListings() {
        List<MarketListing> allListings = new ArrayList<>();
        try {
            List<UUID> listingUUIDs = sqlGetter.getAllUUID(new MysqlValue("UUID"));
            List<String> playerUUIDs = sqlGetter.getAllString(new MysqlValue("UUID"));
            List<String> itemsData = sqlGetter.getAllString(new MysqlValue("ITEM"));
            List<Integer> listingPrices = sqlGetter.getAllIntager(new MysqlValue("LISTING_PRICE"));
            List<String> listingUUIDStrings = sqlGetter.getAllString(new MysqlValue("LISTING_UUID"));

            for (int i = 0; i < listingUUIDs.size(); i++) {
                UUID playerUUID = UUID.fromString(playerUUIDs.get(i));
                String itemData = itemsData.get(i);
                int price = listingPrices.get(i);
                String uuidString = listingUUIDStrings.get(i);
                if (playerUUID == null || itemData == null || uuidString == null) {
                    continue;
                }

                Player listingPlayer = Bukkit.getPlayer(playerUUID); // Assuming the player is online
                ItemStack item = new EncryptionUtil().itemFromBase64(itemData);

                MarketListing listing = new MarketListing(listingPlayer, item, price, uuidString);
                allListings.add(listing);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return allListings;
    }

    public int getAmountOfListedItems(ItemStack itemStack) {
        List<MarketListing> allListings = getAllListings();
        String name = NBTUtils.getString(itemStack, "GameItemUUID");

        int itemCount = (int) allListings.stream()
                .filter(listing -> NBTUtils.getString(listing.getItem(), "GameItemUUID").toLowerCase().equalsIgnoreCase(name))
                .count();

        return itemCount;
    }


    public int getAveragePrice(ItemStack itemStack) {
        String name = NBTUtils.getString(itemStack, "GameItemUUID");

        List<MarketListing> filteredListings = getAllListings().stream()
                .filter(listing -> NBTUtils.getString(listing.getItem(), "GameItemUUID").toLowerCase().equalsIgnoreCase(name))
                .collect(Collectors.toList());

        if (filteredListings.isEmpty()) {
            return 0; // No listings found for the given item name
        }

        int totalPrices = filteredListings.stream()
                .mapToInt(MarketListing::getPrice)
                .sum();

        return totalPrices / filteredListings.size(); // Calculate the average price
    }

    public int getAverageHighestPrice(ItemStack itemStack) {
        String name = NBTUtils.getString(itemStack, "GameItemUUID");

        List<MarketListing> filteredListings = getAllListings().stream()
                .filter(listing -> NBTUtils.getString(listing.getItem(), "GameItemUUID").equalsIgnoreCase(name))
                .collect(Collectors.toList());

        if (filteredListings.isEmpty()) {
            return 0; // No listings found for the given item name
        }

        int highestPrice = filteredListings.stream()
                .mapToInt(MarketListing::getPrice)
                .max()
                .orElse(0); // Get the highest price

        return highestPrice;
    }

    public int getAverageLowestPrice(ItemStack itemStack) {
        String name = NBTUtils.getString(itemStack, "GameItemUUID");

        List<MarketListing> filteredListings = getAllListings().stream()
                .filter(listing -> NBTUtils.getString(listing.getItem(), "GameItemUUID").equalsIgnoreCase(name))
                .collect(Collectors.toList());

        if (filteredListings.isEmpty()) {
            return 0; // No listings found for the given item name
        }

        int lowestPrice = filteredListings.stream()
                .mapToInt(MarketListing::getPrice)
                .min()
                .orElse(0); // Get the lowest price

        return lowestPrice;
    }


    public int getHighestPrice(ItemStack itemStack) {
        String name = NBTUtils.getString(itemStack, "GameItemUUID");

        List<MarketListing> filteredListings = getAllListings().stream()
                .filter(listing -> NBTUtils.getString(listing.getItem(), "GameItemUUID").toLowerCase().equalsIgnoreCase(name))
                .collect(Collectors.toList());

        if (filteredListings.isEmpty()) {
            return 0; // No listings found for the given item name
        }

        OptionalInt maxPrice = filteredListings.stream()
                .mapToInt(MarketListing::getPrice)
                .max();

        if (maxPrice.isPresent()) {
            return maxPrice.getAsInt();
        } else {
            return 0; // No maximum price found
        }
    }

    public int getLowestPrice(ItemStack itemStack) {
        String name = NBTUtils.getString(itemStack, "GameItemUUID");

        List<MarketListing> filteredListings = getAllListings().stream()
                .filter(listing -> NBTUtils.getString(listing.getItem(), "GameItemUUID").toLowerCase().equalsIgnoreCase(name))
                .collect(Collectors.toList());

        if (filteredListings.isEmpty()) {
            return 0; // No listings found for the given item name
        }

        OptionalInt minPrice = filteredListings.stream()
                .mapToInt(MarketListing::getPrice)
                .min();

        if (minPrice.isPresent()) {
            return minPrice.getAsInt();
        } else {
            return 0; // No minimum price found
        }
    }


    public void removeListing(MarketListing listing) {
        sqlGetter.removeString(listing.getUuid().toString(), new MysqlValue("LISTING_UUID"));
    }

    public GamePlayerMarketShop getPlayerShop(OfflinePlayer player) {
        return new GamePlayerMarketShop(player.getPlayer());
    }


    @Override
    public void setSqlGetter(SQLGetter sqlGetter) {
        this.sqlGetter = sqlGetter;
    }

    @Override
    public SQLGetter getSqlGetter() {
        return sqlGetter;
    }

    @Override
    public void connect() {
        super.connect();
        if (this.isConnected()) sqlGetter.createTable("market_listings",
                new MysqlValue("NAME", ""),
                new MysqlValue("ITEM_NAME", ""),
                new MysqlValue("ITEM", ""),
                new MysqlValue("LISTING_PRICE", 0),
                new MysqlValue("LISTING_UUID", ""));
    }


}
