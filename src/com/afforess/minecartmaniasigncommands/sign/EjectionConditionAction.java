package com.afforess.minecartmaniasigncommands.sign;

import org.bukkit.Bukkit;

import com.afforess.minecartmaniacore.event.MinecartMeetsConditionEvent;
import com.afforess.minecartmaniacore.minecart.MinecartManiaMinecart;
import com.afforess.minecartmaniacore.signs.Sign;
import com.afforess.minecartmaniacore.signs.SignAction;

public class EjectionConditionAction implements SignAction {
    private final Sign sign;
    
    public EjectionConditionAction(final Sign sign) {
        this.sign = sign;
    }
    
    public boolean execute(final MinecartManiaMinecart minecart) {
        final MinecartMeetsConditionEvent mmce = new MinecartMeetsConditionEvent(minecart, sign);
        Bukkit.getServer().getPluginManager().callEvent(mmce);
        return mmce.isMeetCondition();
    }
    
    public boolean async() {
        return false;
    }
    
    public boolean valid(final Sign sign) {
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
