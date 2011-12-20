package com.afforess.minecartmaniasigncommands.sensor;

import org.bukkit.block.Sign;

import com.afforess.minecartmaniacore.minecart.MinecartManiaMinecart;

public class SensorPlayerName extends GenericSensor {
    private final String player;
    
    public SensorPlayerName(final SensorType type, final Sign sign, final String name, final String player) {
        super(type, sign, name);
        this.player = player;
    }
    
    public void input(final MinecartManiaMinecart minecart) {
        boolean state = false;
        if (minecart != null) {
            if (minecart.hasPlayerPassenger()) {
                if (minecart.getPlayerPassenger().getName().equals(player)) {
                    state = true;
                }
            }
        }
        setState(state);
    }
}
