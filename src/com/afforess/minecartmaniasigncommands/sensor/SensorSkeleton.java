package com.afforess.minecartmaniasigncommands.sensor;

import org.bukkit.block.Sign;
import org.bukkit.entity.Skeleton;

import com.afforess.minecartmaniacore.minecart.MinecartManiaMinecart;

public class SensorSkeleton extends GenericSensor {
    
    public SensorSkeleton(final SensorType type, final Sign sign, final String name) {
        super(type, sign, name);
    }
    
    public void input(final MinecartManiaMinecart minecart) {
        if (minecart != null) {
            setState(minecart.minecart.getPassenger() instanceof Skeleton);
        } else {
            setState(false);
        }
    }
}
