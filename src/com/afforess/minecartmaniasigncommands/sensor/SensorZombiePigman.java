package com.afforess.minecartmaniasigncommands.sensor;

import org.bukkit.block.Sign;
import org.bukkit.entity.PigZombie;

import com.afforess.minecartmaniacore.minecart.MinecartManiaMinecart;

public class SensorZombiePigman extends GenericSensor {
    
    public SensorZombiePigman(final SensorType type, final Sign sign, final String name) {
        super(type, sign, name);
    }
    
    public void input(final MinecartManiaMinecart minecart) {
        if (minecart != null) {
            setState(minecart.minecart.getPassenger() instanceof PigZombie);
        } else {
            setState(false);
        }
        
    }
    
}
