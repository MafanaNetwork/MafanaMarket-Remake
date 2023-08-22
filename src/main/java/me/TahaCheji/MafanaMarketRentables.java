package me.TahaCheji;

import me.TahaCheji.command.AdminCommand;
import me.TahaCheji.data.list.MarketRentedItemData;
import me.TahaCheji.data.list.MarketRentingData;
import me.TahaCheji.data.list.MarketTransactionData;
import org.bukkit.ChatColor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.reflections.Reflections;

import java.lang.reflect.InvocationTargetException;

public final class MafanaMarketRentables extends JavaPlugin {

    private static MafanaMarketRentables instance;
    private final MarketRentingData listingData = new MarketRentingData();
    private final MarketRentedItemData rentedItemData = new MarketRentedItemData();
    private final MarketTransactionData transactionData = new MarketTransactionData();

    @Override
    public void onEnable() {
        System.out.println(ChatColor.GOLD + "Starting: MafanaMarketRentables");
        instance = this;

        // Connect to the database for both MarketListingData and MarketTransactionData
        listingData.connect();
        transactionData.connect();
        rentedItemData.connect();

        String packageName = getClass().getPackage().getName();
        for (Class<?> clazz : new Reflections(packageName, ".listeners").getSubTypesOf(Listener.class)) {
            try {
                Listener listener = (Listener) clazz.getDeclaredConstructor().newInstance();
                getServer().getPluginManager().registerEvents(listener, this);
            } catch (InvocationTargetException | InstantiationException | IllegalAccessException | NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
        getCommand("mmr").setExecutor(new AdminCommand());
    }


    @Override
    public void onDisable() {
        listingData.disconnect();
        transactionData.disconnect();
        rentedItemData.disconnect();
        System.out.println(ChatColor.RED + "Turning off: MafanaMarketRentables");
    }

    public MarketRentedItemData getRentedItemData() {
        return rentedItemData;
    }

    public MarketRentingData getListingData() {
        return listingData;
    }

    public static MafanaMarketRentables getInstance() {
        return instance;
    }

    public MarketTransactionData getTransactionData() {
        return transactionData;
    }
}
