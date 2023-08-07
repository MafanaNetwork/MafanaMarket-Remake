package me.TahaCheji;

import me.TahaCheji.command.MainCommand;
import me.TahaCheji.data.list.MarketListingData;
import me.TahaCheji.data.list.MarketTransactionData;
import org.bukkit.ChatColor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.reflections.Reflections;

import java.lang.reflect.InvocationTargetException;

public final class MafanaMarket extends JavaPlugin {

    private static MafanaMarket instance;
    private final MarketListingData listingData = new MarketListingData();
    private final MarketTransactionData transactionData = new MarketTransactionData();

    @Override
    public void onEnable() {
        System.out.println(ChatColor.GOLD + "Starting: MafanaMarket");
        instance = this;

        // Connect to the database for both MarketListingData and MarketTransactionData
        listingData.connect();
        transactionData.connect();

        String packageName = getClass().getPackage().getName();
        for (Class<?> clazz : new Reflections(packageName, ".listeners").getSubTypesOf(Listener.class)) {
            try {
                Listener listener = (Listener) clazz.getDeclaredConstructor().newInstance();
                getServer().getPluginManager().registerEvents(listener, this);
            } catch (InvocationTargetException | InstantiationException | IllegalAccessException | NoSuchMethodException e) {
                e.printStackTrace();
            }
        }

        getCommand("MafanaMarket").setExecutor(new MainCommand());
    }


    @Override
    public void onDisable() {
        listingData.disconnect();
        transactionData.disconnect();
        System.out.println(ChatColor.RED + "Turning off: MafanaMarket");
    }

    public MarketListingData getListingData() {
        return listingData;
    }

    public static MafanaMarket getInstance() {
        return instance;
    }

    public MarketTransactionData getTransactionData() {
        return transactionData;
    }
}
