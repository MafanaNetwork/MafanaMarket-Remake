package me.TahaCheji.event;

import de.tr7zw.nbtapi.NBTItem;
import me.TahaCheji.MafanaMarketRentables;
import me.TahaCheji.data.list.MarketRentedItem;
import me.TahaCheji.data.list.MarketRentedItemData;
import me.TahaCheji.events.PlayerJoin;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class UpdateRentedItemEvent implements Listener {


    @EventHandler
    public void onJoin(PlayerJoinEvent event) {

    }

    public void update(Player player) {
        MarketRentedItemData rentingData = MafanaMarketRentables.getInstance().getRentedItemData();
        for (MarketRentedItem marketRentedItem : rentingData.getPlayerRentedItems(player)) {
            for (ItemStack itemStack : player.getInventory().getContents()) {
                if (itemStack == null) {
                    continue;
                }
                if (itemStack.getItemMeta() == null) {
                    continue;
                }
                if (new NBTItem(itemStack).hasTag("GameWeaponUUID")) {
                    if (new NBTItem(itemStack).getString("GameWeaponUUID").equalsIgnoreCase(new NBTItem(marketRentedItem.getItemStack()).getString("GameWeaponUUID"))) {
                        ItemStack item = itemStack;
                        ItemMeta itemMeta = item.getItemMeta();
                        List<String>
                    }
                }
            }
        }
    }
}
