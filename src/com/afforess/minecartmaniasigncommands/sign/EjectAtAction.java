package com.afforess.minecartmaniasigncommands.sign;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;

import com.afforess.minecartmaniacore.config.ControlBlockList;
import com.afforess.minecartmaniacore.event.MinecartPassengerEjectEvent;
import com.afforess.minecartmaniacore.minecart.MinecartManiaMinecart;
import com.afforess.minecartmaniacore.signs.FailureReason;
import com.afforess.minecartmaniacore.signs.Sign;
import com.afforess.minecartmaniacore.signs.SignAction;
import com.afforess.minecartmaniacore.utils.StringUtils;

public class EjectAtAction implements SignAction, FailureReason {
    World world;
    boolean invalidCoords = false;
    boolean invalidRotation = false;
    Location teleport = null;
    
    public EjectAtAction(final Sign sign) {
        world = sign.getLocation().getWorld();
    }
    
    public boolean execute(final MinecartManiaMinecart minecart) {
        if (minecart.minecart.getPassenger() == null)
            return false;
        if (!ControlBlockList.isValidEjectorBlock(minecart))
            return false;
        final Entity passenger = minecart.minecart.getPassenger();
        minecart.setDataValue("Eject At Sign", true);
        final MinecartPassengerEjectEvent mpee = new MinecartPassengerEjectEvent(minecart, passenger);
        Bukkit.getServer().getPluginManager().callEvent(mpee);
        minecart.setDataValue("Eject At Sign", null);
        if (!mpee.isCancelled()) {
            minecart.minecart.eject();
            return passenger.teleport(teleport);
        }
        return false;
    }
    
    public boolean async() {
        return false;
    }
    
    public boolean valid(final Sign sign) {
        for (int i = 0; i < (sign.getNumLines() - 1); i++) {
            if (sign.getLine(i).toLowerCase().contains("eject at")) {
                try {
                    final String coords[] = StringUtils.removeBrackets(sign.getLine(i + 1)).split(":");
                    final double x = Double.parseDouble(coords[0].trim());
                    final double y = Double.parseDouble(coords[1].trim());
                    final double z = Double.parseDouble(coords[2].trim());
                    teleport = new Location(world, x, y, z);
                    if (((i + 2) < sign.getNumLines()) && !sign.getLine(i + 2).trim().isEmpty()) {
                        try {
                            final String rotation[] = StringUtils.removeBrackets(sign.getLine(i + 2)).split(":");
                            float pitch = 0;
                            float yaw = 0;
                            if (rotation.length > 1) {
                                pitch = Float.parseFloat(rotation[1].trim());
                                yaw = Float.parseFloat(rotation[0].trim());
                            } else if (rotation.length > 0) {
                                yaw = Float.parseFloat(rotation[0].trim());
                            }
                            teleport.setPitch(pitch);
                            teleport.setYaw(yaw);
                        } catch (final Exception e) {
                            invalidRotation = true;
                        }
                    }
                } catch (final Exception e) {
                    invalidCoords = true;
                }
            }
        }
        if ((teleport != null) && !invalidRotation && !invalidCoords) {
            sign.addBrackets();
            return true;
        }
        return false;
    }
    
    public String getName() {
        return "ejectatsign";
    }
    
    public String getFriendlyName() {
        return "Eject At Sign";
    }
    
    public String getReason() {
        if (invalidCoords)
            return "Invalid Coordinates. Coordinate should be separated by ':'. \n(e.g \"44:55:-56\")";
        else if (invalidRotation)
            return "Invalid Pitch/Yaw. Pitch and Yaw should be separated by ':'. \n(e.g \"180:35\")";
        return null;
    }
    
}
