package com.afforess.minecartmaniasigncommands.sensor;

import org.bukkit.block.Sign;

import com.afforess.minecartmaniacore.minecart.MinecartManiaMinecart;
import com.afforess.minecartmaniacore.world.MinecartManiaWorld;

public class SensorStation extends GenericSensor {
    
    private final Sign sign;
    
    public SensorStation(final SensorType type, final Sign sign, final String name) {
        super(type, sign, name);
        this.sign = sign;
    }
    
    public void input(final MinecartManiaMinecart minecart) {
        
        if (minecart != null) {
            if (minecart.hasPlayerPassenger()) {
                setState(sign.getLine(2).equals(MinecartManiaWorld.getMinecartManiaPlayer(minecart.getPlayerPassenger()).getLastStation()));
            }
        } else {
            setState(false);
        }
        
    }
    
}
