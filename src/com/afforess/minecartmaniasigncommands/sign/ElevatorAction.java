package com.afforess.minecartmaniasigncommands.sign;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.util.Vector;

import com.afforess.minecartmaniacore.minecart.MinecartManiaMinecart;
import com.afforess.minecartmaniacore.signs.Sign;
import com.afforess.minecartmaniacore.signs.SignAction;
import com.afforess.minecartmaniacore.signs.SignManager;
import com.afforess.minecartmaniacore.utils.DirectionUtils.CompassDirection;
import com.afforess.minecartmaniacore.utils.MinecartUtils;

public class ElevatorAction implements SignAction {
    
    protected Location sign;
    
    public ElevatorAction(final Sign sign) {
        this.sign = sign.getLocation();
    }
    
    protected Sign getSign() {
        return SignManager.getSignAt(sign);
    }
    
    protected Location calculateElevatorStop(final MinecartManiaMinecart minecart) {
        //get the offset of the track just after the sign in the current facing direction
        Vector facing = new Vector(0, 0, 0);
        if (MinecartUtils.validDirection(minecart.getDirectionOfMotion())) {
            facing = minecart.getDirectionOfMotion().toVector(1);
        }
        
        final Location search = sign.clone();
        Location nextFloor = null;
        for (int i = 0; i < 128; i++) {
            if (i != sign.getY()) {
                search.setY(i);
                final Sign temp = SignManager.getSignAt(search);
                if (temp != null) {
                    if (temp.hasSignAction(ElevatorAction.class)) {
                        nextFloor = search.clone();
                        nextFloor.setX(nextFloor.getX() + facing.getBlockX());
                        nextFloor.setZ(nextFloor.getZ() + facing.getBlockZ());
                        //give priority to the minecart current facing direction
                        if (MinecartUtils.isTrack(nextFloor))
                            return nextFloor;
                        
                        for (CompassDirection td : MinecartUtils.getValiddirections()) {
                            nextFloor.setX(nextFloor.getX() + td.toVector(1).getBlockX());
                            nextFloor.setZ(nextFloor.getZ() + td.toVector(1).getBlockZ());
                            final double speed = minecart.getPreviousMotion().length();
                            if (MinecartUtils.isTrack(nextFloor)) {
                                minecart.setMotion(findOppositeDir(td), speed);
                                return nextFloor;
                            }
                        }
                    }
                }
            }
        }
        return null;
    }
    
    private CompassDirection findOppositeDir(CompassDirection td) {
        switch (td) {
            case NORTH:
                return CompassDirection.SOUTH;
            case EAST:
                return CompassDirection.WEST;
            case SOUTH:
                return CompassDirection.NORTH;
            case WEST:
                return CompassDirection.EAST;
        }
        return null;
    }
    
    public boolean execute(final MinecartManiaMinecart minecart) {
        final Block ahead = minecart.getBlockTypeAhead();
        if ((ahead != null) && (ahead.getState() instanceof org.bukkit.block.Sign)) {
            final Location teleport = calculateElevatorStop(minecart);
            if (teleport != null) {
                minecart.minecart.teleport(teleport);
                return true;
            }
        }
        return false;
    }
    
    public boolean async() {
        return false;
    }
    
    public boolean valid(final Sign sign) {
        for (final String line : sign.getLines()) {
            if (line.toLowerCase().contains("elevator") || line.toLowerCase().contains("lift up") || line.toLowerCase().contains("lift down"))
                return true;
        }
        return false;
    }
    
    public String getName() {
        return "elevatorsign";
    }
    
    public String getFriendlyName() {
        return "Elevator Sign";
    }
    
}
