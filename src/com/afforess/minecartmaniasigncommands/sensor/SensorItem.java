package com.afforess.minecartmaniasigncommands.sensor;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.block.Sign;
import org.bukkit.inventory.ItemStack;

import com.afforess.minecartmaniacore.utils.ItemMatcher;
import com.afforess.minecartmaniacore.world.AbstractItem;
import com.afforess.minecartmaniacore.world.SpecificMaterial;
import com.afforess.minecartmaniacore.minecart.MinecartManiaMinecart;
import com.afforess.minecartmaniacore.entity.MinecartManiaPlayer;
import com.afforess.minecartmaniacore.minecart.MinecartManiaStorageCart;
import com.afforess.minecartmaniacore.world.MinecartManiaWorld;

public class SensorItem extends GenericSensor{
	private List<ItemMatcher> detect = new ArrayList<ItemMatcher>();

	private static final long serialVersionUID = 4941223452342L;
	public SensorItem(SensorType type, Sign sign, String name, List<ItemMatcher> list) {
		super(type, sign, name);
		this.detect = list;
	}

	public void input(MinecartManiaMinecart minecart) {
        boolean state = false;
        if (minecart != null) {
            for (ItemMatcher matcher : detect) {
                int n = 0;
                int amountNeeded = matcher.getAmount(0);
                if (minecart.isStorageMinecart()) {
                    MinecartManiaStorageCart cart = ((MinecartManiaStorageCart) minecart);
                    
                    for (int i = 0; i < cart.size(); i++) {
                        ItemStack item = cart.getItem(i);
                        if (matcher.match(item)) {
                            n++;
                        }
                    }
                } else if (minecart.hasPlayerPassenger()) {
                    MinecartManiaPlayer player = MinecartManiaWorld.getMinecartManiaPlayer(minecart.getPlayerPassenger());
                    
                    for (int i = 0; i < player.getInventory().getSize(); i++) {
                        ItemStack item = player.getInventory().getItem(i);
                        if (matcher.match(item)) {
                            n++;
                        }
                    }
                }
                if (n > (amountNeeded == -1 ? 0 : amountNeeded)) {
                    state = true;
                } else {
                    state = false;
                    break;
                }
            }
        }
        setState(state);
	}
}
