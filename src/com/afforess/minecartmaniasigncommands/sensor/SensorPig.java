package com.afforess.minecartmaniasigncommands.sensor;

import org.bukkit.block.Sign;
import org.bukkit.entity.Pig;

import com.afforess.minecartmaniacore.minecart.MinecartManiaMinecart;

public class SensorPig extends GenericSensor {
    
    public SensorPig(final SensorType type, final Sign sign, final String name) {
        super(type, sign, name);
    }
    
    public void input(final MinecartManiaMinecart minecart) {
        if (minecart != null) {
            setState(minecart.minecart.getPassenger() instanceof Pig);
        } else {
            setState(false);
        }
    }
}