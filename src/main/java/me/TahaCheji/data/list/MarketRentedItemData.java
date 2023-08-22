package me.TahaCheji.data.list;

import de.tr7zw.nbtapi.NBTItem;
import me.TahaCheji.GamePlayerInventory;
import me.TahaCheji.Inv;
import me.TahaCheji.data.market.ItemType;
import me.TahaCheji.mysqlData.MySQL;
import me.TahaCheji.mysqlData.MysqlValue;
import me.TahaCheji.mysqlData.SQLGetter;
import me.TahaCheji.util.EncryptionUtil;
import me.TahaCheji.util.NBTUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class MarketRentedItemData extends MySQL {

    SQLGetter sqlGetter = new SQLGetter(this);
    public MarketRentedItemData() {
        super("localhost", "3306", "mafanation", "root", "");
    }


    public void setMarketRentedItem(MarketRentedItem marketRentedItem) {
        UUID uuid = UUID.randomUUID();
        marketRentedItem.setUuid(uuid);
        sqlGetter.setString(new MysqlValue("NAME", uuid, marketRentedItem.getPlayer().getName()));
        sqlGetter.setInt(new MysqlValue("DAYS_LEFT", uuid, marketRentedItem.getDays()));
        sqlGetter.setString(new MysqlValue("ITEM_NAME", uuid, NBTUtils.getString(marketRentedItem.getItemStack(), "GameItemUUID")));
        sqlGetter.setString(new MysqlValue("ITEM", uuid, new EncryptionUtil().encodeItem(marketRentedItem.getItemStack())));
        sqlGetter.setString(new MysqlValue("SELLER_NAME", uuid, marketRentedItem.getSeller().getName()));
        sqlGetter.setString(new MysqlValue("SELLER_UUID", uuid, marketRentedItem.getSeller().getUniqueId().toString()));

        sqlGetter.setString(new MysqlValue("RENT_UUID", uuid, uuid.toString()));


        sqlGetter.setUUID(new MysqlValue("UUID", uuid, marketRentedItem.getPlayer().getUniqueId()));
    }

    public List<MarketRentedItem> getPlayerRentedItems(OfflinePlayer player) {
        List<MarketRentedItem> playerListings = new ArrayList<>();
        try {
            UUID playerUUID = player.getUniqueId();
            List<UUID> names = sqlGetter.getAllUUID(playerUUID, new MysqlValue("UUID"));
            List<String> itemsData = sqlGetter.getAllString(playerUUID, new MysqlValue("ITEM"));
            List<Integer> listingDays = sqlGetter.getAllIntager(playerUUID, new MysqlValue("DAYS_LEFT"));
            List<UUID> sellerUuid = sqlGetter.getAllUUID(playerUUID, new MysqlValue("SELLER_UUID"));
            List<UUID> rentUUID = sqlGetter.getAllUUID(playerUUID, new MysqlValue("RENT_UUID"));

            for (int i = 0; i < names.size(); i++) {
                if(itemsData.get(i) == null) {
                    continue;
                }
                String itemData = itemsData.get(i);
                int listingDay = listingDays.get(i);
                UUID uuid = rentUUID.get(i);
                OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(sellerUuid.get(i));

                OfflinePlayer listingPlayer = Bukkit.getOfflinePlayer(playerUUID); // Assuming the player is online
                ItemStack item = new EncryptionUtil().itemFromBase64(itemData);

                MarketRentedItem listing = new MarketRentedItem(listingPlayer,offlinePlayer, item, listingDay);
                listing.setUuid(uuid);
                playerListings.add(listing);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return playerListings;
    }

    public List<MarketRentedItem> getAllPlayerRentedItems() {
        List<MarketRentedItem> allListings = new ArrayList<>();
        try {
            List<UUID> listingUUIDs = sqlGetter.getAllUUID(new MysqlValue("UUID"));
            List<String> itemsData = sqlGetter.getAllString(new MysqlValue("ITEM"));
            List<String> playerUUIDs = sqlGetter.getAllString(new MysqlValue("UUID"));
            List<Integer> listingDays = sqlGetter.getAllIntager(new MysqlValue("DAYS_LEFT"));
            List<String> sellerUuid = sqlGetter.getAllString(new MysqlValue("SELLER_UUID"));
            List<String> rentUUIDs = sqlGetter.getAllString(new MysqlValue("RENT_UUID"));

            for (int i = 0; i < listingUUIDs.size(); i++) {
                if(sellerUuid.get(i) == null) {
                    continue;
                }
                UUID playerUUID = UUID.fromString(playerUUIDs.get(i));
                String itemData = itemsData.get(i);
                int listingDay = listingDays.get(i);
                UUID rentUUID = UUID.fromString(rentUUIDs.get(i));
                OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(UUID.fromString(sellerUuid.get(i)));

                OfflinePlayer listingPlayer = Bukkit.getOfflinePlayer(playerUUID); // Assuming the player is online
                ItemStack item = new EncryptionUtil().itemFromBase64(itemData);

                MarketRentedItem listing = new MarketRentedItem(listingPlayer, offlinePlayer, item, listingDay);
                listing.setUuid(rentUUID);
                allListings.add(listing);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return allListings;
    }


    public int getDaysLeft(MarketRentedItem marketRenting) {
        return sqlGetter.getInt(marketRenting.getUuid(), new MysqlValue("DAYS_LEFT"));
    }

    public void setDaysLeft(MarketRentedItem marketRenting, int i) {
        sqlGetter.setInt(new MysqlValue("DAYS_LEFT", marketRenting.getUuid(), i));
    }


    public boolean hasRentedItem(OfflinePlayer player) {
        return sqlGetter.exists(player.getUniqueId());
    }

    public List<ItemStack> getAllRentedItems(OfflinePlayer player) throws Exception {
        List<ItemStack> rentedItems = new ArrayList<>();
        List<String> itemUUIDs = sqlGetter.getAllString(player.getUniqueId(), new MysqlValue("ITEM_NAME"));

        for (String itemUUID : itemUUIDs) {
            String encodedItem = sqlGetter.getString(player.getUniqueId(), new MysqlValue("ITEM", itemUUID));
            ItemStack decodedItem = new EncryptionUtil().decodeItems(encodedItem);
            rentedItems.add(decodedItem);
        }

        return rentedItems;
    }

    public List<Player> getAllRentedPlayers(OfflinePlayer seller) {
        List<Player> rentedPlayers = new ArrayList<>();
        List<UUID> playerUUIDs = sqlGetter.getAllUUID(seller.getUniqueId(), new MysqlValue("SELLER_UUID"));

        for (UUID playerUUID : playerUUIDs) {
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(playerUUID);
            if (offlinePlayer instanceof Player) {
                rentedPlayers.add((Player) offlinePlayer);
            }
        }

        return rentedPlayers;
    }

    public void restoreRentedItem(MarketRentedItem marketRenting) throws Exception {
        if (hasRentedItem(marketRenting.getPlayer())) {
            Player player = marketRenting.getPlayer().getPlayer();
            ItemStack itemStack = marketRenting.getItemStack();
            if (new NBTItem(itemStack).getString("ItemType").equalsIgnoreCase(ItemType.LEGGGINGS.getLore()) || new NBTItem(itemStack).getString("ItemType").equalsIgnoreCase(ItemType.CHESTPLATE.getLore())
                    || new NBTItem(itemStack).getString("ItemType").equalsIgnoreCase(ItemType.HELMET.getLore()) || new NBTItem(itemStack).getString("ItemType").equalsIgnoreCase(ItemType.BOOTS.getLore())) {
                new GamePlayerInventory(Inv.getInstance(), marketRenting.getPlayer()).removeArmor(itemStack);
            } else {
            new GamePlayerInventory(Inv.getInstance(), player).removeItem(itemStack);
            }
            if (marketRenting.getSeller() != null) {
                OfflinePlayer seller = marketRenting.getSeller();
                if (seller != null && seller.isOnline()) {
                    new GamePlayerInventory(Inv.getInstance(), seller.getPlayer()).addItem(itemStack);
                } else {
                    new GamePlayerInventory(Inv.getInstance(), seller).addItem(itemStack);
                }
            }
            sqlGetter.removeString(marketRenting.getUuid().toString(), new MysqlValue("RENT_UUID"));
            sqlGetter.removeString(marketRenting.getUuid().toString(), new MysqlValue("UUID"));
        }
    }


    @Override
    public SQLGetter getSqlGetter() {
        return sqlGetter;
    }

    @Override
    public void connect() {
        super.connect();
        if (this.isConnected()) sqlGetter.createTable("market_rented_items",
                new MysqlValue("NAME", ""),
                new MysqlValue("DAYS_LEFT", 0),
                new MysqlValue("ITEM_NAME", ""),
                new MysqlValue("ITEM", ""),
                new MysqlValue("SELLER_NAME", ""),
                new MysqlValue("SELLER_UUID", ""),
                new MysqlValue("RENT_UUID","")
                );
    }
}
