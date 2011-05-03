package com.afforess.minecartmaniasigncommands.sensor;

import org.bukkit.block.Sign;

import com.afforess.minecartmaniacore.world.AbstractItem;
import com.afforess.minecartmaniacore.minecart.MinecartManiaMinecart;
import com.afforess.minecartmaniacore.entity.MinecartManiaPlayer;
import com.afforess.minecartmaniacore.minecart.MinecartManiaStorageCart;
import com.afforess.minecartmaniacore.world.MinecartManiaWorld;
import com.afforess.minecartmaniacore.utils.StringUtils;

public class SensorItem extends GenericSensor{
	private AbstractItem detect = null;

	private static final long serialVersionUID = 4941223452342L;
	public SensorItem(SensorType type, Sign sign, String name, AbstractItem item) {
		super(type, sign, name);
		this.detect = item;
	}

	public void input(MinecartManiaMinecart minecart) {
		boolean state = false;
		if (minecart != null) {
			if (minecart.isStorageMinecart()) {
				if (((MinecartManiaStorageCart)minecart).amount(this.detect.type()) > (detect.isInfinite() ? 0 : detect.getAmount())) {
					state = true;
				}
			}
			else if (minecart.hasPlayerPassenger()) {
				MinecartManiaPlayer player = MinecartManiaWorld.getMinecartManiaPlayer(minecart.getPlayerPassenger());
				if (player.amount(this.detect.type()) > (detect.isInfinite() ? 0 : detect.getAmount())) {
					state = true;
				}
			}
		}
		setState(state);
	}
	
	public String toString() {
		return "[" + StringUtils.removeBrackets(format()) + ":" + detect.getId() + ":" + detect.getData() + "]";
	}
}
