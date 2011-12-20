package com.afforess.minecartmaniasigncommands.sensor;

import org.bukkit.block.Sign;

import com.afforess.minecartmaniacore.minecart.MinecartManiaMinecart;

public class SensorAll extends GenericSensor {
    
    public SensorAll(final SensorType type, final Sign sign, final String name) {
        super(type, sign, name);
    }
    
    public void input(final MinecartManiaMinecart minecart) {
        if (minecart != null) {
            setState(true);
        } else {
            setState(false);
        }
    }
}
