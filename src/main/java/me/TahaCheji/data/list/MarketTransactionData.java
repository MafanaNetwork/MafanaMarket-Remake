package me.TahaCheji.data.list;

import me.TahaCheji.mysqlData.MySQL;
import me.TahaCheji.mysqlData.MysqlValue;
import me.TahaCheji.mysqlData.SQLGetter;
import me.TahaCheji.util.EncryptionUtil;
import me.TahaCheji.util.NBTUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class MarketTransactionData extends MySQL {

    SQLGetter sqlGetter = new SQLGetter(this);

    public MarketTransactionData() {
        super("localhost", "3306", "mafanation", "root", "");
    }

    public void addListingTransaction(OfflinePlayer player, MarketListing listing) {
        UUID listingUUID = UUID.randomUUID();
        sqlGetter.setString(new MysqlValue("BUYER", listingUUID, player.getName()));
        sqlGetter.setString(new MysqlValue("SELLER_NAME", listingUUID, listing.getOfflinePlayer().getName()));
        sqlGetter.setString(new MysqlValue("SELLER_UUID", listingUUID, listing.getOfflinePlayer().getUniqueId()));
        sqlGetter.setString(new MysqlValue("SOLD_ITEM_NAME", listingUUID, NBTUtils.getString(listing.getItem(), "GameItemUUID")));
        sqlGetter.setString(new MysqlValue("SOLD_ITEM", listingUUID, new EncryptionUtil().encodeItem(listing.getItem())));
        sqlGetter.setInt(new MysqlValue("SOLD_LISTING_PRICE", listingUUID, listing.getPrice()));
        sqlGetter.setString(new MysqlValue("SOLD_LISTING_UUID", listingUUID, listing.getUuid()));
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        String formattedDate = currentDate.format(formatter);
        sqlGetter.setString(new MysqlValue("SOLD_LISTING_TIME", listingUUID, formattedDate));

        sqlGetter.setUUID(new MysqlValue("UUID", listingUUID, listing.getPlayer().getUniqueId()));
    }

    public List<String> getTransactions(OfflinePlayer player) {
        List<String> transactions = new ArrayList<>();

        try {
            UUID playerUUID = player.getUniqueId();
            List<String> buyers = sqlGetter.getAllString(playerUUID, new MysqlValue("BUYER"));
            List<String> sellers = sqlGetter.getAllString(playerUUID, new MysqlValue("SELLER_NAME"));
            List<String> itemNames = sqlGetter.getAllString(playerUUID, new MysqlValue("SOLD_ITEM_NAME"));
            List<Integer> prices = sqlGetter.getAllIntager(playerUUID, new MysqlValue("SOLD_LISTING_PRICE"));
            List<String> dates = sqlGetter.getAllString(playerUUID, new MysqlValue("SOLD_LISTING_TIME"));

            for (int i = 0; i < buyers.size(); i++) {
                String buyerName = buyers.get(i);
                String sellerName = sellers.get(i);
                String itemName = itemNames.get(i);
                int price = prices.get(i);
                String date = dates.get(i);

                String transactionString = String.format("Buyer: %s | Seller: %s | Item: %s | Price: %d | Date: %s", buyerName, sellerName, itemName, price, date);
                transactions.add(transactionString);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return transactions;
    }

    public List<MarketTransaction> getAllTransactions() {
        List<MarketTransaction> transactions = new ArrayList<>();
        try {
            List<String> buyers = sqlGetter.getAllString(new MysqlValue("BUYER"));
            List<String> sellerUUIDs = sqlGetter.getAllString(new MysqlValue("SELLER_UUID"));
            List<String> itemNames = sqlGetter.getAllString(new MysqlValue("SOLD_ITEM_NAME"));
            List<String> itemData = sqlGetter.getAllString(new MysqlValue("SOLD_ITEM"));
            List<Integer> prices = sqlGetter.getAllIntager(new MysqlValue("SOLD_LISTING_PRICE"));
            List<String> listingUUIDs = sqlGetter.getAllString(new MysqlValue("SOLD_LISTING_UUID"));
            List<String> listingTimes = sqlGetter.getAllString(new MysqlValue("SOLD_LISTING_TIME"));

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
            for (int i = 0; i < buyers.size(); i++) {
                OfflinePlayer buyer = Bukkit.getOfflinePlayer(UUID.fromString(buyers.get(i)));
                OfflinePlayer seller = Bukkit.getOfflinePlayer(UUID.fromString(sellerUUIDs.get(i)));
                String itemName = itemNames.get(i);
                ItemStack item = new EncryptionUtil().itemFromBase64(itemData.get(i));
                int price = prices.get(i);
                String uuidString = listingUUIDs.get(i);
                LocalDate time = LocalDate.parse(listingTimes.get(i), formatter);

                MarketTransaction transaction = new MarketTransaction(buyer, seller, itemName, item, price, uuidString, time);
                transactions.add(transaction);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return transactions;
    }

    public int getNumberOfTimesSold(ItemStack itemStack) {
        String name = NBTUtils.getString(itemStack, "GameItemUUID");

        return (int) getAllTransactions().stream()
                .filter(transaction -> NBTUtils.getString(transaction.getItem(), "GameItemUUID").equalsIgnoreCase(name))
                .count();
    }

    // Get the average price of sold items
    public int getAverageSoldPrice(ItemStack itemStack) {
        String name = NBTUtils.getString(itemStack, "GameItemUUID");

        List<Integer> soldPrices = getAllTransactions().stream()
                .filter(transaction -> NBTUtils.getString(transaction.getItem(), "GameItemUUID").equalsIgnoreCase(name))
                .map(MarketTransaction::getPrice)
                .collect(Collectors.toList());

        if (soldPrices.isEmpty()) {
            return 0; // No transactions found for the given item name
        }

        int totalPrices = soldPrices.stream()
                .mapToInt(Integer::intValue)
                .sum();

        return totalPrices / soldPrices.size(); // Calculate the average price
    }

    public int getAverageHighestSoldPrice(ItemStack itemStack) {
        String name = NBTUtils.getString(itemStack, "GameItemUUID");

        List<Integer> soldPrices = getAllTransactions().stream()
                .filter(transaction -> NBTUtils.getString(transaction.getItem(), "GameItemUUID").equalsIgnoreCase(name))
                .map(MarketTransaction::getPrice)
                .sorted(Comparator.reverseOrder()) // Sort the prices in descending order
                .collect(Collectors.toList());

        if (soldPrices.isEmpty()) {
            return 0; // No transactions found for the given item name
        }

        int totalPrices = soldPrices.stream()
                .mapToInt(Integer::intValue)
                .sum();

        return totalPrices / soldPrices.size(); // Calculate the average price
    }

    public int getAverageLowestSoldPrice(ItemStack itemStack) {
        String name = NBTUtils.getString(itemStack, "GameItemUUID");

        List<Integer> soldPrices = getAllTransactions().stream()
                .filter(transaction -> NBTUtils.getString(transaction.getItem(), "GameItemUUID").equalsIgnoreCase(name))
                .map(MarketTransaction::getPrice)
                .sorted() // Sort the prices in ascending order
                .collect(Collectors.toList());

        if (soldPrices.isEmpty()) {
            return 0; // No transactions found for the given item name
        }

        int totalPrices = soldPrices.stream()
                .mapToInt(Integer::intValue)
                .sum();

        return totalPrices / soldPrices.size(); // Calculate the average price
    }


    // Get the highest price of sold items
    public int getHighestSoldPrice(ItemStack itemStack) {
        String name = NBTUtils.getString(itemStack, "GameItemUUID");

        List<Integer> soldPrices = getAllTransactions().stream()
                .filter(transaction -> NBTUtils.getString(transaction.getItem(), "GameItemUUID").equalsIgnoreCase(name))
                .map(MarketTransaction::getPrice)
                .collect(Collectors.toList());

        if (soldPrices.isEmpty()) {
            return 0; // No transactions found for the given item name
        }

        OptionalInt maxPrice = soldPrices.stream()
                .mapToInt(Integer::intValue)
                .max();

        if (maxPrice.isPresent()) {
            return maxPrice.getAsInt();
        } else {
            return 0; // No maximum price found
        }
    }

    // Get the lowest price of sold items
    public int getLowestSoldPrice(ItemStack itemStack) {
        String name = NBTUtils.getString(itemStack, "GameItemUUID");

        List<Integer> soldPrices = getAllTransactions().stream()
                .filter(transaction -> NBTUtils.getString(transaction.getItem(), "GameItemUUID").equalsIgnoreCase(name))
                .map(MarketTransaction::getPrice)
                .collect(Collectors.toList());

        if (soldPrices.isEmpty()) {
            return 0; // No transactions found for the given item name
        }

        OptionalInt minPrice = soldPrices.stream()
                .mapToInt(Integer::intValue)
                .min();

        if (minPrice.isPresent()) {
            return minPrice.getAsInt();
        } else {
            return 0; // No minimum price found
        }
    }



    @Override
    public SQLGetter getSqlGetter() {
        return sqlGetter;
    }

    @Override
    public void connect() {
        super.connect();
        if (this.isConnected()) sqlGetter.createTable("market_transactions",
                new MysqlValue("BUYER", ""),
                new MysqlValue("SELLER_NAME", ""),
                new MysqlValue("SELLER_UUID", ""),
                new MysqlValue("SOLD_ITEM_NAME", ""),
                new MysqlValue("SOLD_ITEM", ""),
                new MysqlValue("SOLD_LISTING_PRICE", 0),
                new MysqlValue("SOLD_LISTING_UUID", ""),
                new MysqlValue("SOLD_LISTING_TIME", ""));
    }

}
