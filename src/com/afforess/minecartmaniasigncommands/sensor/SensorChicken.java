package com.afforess.minecartmaniasigncommands.sensor;

import org.bukkit.block.Sign;
import org.bukkit.entity.Chicken;

import com.afforess.minecartmaniacore.minecart.MinecartManiaMinecart;

public class SensorChicken extends GenericSensor {
    
    public SensorChicken(final SensorType type, final Sign sign, final String name) {
        super(type, sign, name);
    }
    
    public void input(final MinecartManiaMinecart minecart) {
        if (minecart != null) {
            setState(minecart.minecart.getPassenger() instanceof Chicken);
        } else {
            setState(false);
        }
    }
}