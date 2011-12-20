package com.afforess.minecartmaniasigncommands.sensor;

import org.bukkit.block.Sign;

import com.afforess.minecartmaniacore.minecart.MinecartManiaMinecart;
import com.afforess.minecartmaniacore.utils.ItemUtils;

public class SensorItemHeld extends GenericSensor {
    
    private final Sign sign;
    
    public SensorItemHeld(final SensorType type, final Sign sign, final String name) {
        super(type, sign, name);
        this.sign = sign;
    }
    
    public void input(final MinecartManiaMinecart minecart) {
        
        if (minecart != null) {
            if (minecart.hasPlayerPassenger() && (minecart.getPlayerPassenger().getItemInHand() != null)) {
                setState(minecart.getPlayerPassenger().getItemInHand().equals(ItemUtils.getItemStringToMaterial(sign.getLine(2))));
            }
        } else {
            setState(false);
        }
        
    }
    
}
