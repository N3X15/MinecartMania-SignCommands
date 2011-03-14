package com.afforess.minecartmaniasigncommands.sensor;

import org.bukkit.ChatColor;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;

import com.afforess.minecartmaniacore.utils.ChatUtils;
import com.afforess.minecartmaniacore.utils.ItemUtils;

public abstract class SensorConstructor {
	
	public static boolean isInactiveSensor(Sign sign) {
		String[] type = sign.getLine(0).split(":");
		if (type.length != 2){
			return false;
		}
		type[1].trim();
		SensorType sensorType = SensorType.fromName(type[1]);
		if (sensorType == null) {
			return false;
		}
		return true;
	}
	
	public static Sensor constructSensor(Sign sign, Player player) {
		if (isInactiveSensor(sign)) {
			if (sign.getLine(1).trim().isEmpty()) {
				if (player != null) {
					ChatUtils.sendMultilineWarning(player, "Sensors Must Be Given a Unique Name, On Line 2");
				}
				return null;
			}
			SensorType sensorType = SensorType.fromName(sign.getLine(0).split(":")[1].trim());
			
			//Special Cases
			if (sensorType == SensorType.DETECT_ITEM) {
				if (ItemUtils.getFirstItemStringToMaterial(sign.getLine(2)) == null) {
					if (player != null) {
						ChatUtils.sendMultilineWarning(player, "Item Sensors Must Have a Valid Item Name or id, On Line 3");
					}
					return null;
				}
			}
			if (sensorType == SensorType.DETECT_PLYR_NAME) {
				if (sign.getLine(2).trim().isEmpty()) {
					if (player != null) {
						ChatUtils.sendMultilineWarning(player, "Player Name Sensors Must Have The Name Of The Player To Detect, On Line 3");
					}
					return null;
				}
			}
			
			
			String name = sign.getLine(1).trim();
			Sensor sensor = null;
			switch(sensorType){
				case DETECT_ALL: sensor = new SensorAll(sensorType, sign, name); break;
				case DETECT_ENTITY: sensor = new SensorEntity(sensorType, sign, name); break;
				case DETECT_EMPTY: sensor = new SensorEmpty(sensorType, sign, name); break;
				case DETECT_MOB: sensor = new SensorMob(sensorType, sign, name); break;
				case DETECT_ANIMAL: sensor = new SensorAnimal(sensorType, sign, name); break;
				case DETECT_PLAYER: sensor = new SensorPlayer(sensorType, sign, name); break;
				case DETECT_STORAGE: sensor = new SensorStorage(sensorType, sign, name); break;
				case DETECT_POWERED: sensor = new SensorPowered(sensorType, sign, name); break;
				case DETECT_ITEM: sensor = new SensorItem(sensorType, sign, name, ItemUtils.getFirstItemStringToMaterial(sign.getLine(2))); break;
				case DETECT_PLYR_NAME: sensor = new SensorPlayerName(sensorType, sign, name, sign.getLine(2).trim()); break;
				case DETECT_ZOMBIE: sensor = new SensorZombie(sensorType, sign, name); break;
				case DETECT_SKELETON: sensor = new SensorSkeleton(sensorType, sign, name); break;
				case DETECT_CREEPER: sensor = new SensorCreeper(sensorType, sign, name); break;
				case DETECT_PIG: sensor = new SensorPig(sensorType, sign, name); break;
				case DETECT_SHEEP: sensor = new SensorSheep(sensorType, sign, name); break;
				case DETECT_COW: sensor = new SensorCow(sensorType, sign, name); break;
				case DETECT_CHICKEN: sensor = new SensorChicken(sensorType, sign, name); break;
			}
			if (player != null) {
				ChatUtils.sendMultilineMessage(player, "Sensor Successfully Created", ChatColor.GREEN.toString());
			}
			return sensor;
		}
		return null;
	}

}