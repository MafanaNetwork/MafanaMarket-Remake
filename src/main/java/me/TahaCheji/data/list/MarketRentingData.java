package me.TahaCheji.data.list;
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

public class MarketRentingData extends MySQL {

    SQLGetter sqlGetter = new SQLGetter(this);

    public MarketRentingData() {
        super("localhost", "3306", "mafanation", "root", "");
    }

    public void saveRentedListing(MarketRenting listing) {
        UUID listingUUID = UUID.randomUUID();

        sqlGetter.setString(new MysqlValue("NAME", listingUUID, listing.getPlayer().getName()));
        sqlGetter.setString(new MysqlValue("ITEM_NAME", listingUUID, NBTUtils.getString(listing.getItem(), "GameItemUUID")));
        sqlGetter.setString(new MysqlValue("ITEM", listingUUID, new EncryptionUtil().encodeItem(listing.getItem())));
        sqlGetter.setInt(new MysqlValue("RENTED_PRICE", listingUUID, listing.getPrice()));
        sqlGetter.setInt(new MysqlValue("RENTED_DAYS", listingUUID, listing.getDays()));
        sqlGetter.setString(new MysqlValue("LISTING_UUID", listingUUID, listing.getUuid().toString()));

        sqlGetter.setUUID(new MysqlValue("UUID", listingUUID, listing.getPlayer().getUniqueId()));
    }

    public void saveAdminRentedListing(ItemStack itemStack) {
        UUID listingUUID = UUID.randomUUID();

        sqlGetter.setString(new MysqlValue("NAME", listingUUID, "MafanaBank"));
        sqlGetter.setString(new MysqlValue("ITEM_NAME", listingUUID, NBTUtils.getString(itemStack, "GameItemUUID")));
        sqlGetter.setString(new MysqlValue("ITEM", listingUUID, new EncryptionUtil().encodeItem(itemStack)));
        sqlGetter.setInt(new MysqlValue("RENTED_PRICE", listingUUID, getAveragePrice(itemStack)));
        sqlGetter.setInt(new MysqlValue("RENTED_DAYS", listingUUID, 0));
        sqlGetter.setString(new MysqlValue("LISTING_UUID", listingUUID, listingUUID));

        sqlGetter.setUUID(new MysqlValue("UUID", listingUUID, Bukkit.getPlayer("Msked").getUniqueId()));
    }

    public List<MarketRenting> getAllPlayerListings(OfflinePlayer player) {
        List<MarketRenting> playerListings = new ArrayList<>();
        try {
            UUID playerUUID = player.getUniqueId();
            List<UUID> names = sqlGetter.getAllUUID(playerUUID, new MysqlValue("UUID"));
            List<String> itemsData = sqlGetter.getAllString(playerUUID, new MysqlValue("ITEM"));
            List<Integer> listingPrices = sqlGetter.getAllIntager(playerUUID, new MysqlValue("RENTED_PRICE"));
            List<String> listingUUIDs = sqlGetter.getAllString(playerUUID, new MysqlValue("LISTING_UUID"));
            List<Integer> listingDays = sqlGetter.getAllIntager(playerUUID, new MysqlValue("RENTED_DAYS"));

            for (int i = 0; i < names.size(); i++) {
                String itemData = itemsData.get(i);
                int price = listingPrices.get(i);
                int days = listingDays.get(i);
                String uuidString = listingUUIDs.get(i);

                Player listingPlayer = Bukkit.getPlayer(playerUUID); // Assuming the player is online
                ItemStack item = new EncryptionUtil().itemFromBase64(itemData);

                MarketRenting listing = new MarketRenting(listingPlayer, item, price, days, uuidString);
                playerListings.add(listing);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return playerListings;
    }


    public List<MarketRenting> getAllListings() {
        List<MarketRenting> allListings = new ArrayList<>();
        try {
            List<UUID> listingUUIDs = sqlGetter.getAllUUID(new MysqlValue("UUID"));
            List<String> playerUUIDs = sqlGetter.getAllString(new MysqlValue("UUID"));
            List<String> itemsData = sqlGetter.getAllString(new MysqlValue("ITEM"));
            List<Integer> listingPrices = sqlGetter.getAllIntager(new MysqlValue("RENTED_PRICE"));
            List<String> listingUUIDStrings = sqlGetter.getAllString(new MysqlValue("LISTING_UUID"));
            List<Integer> listingDays = sqlGetter.getAllIntager(new MysqlValue("RENTED_DAYS"));

            for (int i = 0; i < listingUUIDs.size(); i++) {
                UUID playerUUID = UUID.fromString(playerUUIDs.get(i));
                String itemData = itemsData.get(i);
                int price = listingPrices.get(i);
                int days = listingDays.get(i);
                String uuidString = listingUUIDStrings.get(i);
                if (playerUUID == null || itemData == null || uuidString == null) {
                    continue;
                }

                Player listingPlayer = Bukkit.getPlayer(playerUUID); // Assuming the player is online
                ItemStack item = new EncryptionUtil().itemFromBase64(itemData);

                MarketRenting listing = new MarketRenting(listingPlayer, item, price,days, uuidString);
                allListings.add(listing);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return allListings;
    }

    public int getAmountOfListedItems(ItemStack itemStack) {
        List<MarketRenting> allListings = getAllListings();
        String name = NBTUtils.getString(itemStack, "GameItemUUID");

        int itemCount = (int) allListings.stream()
                .filter(listing -> NBTUtils.getString(listing.getItem(), "GameItemUUID").toLowerCase().equalsIgnoreCase(name))
                .count();

        return itemCount;
    }


    public int getAveragePrice(ItemStack itemStack) {
        String name = NBTUtils.getString(itemStack, "GameItemUUID");

        List<MarketRenting> filteredListings = getAllListings().stream()
                .filter(listing -> NBTUtils.getString(listing.getItem(), "GameItemUUID").toLowerCase().equalsIgnoreCase(name))
                .collect(Collectors.toList());

        if (filteredListings.isEmpty()) {
            return 0; // No listings found for the given item name
        }

        int totalPrices = filteredListings.stream()
                .mapToInt(MarketRenting::getPrice)
                .sum();

        return totalPrices / filteredListings.size(); // Calculate the average price
    }

    public int getAverageHighestPrice(ItemStack itemStack) {
        String name = NBTUtils.getString(itemStack, "GameItemUUID");

        List<MarketRenting> filteredListings = getAllListings().stream()
                .filter(listing -> NBTUtils.getString(listing.getItem(), "GameItemUUID").equalsIgnoreCase(name))
                .collect(Collectors.toList());

        if (filteredListings.isEmpty()) {
            return 0; // No listings found for the given item name
        }

        int highestPrice = filteredListings.stream()
                .mapToInt(MarketRenting::getPrice)
                .max()
                .orElse(0); // Get the highest price

        return highestPrice;
    }

    public int getAverageLowestPrice(ItemStack itemStack) {
        String name = NBTUtils.getString(itemStack, "GameItemUUID");

        List<MarketRenting> filteredListings = getAllListings().stream()
                .filter(listing -> NBTUtils.getString(listing.getItem(), "GameItemUUID").equalsIgnoreCase(name))
                .collect(Collectors.toList());

        if (filteredListings.isEmpty()) {
            return 0; // No listings found for the given item name
        }

        int lowestPrice = filteredListings.stream()
                .mapToInt(MarketRenting::getPrice)
                .min()
                .orElse(0); // Get the lowest price

        return lowestPrice;
    }


    public int getHighestPrice(ItemStack itemStack) {
        String name = NBTUtils.getString(itemStack, "GameItemUUID");

        List<MarketRenting> filteredListings = getAllListings().stream()
                .filter(listing -> NBTUtils.getString(listing.getItem(), "GameItemUUID").toLowerCase().equalsIgnoreCase(name))
                .collect(Collectors.toList());

        if (filteredListings.isEmpty()) {
            return 0; // No listings found for the given item name
        }

        OptionalInt maxPrice = filteredListings.stream()
                .mapToInt(MarketRenting::getPrice)
                .max();

        if (maxPrice.isPresent()) {
            return maxPrice.getAsInt();
        } else {
            return 0; // No maximum price found
        }
    }

    public int getLowestPrice(ItemStack itemStack) {
        String name = NBTUtils.getString(itemStack, "GameItemUUID");

        List<MarketRenting> filteredListings = getAllListings().stream()
                .filter(listing -> NBTUtils.getString(listing.getItem(), "GameItemUUID").toLowerCase().equalsIgnoreCase(name))
                .collect(Collectors.toList());

        if (filteredListings.isEmpty()) {
            return 0; // No listings found for the given item name
        }

        OptionalInt minPrice = filteredListings.stream()
                .mapToInt(MarketRenting::getPrice)
                .min();

        if (minPrice.isPresent()) {
            return minPrice.getAsInt();
        } else {
            return 0; // No minimum price found
        }
    }


    public void removeRentedListing(MarketRenting listing) {
        sqlGetter.removeString(listing.getUuid().toString(), new MysqlValue("LISTING_UUID"));
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
        if (this.isConnected()) sqlGetter.createTable("market_rentables",
                new MysqlValue("NAME", ""),
                new MysqlValue("ITEM_NAME", ""),
                new MysqlValue("ITEM", ""),
                new MysqlValue("RENTED_PRICE", 0),
                new MysqlValue("RENTED_DAYS", 0),
                new MysqlValue("LISTING_UUID", ""));
    }


}
