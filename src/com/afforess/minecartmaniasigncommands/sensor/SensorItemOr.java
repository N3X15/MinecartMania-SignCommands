package com.afforess.minecartmaniasigncommands.sensor;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.block.Sign;
import org.bukkit.inventory.ItemStack;

import com.afforess.minecartmaniacore.entity.MinecartManiaPlayer;
import com.afforess.minecartmaniacore.minecart.MinecartManiaMinecart;
import com.afforess.minecartmaniacore.minecart.MinecartManiaStorageCart;
import com.afforess.minecartmaniacore.utils.ItemMatcher;
import com.afforess.minecartmaniacore.world.MinecartManiaWorld;

public class SensorItemOr extends GenericSensor {
    private List<ItemMatcher> detect = new ArrayList<ItemMatcher>();
    
    public SensorItemOr(final SensorType type, final Sign sign, final String name, final List<ItemMatcher> list) {
        super(type, sign, name);
        detect = list;
    }
    
    public void input(final MinecartManiaMinecart minecart) {
        boolean state = false;
        if (minecart != null) {
            for (final ItemMatcher matcher : detect) {
                int n = 0;
                final int amountNeeded = matcher.getAmount(0);
                if (minecart.isStorageMinecart()) {
                    final MinecartManiaStorageCart cart = ((MinecartManiaStorageCart) minecart);
                    
                    for (int i = 0; i < cart.size(); i++) {
                        final ItemStack item = cart.getItem(i);
                        if (matcher.match(item)) {
                            n++;
                        }
                    }
                } else if (minecart.hasPlayerPassenger()) {
                    final MinecartManiaPlayer player = MinecartManiaWorld.getMinecartManiaPlayer(minecart.getPlayerPassenger());
                    
                    for (int i = 0; i < player.getInventory().getSize(); i++) {
                        final ItemStack item = player.getInventory().getItem(i);
                        if (matcher.match(item)) {
                            n++;
                        }
                    }
                }
                if (n > (amountNeeded == -1 ? 0 : amountNeeded)) {
                    state = true;
                    break;
                }
            }
        }
        setState(state);
    }
}
