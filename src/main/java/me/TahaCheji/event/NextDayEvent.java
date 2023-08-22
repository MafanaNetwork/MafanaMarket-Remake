package me.TahaCheji.event;

import me.TahaCheji.MafanaMarketRentables;
import me.TahaCheji.data.list.MarketRentedItem;
import me.TahaCheji.data.list.MarketRentedItemData;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class NextDayEvent implements Listener {

    @EventHandler
    public void nextDayEvent(MSNextDayEvent event) throws Exception {
        MarketRentedItemData rentingData = MafanaMarketRentables.getInstance().getRentedItemData();
        for(MarketRentedItem marketRentedItem : rentingData.getAllPlayerRentedItems()) {
            if(marketRentedItem == null) {
                continue;
            }
            rentingData.setDaysLeft(marketRentedItem, rentingData.getDaysLeft(marketRentedItem) - 1);
            if(rentingData.getDaysLeft(marketRentedItem) <= 0) {
                rentingData.restoreRentedItem(marketRentedItem);
            }
        }
    }
}
