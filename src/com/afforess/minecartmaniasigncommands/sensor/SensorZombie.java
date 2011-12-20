package com.afforess.minecartmaniasigncommands.sensor;

import org.bukkit.block.Sign;
import org.bukkit.entity.Zombie;

import com.afforess.minecartmaniacore.minecart.MinecartManiaMinecart;

public class SensorZombie extends GenericSensor {
    
    public SensorZombie(final SensorType type, final Sign sign, final String name) {
        super(type, sign, name);
    }
    
    public void input(final MinecartManiaMinecart minecart) {
        if (minecart != null) {
            setState(minecart.minecart.getPassenger() instanceof Zombie);
        } else {
            setState(false);
        }
    }
}
