package com.afforess.minecartmaniasigncommands.sign;

import org.bukkit.Location;
import org.bukkit.entity.Entity;

import com.afforess.minecartmaniacore.config.ControlBlockList;
import com.afforess.minecartmaniacore.minecart.MinecartManiaMinecart;
import com.afforess.minecartmaniacore.signs.Sign;
import com.afforess.minecartmaniacore.signs.SignAction;
import com.afforess.minecartmaniacore.utils.EntityUtils;

public class EjectionAction implements SignAction {
    
    private final Location sign;
    
    public EjectionAction(final Sign sign) {
        this.sign = sign.getLocation();
    }
    
    public boolean execute(final MinecartManiaMinecart minecart) {
        if (minecart.minecart.getPassenger() == null)
            return false;
        if (!ControlBlockList.isValidEjectorBlock(minecart))
            return false;
        final Location location = EntityUtils.getValidLocation(sign.getBlock());
        if (location != null) {
            final Entity passenger = minecart.minecart.getPassenger();
            location.setPitch(passenger.getLocation().getPitch());
            location.setYaw(passenger.getLocation().getYaw());
            minecart.minecart.eject();
            return passenger.teleport(location);
        }
        return false;
    }
    
    public boolean async() {
        return false;
    }
    
    public boolean valid(final Sign sign) {
        for (final String line : sign.getLines()) {
            if (line.toLowerCase().contains("eject here")) {
                sign.addBrackets();
                return true;
            }
        }
        return false;
    }
    
    public String getName() {
        return "ejectionsign";
    }
    
    public String getFriendlyName() {
        return "Ejection Sign";
    }
    
}
