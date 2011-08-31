package com.afforess.minecartmaniasigncommands.sign;

import org.bukkit.Bukkit;

import com.afforess.minecartmaniacore.event.MinecartMeetsConditionEvent;
import com.afforess.minecartmaniacore.minecart.MinecartManiaMinecart;
import com.afforess.minecartmaniacore.signs.Sign;
import com.afforess.minecartmaniacore.signs.SignAction;

public class EjectionConditionAction implements SignAction {
	private Sign sign;
	public EjectionConditionAction(Sign sign) {
		this.sign = sign;
	}

	public boolean execute(MinecartManiaMinecart minecart) {
		MinecartMeetsConditionEvent mmce = new MinecartMeetsConditionEvent(minecart, this.sign);
		Bukkit.getServer().getPluginManager().callEvent(mmce);
		return mmce.isMeetCondition();
	}

	public boolean async() {
		return false;
	}

	public boolean valid(Sign sign) {
		if (sign.getLine(0).toLowerCase().contains("ejection")) {
			sign.addBrackets();
			return true;
		}
		return false;
	}

	public String getName() {
		return "ejectionconditionsign";
	}

	public String getFriendlyName() {
		return "Ejection Condition Sign";
	}
}
